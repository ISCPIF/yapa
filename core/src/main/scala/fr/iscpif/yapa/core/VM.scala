package fr.iscpif.yapa.core

import scala.sys.process.Process
import net.schmizz.sshj.xfer.FileSystemFile

class VM() {

  private var paths = ""
  private var pathqs = ""
  private var vm : Process = new Process {
    def exitValue(): Int = 0

    def destroy() {}
  }
  private var pathFolder = ""
  private var execCmd = ""

  def startVM = {
    vm = Process(pathqs+" "+paths+" -redir tcp:2222::22 -nographic").run()
    Thread.sleep(5000)
    val connect = new SshObject("127.0.0.1", 2222, "yapa", "yapa")
    val f = new FileSystemFile(pathFolder)
    connect.connect
    connect.upload(pathFolder, f.getName)
    connect.disconnect
  }

  def stopVM = {
    val connect = new SshObject("127.0.0.1", 2222, "yapa", "yapa")
    val f = new FileSystemFile(pathFolder)
    connect.connect
    connect.exec("rm -rf "+f.getName)
    connect.download("cde-package", pathFolder)
    connect.exec("rm -rf cde-package")
    connect.disconnect
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

  def setCmd(str : String) = {
    execCmd = str
  }

  def getCmd : String = {
    return execCmd
  }
}