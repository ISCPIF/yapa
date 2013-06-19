package fr.iscpif.yapa.core

import net.schmizz.sshj._
import net.schmizz.sshj.transport.verification.HostKeyVerifier
import java.security.PublicKey
import net.schmizz.sshj.connection.channel.direct.Session
import scala.util._


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

  def retry[T](f: => T, i:Int): T = {
    println("Wait. . .")
   Try(f) match {
     case Success(r) => r
     case Failure(e) => {if (i < 200) {retry(f, i + 1)}
     else
     {throw new RuntimeException("VM crash")}}
   }
  }


  def connection = {
    println("Try connection")
    retry(ssh.connect(hosts, ports), 0)
    try {
      ssh.authPassword(users, mdp)
      val session:Session = retry(ssh.startSession, 0)
      println("Seem legit")
      exec(session, "sudo -i")
      var test : Boolean = true
      while(test) {
        val strO = session.getOutputStream
        val strI = session.getInputStream
        val cmd = readLine()
        if (cmd != "")
        {exec(session, cmd)}
        println("Output = "+strO)
        println("Input = "+strI)
        if (cmd == "stop")
        {test = false}
      }
      session.close
    } finally { ssh.disconnect
    println("finally")}
  }
}

class VM(path:String) {

  lazy val paths = "/usr/bin/qemu-system-x86_64 "+path+" -redir tcp:2222::22"
  println(paths)
  def process = {
    Runtime.getRuntime.exec(paths)
  }
}

object Yapa extends App {
  val vm = new VM("/media/martin-port/test/DDV.img")
  val p = vm.process
  val ssh = new SshObject("localhost", 2222, "yapa", "yapa")
  try
  ssh.connection
  finally { println("prog end")
    p.destroy()}
}