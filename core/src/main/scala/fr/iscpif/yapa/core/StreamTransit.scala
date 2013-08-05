package fr.iscpif.yapa.core

import net.schmizz.sshj.common.StreamCopier
import java.io.{PipedOutputStream, PipedInputStream, OutputStream}
import net.schmizz.sshj.connection.channel.direct.Session
import java.util

class StreamTransit {

  val tab = new  util.Vector[util.Vector[Byte]](1)
   this.tab.add(new util.Vector[Byte](1))

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
      var test = new  util.Vector[Boolean](4)
      test.clear()
      test.add(true)
      test.add(false)
      test.add(false)
      test.add(false)
      var up = 1
      val clear = "\b \b"
    while (test.elementAt(0) && (my_read(buf) != -1))
    {
      test.remove(3)
      test.add(3, false)
      buf.foreach(_ match {
        case 3 => {test.remove(0)
        test.add(0, false)}
        case 4 => {test.remove(0)
          test.add(0, false)}
        case 10 => { if (this.tab.elementAt(0).capacity > 1)
        {this.tab.add(0, new util.Vector[Byte](1))}
        up = 1}
        case 27 => {test.remove(1)
          test.add(1, true)}
        case 91 => {if (test.elementAt(1))
        {test.remove(1)
        test.add(1, false)
        test.remove(2)
        test.add(2, true)}}
        case 65 => {if (test.elementAt(2))
        {test.remove(2)
          test.add(2, false)
          test.remove(3)
          test.add(3, true)
          print(clear*4)
          if (up < (tab.capacity - 1))
          {
          print(tab.elementAt(up))
          up = (up + 1)
        }}}
        case 127 => print(clear*3)
        case _ =>
        })
      if (buf(0) != 10 && buf(0) != 127 && !(test.elementAt(1) || test.elementAt(2) || test.elementAt(3))) {
        this.tab.elementAt(0).add(buf(0))}
      shell.getOutputStream.write(buf, 0, read)
      shell.getOutputStream.flush()
      //println("")
    }}
    catch {case e: Exception => println(e)}
  }
}
