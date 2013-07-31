package fr.iscpif.yapa.core

import net.schmizz.sshj.common.StreamCopier
import java.io.{PipedOutputStream, PipedInputStream, OutputStream}
import net.schmizz.sshj.connection.channel.direct.Session

class StreamTransit {
  val output = new PipedOutputStream()
  val input = new PipedInputStream(output)


  def show (shell : Session.Shell) {
    // new StreamCopier(System.in, output).bufSize(shell.getRemoteMaxPacketSize).spawn("stdin")
    // new StreamCopier(input, shell.getOutputStream).bufSize(shell.getRemoteMaxPacketSize).spawn("stdin")

    val buf = new Array[Byte](shell.getRemoteMaxPacketSize)

    while (System.in.read(buf) != -1)
    {
      shell.getOutputStream.write(buf)
      shell.getOutputStream.flush()
    }
       /* i match {
        case -1 =>
        case 3 => test = false
        case 4 => test = false
        case 8 => print("\b \b")
        case 13 =>
        case _ =>

    }*/
  }
}
