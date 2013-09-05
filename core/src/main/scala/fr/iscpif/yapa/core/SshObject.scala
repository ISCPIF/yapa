package fr.iscpif.yapa.core

import java.security.PublicKey
import net.schmizz.sshj.connection.channel.direct.Session
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.transport.verification.HostKeyVerifier
import scala.util.{Failure, Success, Try}
import net.schmizz.sshj.common.StreamCopier

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
    f(session)
    session.close
  }

  def exec() =  {
    def do_cmd(session: Session) = {
      session.allocateDefaultPTY
      val shell: Session.Shell = session.startShell
    }
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
    ssh.disconnect
  }

  def connect = {
    retry(ssh.connect(hosts, ports))
    ssh.authPassword(users, mdp)
  }

  def chat = {

    connect

     exec

    disconnect
  }
}