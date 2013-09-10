package fr.iscpif.yapa.core

import java.io.File
import java.util
import sbt.Resolver.file

object Yapa extends App {
  private val tab = new util.Vector[File]
  private var pathVM = ""

  override def main(args: Array[String]) {
    setQemuPath(args(0))
    choseVM(args(1))
    start
  }

  def setQemuPath(path : String) = {
    Global.vm.setQemuPath(path)
  }

  def setCopyFolder(path : String) = {
Glob
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

  def seeVMavaible(folderpath : String) : util.Vector[String] = {
    try {
      val file = new File(folderpath)
      if (!(file.exists()))
        return null
      val choice = file.listFiles()
      choice.foreach(x => {
        if ((x.getName).contains(".img"))
        {tab.add(x)}})
    }
    catch {case e: Exception => }

    val rval = new util.Vector[String]
    for (i <- 0 to (tab.size() - 1))
    {
      rval.add(tab.elementAt(i).getName)
    }
   return rval
  }

  def start() = {
    try {
      if (pathVM != "" && Global.vm.getQemuPath != "")
      {
      Global.vm.setVMPath(pathVM)
      JCTermSwingFrame.main(new Array[String](0))
      }
    }
    finally { println("prog end")
    }
  }
}