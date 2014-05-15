/*
 * Copyright (C) 2013-2014 <mathieu.Mathieu Leclaire at openmole.org>
 * Copyright (C) 2014 Jonathan Passerat-Palmbach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.iscpif.yapa

import java.io.File
import fr.iscpif.yapa.tools.IOTools._
import fr.iscpif.yapa.tools.IOTools
import org.openmole.ide.plugin.task.systemexec.SystemExecTaskDataUI010
import java.util.UUID
import org.openmole.misc.tools.io.FileUtil._
import org.openmole.ide.core.implementation.serializer.GUISerializer
import org.openmole.ide.core.implementation.dataproxy.{ Proxies, TaskDataProxyUI }

import sys.process._

object Yapa extends App {

  try {
    val command = Command.parse(args.toList)

    // Build an new sandboxed working folder
    val uuid = UUID.randomUUID.toString
    val cdedir = new File(System.getProperty("user.home"), ".yapa/" + uuid)
    cdedir.mkdirs

    //Copy the cde executable into the cdedir
    val cde = File.createTempFile("tmp", "cde", cdedir)
    getClass.getClassLoader.getResourceAsStream("cde_2011-08-15_64bit").copy(cde)
    cde.setExecutable(true)

    //Copy cde-package into output folder
    val cdeOutputDir = new File(command.outputDir + "/cde-package")
    cdeOutputDir.mkdirs

    //Run CDEPack (line break mandatory to prevent ! to consume next line)
    Process(cde + " -o " + cdeOutputDir + " " + command.launchingCommand) !

    cde.delete

    // remove ignored paths
    command.ignore.foreach {
      i =>
        IOTools(IOTools.find(i, cdeOutputDir).headOption, {
          f: File => f.delete
        })
    }

    // add arbitrary requested path to archive
    command.additions.foreach {
      i =>
        val f = new File(i)
        val dest = new File(cdeOutputDir + "/cde-root/" + i)
        dest.getParent.mkdirs
        f.copy(dest)
    }

    val all = IOTools.recursiveFind(command.executable + ".cde", cdeOutputDir + "/cde-root")

    val exe = IOTools(all.headOption, {
      f: File => f.getAbsolutePath
    })

    val workingDir = exe.getParent.split("cde-package").last

    val proxies = new Proxies

    val cleanExe = exe.getName.replace(".cde", "")

    proxies += TaskDataProxyUI(new SystemExecTaskDataUI010(cleanExe + "Task", workingDir, command.stripedLaunchingCommand, List((new File(command.outputDir + "/cde-package"), "cde-package"))))

    (new GUISerializer).serialize(command.outputDir + "/" + cleanExe + ".om", proxies, Iterable(), saveFiles = command.embedded)
    println("val systemTask = new SystemExecTask(" + List(cleanExe + "Task", "\"" + command.stripedLaunchingCommand + "\"", "\"" + workingDir + "\"").mkString(",") + ")\nsystemTask.addResource(new File(\"" + command.outputDir + "\"))")

    // clean temporary CDE dir and generated options file
    cdedir.delete
    val cdeoptions = new File("cde.options")
    cdeoptions.delete

  } catch {
    case e: Throwable =>
      println("Invalid command\n")
      Command.help
  }
}
