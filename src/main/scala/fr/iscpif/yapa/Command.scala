/*
 * Copyright (C) 2011 <mathieu.Mathieu Leclaire at openmole.org>
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

import scala.annotation.tailrec
import java.io.File

object Command {

  @tailrec def parse(args: List[String], c: Command = new Command): Command =
      args match {
        case "-o" :: tail ⇒ parse(tail.tail, c.copy(outputDir = tail.head))
        case "-c" :: tail ⇒ parse(tail.tail, c.copy(launchingCommand = tail.head))
        case "-e" :: tail ⇒ parse(tail.tail, c.copy(embedd = tail.head.toBoolean))
        case "-i" :: tail  ⇒ parse(dropArgs(tail), c.copy(ignore = takeArgs(tail)))
        case s :: tail     ⇒ parse(tail, c.copy(unknown = s :: c.unknown))
        case Nil           ⇒ c
      }

  def takeArgs(args: List[String]) = args.takeWhile(!_.startsWith("-"))
  def dropArgs(args: List[String]) = args.dropWhile(!_.startsWith("-"))
}

case class Command(val outputDir: String = "",
                   val launchingCommand: String = "",
                   val ignore: List[String] = List(),
                   val embedd: Boolean = true,
                   val unknown: List[String]= List()){
  val executable = launchingCommand.split(" ").head

  val cdeLaunchinCommand = launchingCommand.replace(executable, executable + ".cde")
}