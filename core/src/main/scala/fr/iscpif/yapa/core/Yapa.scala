package fr.iscpif.yapa.core

import java.io.File
import java.util

object Yapa extends App {
  private val tab = new util.Vector[File]
  private var pathVM = ""

  override def main(args: Array[String]) {
    if (args.size > 0)
    {
      println(args(0))
      setQemuPath(args(0))
      if (args.size > 1)
      {
        println(args(1))
        setCopyFolder(args(1))
        if (args.size > 2) {
          println(args(2))
          choseVM(args(2))
          if (args.size > 3) {
            println(args(3))
            setStartCmd(args(3))
          }
        }
      }
    }
    println(start)
  }

  def setQemuPath(path : String) = {
    Global.vm.setQemuPath(path)
  }

  def setStartCmd(cmd : String) = {
    Global.vm.setCmd(cmd)
  }

  def setCopyFolder(path : String) = {
    Global.vm.setCopyFolder(path)
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
      tab.clear
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

  def start() : String = {
      if (pathVM != "" && Global.vm.getQemuPath != "" && pathVM != "" && Global.vm.getCmd != "")
      {
      Global.vm.setVMPath(pathVM)
      JCTermSwingFrame.main(new Array[String](0))
        return "start : OK"
      }
      else {
        return """yapa "QemuPath" "ProjectPath" "VirtualMachinePath" "CmdToRunYourProject" """
      }
  }
}