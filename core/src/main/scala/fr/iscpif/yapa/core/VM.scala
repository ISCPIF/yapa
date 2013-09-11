package fr.iscpif.yapa.core

import scala.sys.process.Process

class VM() {

  private var paths = ""
  private var pathqs = ""
  private var vm : Process = new Process {
    def exitValue(): Int = 0

    def destroy() {}
  }
  private var pathFolder = ""

  def startVM = {
    vm = Process(pathqs+" "+paths+" -redir tcp:2222::22 -nographic").run()
    Thread.sleep(5000)
    val connect = new SshObject("127.0.0.1", 2222, "yapa", "yapa")
    println("upload start")
    connect.upload(pathFolder, "~/")
    println("END startVM")
  }

  def stopVM = {
    val connect = new SshObject("127.0.0.1", 2222, "yapa", "yapa")
    connect.download("~/", pathFolder)
    vm.destroy()
    println("VM destroy")
  }

  def setQemuPath(path : String) = {
    pathqs = path
  }

  def setVMPath(path : String) = {
    paths = path
  }

  def getQemuPath : String = {
    return pathqs
  }

  def setCopyFolder(str : String) = {
    pathFolder = str
  }
}