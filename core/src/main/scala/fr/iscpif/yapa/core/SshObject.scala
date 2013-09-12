package fr.iscpif.yapa.core

import java.security.PublicKey
import net.schmizz.sshj.connection.channel.direct.Session
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.transport.verification.HostKeyVerifier
import scala.util.{Failure, Success, Try}
import net.schmizz.sshj.xfer.FileSystemFile

class SshObject(host:String, port:Int, user:String, pass:String) {

  private val ssh = new SSHClient
  ssh.addHostKeyVerifier(new HostKeyVerifier {
    def verify(p1: String, p2: Int, p3: PublicKey) = true })
  ssh.loadKnownHosts

  private lazy val hosts = host
  private lazy val ports = port
  private lazy val users = user
  private lazy val mdp = pass
  private val timeout = 120

  private def withSession(f:(Session) => Unit) = {
    val session:Session = retry(ssh.startSession)
    f(session)
    session.close
  }

  def exec(cmd : String) = {
    def exec_cmd(session: Session) = {
        val str = session.exec(cmd)
        str.join
        //println("\n** exit status: " + str.getExitStatus);
    }
    withSession(exec_cmd)
  }

  private def verif() =  {
    def tryFunc(session: Session) = {    }
    withSession(tryFunc)
  }

  private def retry[T](f: => T): T = {
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
    ssh.setConnectTimeout(timeout * 1000)
    ssh.setTimeout(timeout * 1000)
    retry(ssh.connect(hosts, ports))
    ssh.authPassword(users, mdp)
  }

  def download(src : String, target : String) = {
      ssh.newSCPFileTransfer().download(src, new FileSystemFile(target))
  }

  def upload(src : String, target : String) = {
    verif
    //ssh.useCompression()
    ssh.newSCPFileTransfer().upload(new FileSystemFile(src), target)
  }
}