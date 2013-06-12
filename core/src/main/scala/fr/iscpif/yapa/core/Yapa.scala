package fr.iscpif.yapa.core

import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.transport.verification.HostKeyVerifier
import java.security.PublicKey
;

class SshObject {
  val ssh = new SSHClient
  ssh.addHostKeyVerifier(new HostKeyVerifier {
    def verify(p1: String, p2: Int, p3: PublicKey) = true })
  def connection(host:String, user:String, mdp:String) = {
    ssh.loadKnownHosts
    ssh.connect(host)
    try {
      ssh.authPassword(user, mdp)
      val session = ssh.startSession
      println("Seem legit")
      session.close
    } finally { ssh.disconnect }
  }

}

object Yapa extends App {
  val ssh = new SshObject
  ssh.connection("localhost", "yapa", "yapa")
}
