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

import fr.iscpif.yapa.tools.IOTools._
import fr.iscpif.yapa.tools.IOTools

import java.io.File
import java.nio.file.{ StandardCopyOption, Paths, Files }
import sys.process._

import org.openmole.tool.file._

object Yapa extends App {

  try {
    val command = Command.parse(args.toList)

    // check compulsory parameters
    command match {
      case Command("", _, _, _, _, _, _) =>
        Command.help
        throw new RuntimeException("Unspecified output directory (-o)")
      case Command(_, _, "", _, _, _, _) =>
        Command.help
        throw new RuntimeException("Unspecified launching command (-c)")
      case _ =>
    }

    // build an new sandboxed working folder
    val yapaUserDir = Files.createDirectories(Paths.get(System.getProperty("user.home"), ".yapa/"))
    val careDir = Files.createTempDirectory(yapaUserDir, "")

    // copy the CARE executable into temporary careDir
    val care = Files.createTempFile(careDir, "care_", "")
    // TODO choose CARE binary according to underlying OS ( System.getProperty("blabla") )
    Files.copy(getClass.getClassLoader.getResourceAsStream("care-x86_64"), care, StandardCopyOption.REPLACE_EXISTING)
    care.toFile.setExecutable(true)

    // run CARE (line break mandatory to prevent ! to consume next line)
    // default options extended with:
    //   - unlimited archive size
    //   - don't archive /run
    //   - don't archive /var/run
    Process(s"${care} -m -1 -p /run -p /var/run -o ${command.outputDir}/${command.workingDir}/ ${command.launchingCommand}") !

    // hack to allow copy of the archive in OpenMOLE (some path would be d--------- otherwise...)
    Process(s"/bin/chmod -R 777 ${command.outputDir}/${command.workingDir}/rootfs") !

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

    // generate DSL task
    println(s"""
      val ${taskName} = SystemExecTask("./${command.workingDir}/${careExe}") set (
        resources += "${command.outputDir}/${command.workingDir}"
      )
      """)

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
