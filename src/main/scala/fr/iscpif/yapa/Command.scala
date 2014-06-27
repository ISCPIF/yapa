/*
 * Copyright (C) 2011 <mathieu.Mathieu Leclaire at openmole.org>
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

import scala.annotation.tailrec

object Command {

  @tailrec
  def parse(args: List[String], c: Command = new Command): Command =
    args match {
      case "-o" :: tail ⇒ parse(tail.tail, c.copy(outputDir = tail.head))
      case "-c" :: tail ⇒ parse(tail.tail, c.copy(launchingCommand = tail.head))
      case "-e" :: tail ⇒ parse(tail.tail, c.copy(embedded = tail.head.toBoolean))
      case "-i" :: tail ⇒ parse(dropArgs(tail), c.copy(ignore = takeArgs(tail)))
      case "-a" :: tail ⇒ parse(dropArgs(tail), c.copy(additions = takeArgs(tail)))
      case "-h" :: tail =>
        help
        c
      case s :: tail ⇒ parse(tail, c.copy(unknown = s :: c.unknown))
      case _ ⇒
        c
    }

  def takeArgs(args: List[String]) = args.takeWhile(!_.startsWith("-"))
  def dropArgs(args: List[String]) = args.dropWhile(!_.startsWith("-"))

  def help = println("usage   : ./yapa -o <outputDirectory> -c <fullCommand>\n\n" +
    "options:\n" +
    "-o (compulsory): the output directory where the packaging archive and the OpenMOLE project are generated\n" +
    "-c (compulsory): the full command with parameters of you code. It must be enclosed in quotation marks\n" +
    "-e (optional)  : are the program resources embedded in the OpenMOLE project ? true / false. It is recommended to render your OpenMOLE workflow fully portable. The default value is true.\n" +
    "-i (optional)  : the list of resources that have to be ignored in the final archive\n" +
    "-a (optional)  : the list of resources to be arbitrarily added to the final archive\n\n" +
    "Example: ./yapa -o /tmp/out -c \"myCode -a 14 -b 7 -o /home/toto/file1.csv\" -i excludedPath1 excludedPath2 -a addedPath1 addedPath2\n\n" +
    "Visit: http://www.openmole.org/documentation/package-your-external-applications-with-yapa/"
  )
}

case class Command(val outputDir: String = "",
    val launchingCommand: String = "",
    val ignore: List[String] = List(),
    val additions: List[String] = List(),
    val embedded: Boolean = true,
    val unknown: List[String] = List()) {
  val executable = launchingCommand.split(" ").head
}
