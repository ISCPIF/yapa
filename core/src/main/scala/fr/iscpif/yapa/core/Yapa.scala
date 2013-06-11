package fr.iscpif.yapa.core

import net.schmizz.sshj.SSHClient;

class SshObject {
  val ssh = new SSHClient();
  def connection(host:String, user:String, mdp:String) = {
    ssh.loadKnownHosts();
    ssh.connect(host);
    try {
      ssh.authPassword(user, mdp);
      val session = ssh.startSession();
      print("Seem legit");
      session.close();
    } finally { ssh.disconnect(); }
  }

}

object Yapa extends App {
  val ssh = new SshObject();
  ssh.connection("localhost", "martin-port", "");
}
