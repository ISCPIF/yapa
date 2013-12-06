package fr.iscpif.yapa

import at.loveoneanother.schale._
import java.io.File
import fr.iscpif.yapa.tools.IOTools._
import fr.iscpif.yapa.tools.IOTools
import org.openmole.ide.plugin.task.systemexec.SystemExecTaskDataUI010
import java.util.UUID
import org.openmole.misc.tools.io.FileUtil._
import org.openmole.ide.core.implementation.serializer.GUISerializer
import org.openmole.ide.core.implementation.dataproxy.{Proxies, TaskDataProxyUI}

object Yapa extends App {

  try {
    val command = Command.parse(args.toList)

    // Build an new sandboxed working folder
    val uuid = UUID.randomUUID.toString
    val rootdir = new File(System.getProperty("user.home"), ".yapa/" + uuid)
    rootdir.mkdirs

    //Copy the cde executable into the rootdir
    val cde = File.createTempFile("tmp", "cde", rootdir)
    getClass.getClassLoader.getResourceAsStream("cde_2011-08-15_64bit").copy(cde)
    cde.setExecutable(true)

    //Run CDEPack
    val shell = Shell(cde + " " + command.launchingCommand)(new Env(pwd = rootdir))
   shell.waitFor
  //  println(shell)
    cde.delete

    //Copy cde-package into output folder
    command.outputDir.mkdirs
    rootdir.move(command.outputDir)

    command.ignore.foreach {
      i =>
        IOTools(IOTools.find(i, command.outputDir + "/cde-package").headOption, {
          f: File => f.delete
        })
    }

    val all = IOTools.find(command.executable, command.outputDir + "/cde-package/cde-root")
    val exe = IOTools(all.headOption, {
      f: File => f.getAbsolutePath
    })

    val workingDir = "cde-package" + exe.getParent.split("cde-package").last

    val proxies = new Proxies

    proxies += TaskDataProxyUI(new SystemExecTaskDataUI010(exe.getName + "Task", workingDir, command.stripedLaunchingCommand, List((new File(command.outputDir + "/cde-package"), "cde-package"))))

    (new GUISerializer).serialize(command.outputDir + "/" + exe.getName + ".om", proxies, Iterable(), saveFiles = command.embedd)
    println("val systemTask = new SystemExecTask(" + List(exe.getName + "Task", "\"" + command.stripedLaunchingCommand + "\"", "\"" + workingDir + "\"").mkString(",") + ")")
    rootdir.delete

  } catch {
    case e: Throwable =>
      println("Invalid command\n")
      Command.help
  }
}
