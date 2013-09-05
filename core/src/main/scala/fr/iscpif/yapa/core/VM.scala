package fr.iscpif.yapa.core

import scala.sys.process.Process

class VM() {

  var paths = ""
  var pathqs = ""
  var vm : Process = new Process {
    def exitValue(): Int = 0

    def destroy() {}
  }

  def startVM = {
    vm = Process(pathqs+" "+paths+" -redir tcp:2222::22 -nographic").run()
    Thread.sleep(5000)
    (new SshObject("127.0.0.1", 2222, "yapa", "yapa")).chat
  }

  def stopVM = {
    vm.destroy()
    println("VM destroy")
  }

  def setQemuPath(path : String) = {
    pathqs = path
  }

  def setVMPath(path : String) = {
    paths = path
  }
}