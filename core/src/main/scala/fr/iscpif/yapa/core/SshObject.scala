package fr.iscpif.yapa.core

import java.security.PublicKey
import net.schmizz.sshj.connection.channel.direct.Session
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.transport.verification.HostKeyVerifier
import scala.util.{Failure, Success, Try}
import net.schmizz.sshj.common.{StreamCopier}

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

  def withSession(f:(Session) => Unit) = {
    val session:Session = retry(ssh.startSession)
    println("Open session")
    f(session)
    session.close
    println("Close session")
  }

  def exec() =  {
    def do_cmd(session: Session) = {
      session.allocateDefaultPTY
      println("start TEST")
      val shell: Session.Shell = session.startShell
      new StreamCopier(shell.getInputStream, System.out).bufSize(shell.getLocalMaxPacketSize).spawn("stdout")
      new StreamCopier(shell.getErrorStream, System.err).bufSize(shell.getLocalMaxPacketSize).spawn("stderr")
     // try {
     // new StreamCopier(System.in, shell.getOutputStream).bufSize(shell.getRemoteMaxPacketSize).copy //.spawn("stdin")
     // }
     // catch {case e: Exception => }
     (new StreamTransit).show(shell)
      println(".")
      //var cmd: String = ""
      //while (cmd != "stop-YAPA")
      //{
      //   System.console.readPassword()
      //}
      //cmdReturn.join
      //val test = IOUtils.readFully(cmdReturn.getInputStream).toString
      //cmdReturn.close
      //println(test)
    }
    println("Start command")
    withSession(do_cmd)
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

  def disconnect = {
    println("Stop connection")
    ssh.disconnect
  }

  def connect = {
    println("Try connection")
    retry(ssh.connect(hosts, ports))
    ssh.authPassword(users, mdp)
  }

  def chat = {

    connect

     exec

    disconnect
  }
}