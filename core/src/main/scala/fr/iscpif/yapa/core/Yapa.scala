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

  def retry[T](f: => T, i:Int): T = {
    println("Wait. . .")
   Try(f) match {
     case Success(r) => r
     case Failure(e) => {if (i < 2000) {retry(f, i + 1)}
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
      session.exec("sudo -i")
      val str = session.getOutputStream
      session.exec("halt")
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
  println("qemu-system-x86_64 "+paths+" -redir tcp:2222::22")
  def process = runtime.exec("qemu-system-x86_64 "+paths+" -redir tcp:2222::22")
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