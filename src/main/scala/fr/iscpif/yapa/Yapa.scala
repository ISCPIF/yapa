package fr.iscpif.yapa

import at.loveoneanother.schale._
import java.io.{FileOutputStream, File}
import fr.iscpif.yapa.tools.IOTools._
import fr.iscpif.yapa.tools.IOTools

object Yapa extends App {
  val cde = File.createTempFile("tmp", "cde")
  cde.setExecutable(true)
  val folder = cde.getParent
  val cdeExe = "./" + cde.getName

  copy(getClass.getClassLoader.getResourceAsStream("cde_2011-08-15_64bit"), new FileOutputStream(cde))

  println(Shell("cd " + folder + "; " + cdeExe + " " + args.mkString(" ")))

  val exe = IOTools.find(args(0).getName, folder + "/cde-package").headOption match {
    case Some(f: File) => f.getAbsolutePath
    case _ => throw new Throwable("The executable has not been found in the cde-package tree. The packaging failed")
  }

 // new SystemExecTask(exe.getName, directory = workdir)
}
