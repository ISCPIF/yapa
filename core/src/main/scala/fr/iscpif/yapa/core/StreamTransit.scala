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

    val buf = new Array[Byte](1)
    try
    {
      var test = true
    while ((my_read(buf) != -1) && test)
    {
      shell.getOutputStream.write(buf, 0, read)
      shell.getOutputStream.flush()
      //val str = buf.map(_.toInt)
       buf.foreach(print)
      buf.foreach(_ match {
        case 0x3 => test = false
        case 0x4 => test = false
        case 0x8 => print("\b \b")
        case _ =>
        //case x: Any => println("other : " + x)
        })

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
