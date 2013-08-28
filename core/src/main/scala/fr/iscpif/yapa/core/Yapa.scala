package fr.iscpif.yapa.core

import java.io.File
import java.util

object Yapa extends App {

  try {
  val choice = (new File("/media/martin-port/test/")).listFiles()
  val tab = new util.Vector[File]
  println("which one do you want ?")
  choice.foreach(x => {
    if ((x.getName).contains(".img"))
    {tab.add(x)}})
  for (i <- 0 to (tab.size() - 1))
  {
    println(i+"/ "+tab.elementAt(i).getName)
  }
  println(tab.size()+"/ Enter the VM's path")
  var f = readInt()
  while (f< 0 || f > tab.size())
  {println("wrong number. Which one do you want ?")
  f = readInt()}
  var path = ""
  if (f != tab.size()) {
   path = tab.elementAt(f).getAbsolutePath
  }
  else {
    println("path ?")
    path = readLine()
  }
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
      //ssh.chat
      JCTermSwing
    }
    finally { println("prog end")
      p.destroy()
    }
  }
}