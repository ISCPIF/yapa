package fr.iscpif.yapa.core

import net.schmizz.sshj._
import net.schmizz.sshj.transport.verification.HostKeyVerifier
import java.security.PublicKey


class SshObject(host:String, port:Int, user:String, pass:String) {

  val ssh = new SSHClient
  ssh.addHostKeyVerifier(new HostKeyVerifier {
    def verify(p1: String, p2: Int, p3: PublicKey) = true })
  ssh.setConnectTimeout(0)
  ssh.setTimeout(0)

  lazy val hosts = host
  lazy val ports = port
  lazy val users = user
  lazy val mdp = pass

  def connection = {
    ssh.loadKnownHosts
    println("Try connection")
    ssh.connect(hosts, ports)
    try {
      ssh.authPassword(users, mdp)
      val session = ssh.startSession
      println("Seem legit")
      session.close
    } finally { ssh.disconnect
    println("finally")}
  }
}

class VM(path:String) {

  lazy val paths = path

  def start = {


  }

}

object Yapa extends App {
  val ssh = new SshObject("localhost", 2222, "yapa", "yapa")
  ssh.connection
}