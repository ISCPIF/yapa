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
import java.nio.file.{ StandardCopyOption, Paths, Files }
import org.openmole.misc.tools.io.FileUtil._
//import org.openmole.misc.tools.io.DirUtils._

import org.openmole.ide.plugin.task.systemexec.SystemExecTaskDataUI010
import org.openmole.ide.core.implementation.serializer.GUISerializer
import org.openmole.ide.core.implementation.dataproxy.{ Proxies, TaskDataProxyUI }

import sys.process._

object Yapa extends App {

  try {
    val command = Command.parse(args.toList)

    // build an new sandboxed working folder
    val yapaUserDir = Files.createDirectories(Paths.get(System.getProperty("user.home"), ".yapa/"))
    val careDir = Files.createTempDirectory(yapaUserDir, "")

    // copy the CARE executable into temporary careDir
    val care = Files.createTempFile(careDir, "care_", "")
    // TODO choose CARE binary according to underlying OS ( System.getProperty("blabla") )
    Files.copy(getClass.getClassLoader.getResourceAsStream("care-x86_64"), care, StandardCopyOption.REPLACE_EXISTING)
    care.toFile.setExecutable(true)

    // run CARE (line break mandatory to prevent ! to consume next line)
    // force run in current shell
    Process(care + " -m -1 -o " + command.outputDir + "/" + command.workingDir + "/ " + command.launchingCommand) !

    // hack to allow copy of the archive in OpenMOLE (some path would be d--------- otherwise...)
    Process("/bin/chmod -R 777 " + command.outputDir + "/" + command.workingDir + "/" + "rootfs") !

    // remove ignored paths
    command.ignore.foreach {
      i =>
        IOTools(IOTools.find(i, command.outputDir + "/" + command.workingDir).headOption, {
          // FIXME get DirUtils from OpenMOLE
          f: File =>
            //            if (Files.isDirectory(f)) DirUtils.delete(f)
            //            else
            Files.delete(f)
        })
    }

    // add arbitrary requested path to archive
    command.additions.foreach {
      i =>
        val f = Paths.get(i)
        val dest = Paths.get(command.outputDir, command.workingDir, i)
        Files.createDirectories(dest)
        Files.copy(f, dest)
    }

    val careExe = "re-execute.sh"
    val taskName = command.executable + "Task"

    // generate GUI task
    val proxies = new Proxies
    proxies += TaskDataProxyUI(new SystemExecTaskDataUI010(taskName, command.workingDir, careExe, List((new File(command.outputDir + "/" + command.workingDir), command.workingDir))))
    (new GUISerializer).serialize(command.outputDir + "/" + command.executable + ".om", proxies, Iterable(), saveFiles = command.embedded)

    // generate DSL task
    println("import org.openmole.plugin.task.systemexec.SystemExecTask\n" +
      "val " + taskName + " = SystemExecTask(" + List("\"" + taskName + "\"", "\"" + careExe + "\"", "\"" + command.workingDir + "\"").mkString(",") + ")\n" +
      taskName + " addResource \"" + command.outputDir + "/" + command.workingDir + "\"")

    // clean up temporary files
    Files.delete(care)
    Files.delete(careDir)

  } catch {
    case e: Throwable =>
      // FIXME remove debug print
      e.printStackTrace()
      println("Invalid command")
  }
}
