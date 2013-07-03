package fr.iscpif.yapa.core

import scala.sys.process.Process

class VM(pathq:String, path:String) {

  lazy val paths = path
  lazy val pathqs = pathq
  println(paths)
  def process = {
    Process(pathqs+" "+paths+" -redir tcp:2222::22 -nographic").run()
  }
}