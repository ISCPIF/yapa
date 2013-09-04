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
  }

  def stopVM = {
    vm.destroy()
  }

  def setQemuPath(path : String) = {
    pathqs = path
  }

  def setVMPath(path : String) = {
    paths = path
  }
}