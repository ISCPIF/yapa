package fr.iscpif.yapa.core

import net.schmizz.sshj.common.StreamCopier
import java.io.{PipedOutputStream, PipedInputStream, OutputStream}
import net.schmizz.sshj.connection.channel.direct.Session

class StreamTransit {
  val output = new PipedOutputStream()
  val input = new PipedInputStream(output)


  def show (shell : Session.Shell) {
    var read : Int = 0

    def my_read(buf :Array[Byte])
    {
      read = System.in.read(buf)
      read
    }

    val buf = new Array[Byte](shell.getRemoteMaxPacketSize)
    try
    {
      var test = true
    while ((my_read(buf) != -1) && test)
    {
      shell.getOutputStream.write(buf, 0, read)
      shell.getOutputStream.flush()
      val str = buf.toVector
      str.map(x => x.toInt match {
        case -1 =>
        case 3 => test = false
        case 4 => test = false
        case 8 => print("\b \b")
        case 13 =>
        case _ =>})

    }}
    catch {case e: Exception => }
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
