package fr.iscpif.yapa.core

import java.io.File

object Yapa extends App {

  def recursiveListFiles(f: File): Array[File] = {
    val these = f.listFiles
    these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
  }
  try {
  val choice = (new File("/media/martin-port/test/")).listFiles()
  println("which one do you want ?")
  var i = 0
  choice.foreach(x => {
    println(i+"/ "+x.getName)
    i = i + 1
  })
  println(i+"/ Enter the VM's path")
  var f = readInt()
  while (f< 0 || f > i)
  {println("wrong number. Which one do you want ?")
  f = readInt()}

  val path = choice(f).getAbsolutePath
  val file = new File(path);
  if(file.exists()) {
    start(path)
  }}
  catch {case e: Exception => }

  def start(path : String) = {
    val vm = new VM("qemu-system-x86_64", path)
    val p = vm.process
    Thread.sleep(5000)
    val ssh = new SshObject("localhost", 2222, "yapa", "yapa")
    try {
      ssh.chat
    }
    finally { println("prog end")
      p.destroy()
    }
  }
}