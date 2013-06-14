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
  ssh.loadKnownHosts

  lazy val hosts = host
  lazy val ports = port
  lazy val users = user
  lazy val mdp = pass

  def connection = {
    println("Try connection")
    var troll = true
    while (troll)
    {
      try {
       ssh.connect(hosts, ports)
      troll = false
      }
      catch {case _ => println("wait. . .")
      troll = true}
    }
    try {
      ssh.authPassword(users, mdp)
      val session = ssh.startSession
      println("Seem legit")
      session.exec("sudo -i")
      val str = session.getOutputStream
      println(str)
      var test : Boolean = true
      while(test) {
        test = readBoolean()
      }
      session.close
    } finally { ssh.disconnect
    println("finally")}
  }
}

class VM(path:String) {

  lazy val paths = path
  lazy val runtime = Runtime.getRuntime()

  def start = {
       println("qemu-system-x86_64 "+paths+" -redir tcp:2222::22")
       runtime.exec("qemu-system-x86_64 "+paths+" -redir tcp:2222::22")
  }

}

object Yapa extends App {
  val vm = new VM("/media/martin-port/test/DDV.img")
  vm.start
  val ssh = new SshObject("localhost", 2222, "yapa", "yapa")
  ssh.connection
}