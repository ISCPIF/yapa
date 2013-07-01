package fr.iscpif.yapa.core

import java.security.PublicKey
import net.schmizz.sshj.connection.channel.direct.Session
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.transport.verification.HostKeyVerifier
import scala.util.{Failure, Success, Try}
import net.schmizz.sshj.common.IOUtils

class SshObject(host:String, port:Int, user:String, pass:String) {

  val ssh = new SSHClient
  ssh.addHostKeyVerifier(new HostKeyVerifier {
    def verify(p1: String, p2: Int, p3: PublicKey) = true })
  ssh.setConnectTimeout(0)
  ssh.setTimeout(0)
  ssh.loadKnownHosts

  lazy val hosts = host
  lazy val ports = port
  lazy val users = user
  lazy val mdp = pass

  def execReturnCode(session: Session, cde: String) = {
    val cmd = session.exec(cde)
    try {
      cmd.join
      cmd.getExitStatus
    } finally cmd.close
  }

  def exec(session: Session, cde: String) = {
    val retCode = execReturnCode(session, cde)
    if (retCode != 0) throw new RuntimeException("Return code was no 0 but " + retCode)
  }

  def retry[T](f: => T): T = {
    def retry0[T](f: =>T, i:Int): T = {
      println("Wait. . .")
      Try(f) match {
        case Success(r) => r
        case Failure(e) => {if (i < 5) {
          Thread.sleep(2000)
          retry0(f, i + 1)}
        else
        {throw new RuntimeException("VM crash")}}
      }
    }
    retry0(f, 0)
  }


  def connection = {
    println("Try connection")
    retry(ssh.connect(hosts, ports))
    try {
      ssh.authPassword(users, mdp)
      var test : Boolean = true
      while(test) {
        val session:Session = retry(ssh.startSession)
        println("Seem legit")
        val cmd = readLine()
        if (cmd != "")
        {
         val cmdReturn = session.exec(cmd)
         cmdReturn.join
         val test = IOUtils.readFully(cmdReturn.getInputStream).toString
          cmdReturn.close
          println(test)
        }
        if (cmd == "stopyapa")
        {test = false}
        session.close
      }
    } finally { ssh.disconnect
      println("finally")}
  }
}