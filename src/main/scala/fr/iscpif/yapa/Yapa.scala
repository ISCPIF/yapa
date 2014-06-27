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

    // build an new sandboxed working folder
    val uuid = UUID.randomUUID.toString
    val careDir = new File(System.getProperty("user.home"), ".yapa/" + uuid)
    careDir.mkdirs

    // copy the CARE executable into temporary careDir
    val care = File.createTempFile("care_", "", careDir)
    // TODO choose CARE binary according to underlying OS ( System.getProperty("blabla") )
    getClass.getClassLoader.getResourceAsStream("care-x86_64").copy(care)
    care.setExecutable(true)

    val workingDir = "yapa-archive"

    // run CARE (line break mandatory to prevent ! to consume next line)
    Process(care + " -m -1 -o " + command.outputDir + "/" + workingDir + "/ " + command.launchingCommand) !

    // hack to allow copy of the archive in OpenMOLE (some path would be d--------- otherwise...)
    Process("/bin/chmod -R 777 " + command.outputDir + "/" + workingDir + "/" + "rootfs") !

    // remove ignored paths
    command.ignore.foreach {
      i =>
        IOTools(IOTools.find(i, command.outputDir + "/" + workingDir).headOption, {
          f: File => f.delete
        })
    }

    // add arbitrary requested path to archive
    command.additions.foreach {
      i =>
        val f = new File(i)
        val dest = new File(command.outputDir + "/" + workingDir + "/" + i)
        dest.getParent.mkdirs
        f.copy(dest)
    }

    val careExe = "re-execute.sh"
    val taskName = command.executable + "Task"

    // generate GUI task
    val proxies = new Proxies
    proxies += TaskDataProxyUI(new SystemExecTaskDataUI010(taskName, workingDir, careExe, List((new File(command.outputDir + "/" + workingDir), workingDir))))
    (new GUISerializer).serialize(command.outputDir + "/" + command.executable + ".om", proxies, Iterable(), saveFiles = command.embedded)

    // generate DSL task
    println("import org.openmole.plugin.task.systemexec.SystemExecTask\n" +
      "val " + taskName + " = SystemExecTask(" + List("\"" + taskName + "\"", "\"" + careExe + "\"", "\"" + workingDir + "\"").mkString(",") + ")\n" +
      taskName + " addResource \"" + command.outputDir + "/" + workingDir + "\"")

    // clean up temporary files
    care.delete
    careDir.delete

  } catch {
    case e: Throwable =>
      // TODO remove debug print
      e.printStackTrace()
      println("Invalid command")
  }
}
