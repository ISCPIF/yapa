package fr.iscpif.yapa.core

import java.io.File
import java.util

object Yapa extends App {
  val tab = new util.Vector[File]
  var pathVM = ""

  try {
  val choice = (new File("/media/martin-port/test/")).listFiles()
  choice.foreach(x => {
    if ((x.getName).contains(".img"))
    {tab.add(x)}})
    test()
  }
  catch {case e: Exception => }

  def setQemuPath(path : String) = {
    Global.vm.setQemuPath(path)
  }

  def test() = {
    choseVM("/media/martin-port/test/DDV.img")
     start
  }

  def choseVM(index : Int) : Boolean = {
     if (index< 0 || index > (tab.size() - 1))
       return false
    else {
       pathVM = tab.elementAt(index).getAbsolutePath
     }
    return true
  }

  def choseVM(path : String) : Boolean = {
    val file = new File(path)
    if(!file.exists())
      return false
    else {
      pathVM = path
    }
    return true
  }

  def seeVMavaible() : util.Vector[String] = {
    val rval = new util.Vector[String]
    for (i <- 0 to (tab.size() - 1))
    {
      rval.add(tab.elementAt(i).getName)
    }
   return rval
  }

  def start() = {

    try {
      JCTermSwingFrame.main(new Array[String](0))
    }
    finally { println("prog end")
    }
  }
}