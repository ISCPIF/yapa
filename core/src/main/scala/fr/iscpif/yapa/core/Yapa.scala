package fr.iscpif.yapa.core

import net.schmizz.sshj._
import net.schmizz.sshj.transport.verification.HostKeyVerifier
import java.security.PublicKey
;

class SshObject {

  val ssh = new SSHClient
  ssh.addHostKeyVerifier(new HostKeyVerifier {
    def verify(p1: String, p2: Int, p3: PublicKey) = true })
  ssh.setConnectTimeout(0)
  ssh.setTimeout(0)

  def connection(host:String, port:Int, user:String, mdp:String) = {
    ssh.loadKnownHosts
    println("Try connection")
    ssh.connect(host, port)
    try {
      ssh.authPassword(user, mdp)
      val session = ssh.startSession
      println("Seem legit")
      session.close
    } finally { ssh.disconnect
    println("finally")}
  }

}

object Yapa extends App {
  val ssh = new SshObject
  ssh.connection("localhost", 2222, "yapa", "yapa")
}
