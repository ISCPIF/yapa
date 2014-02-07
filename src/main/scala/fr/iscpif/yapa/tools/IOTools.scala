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
package fr.iscpif.yapa.tools

import scala.language.implicitConversions
import java.io.{FileInputStream, OutputStream, InputStream, File}
import java.net.URL
import java.nio.channels.Channel

object IOTools {
  implicit def stringToFile(s: String) = new File(s)

  implicit def fileToString(f: File) = f.getAbsolutePath

  implicit def toRichFile(file: File) = new RichFile(file)

  implicit def urlToFileChannel(u: URL) = fileToFileChannel(new File(u.toURI))

  implicit def fileToFileChannel(f: File) = {
    val channel = new FileInputStream(f).getChannel
  }

  def find(file: File, in: File) = list(in).filter {
    _.getName == file.getName
  }

  def recursiveFind(f: File, in: File): Iterable[File] = {
    val loc = find(f, in)
    loc ++ loc.filter(_.isDirectory).flatMap(dir => find(f, dir))
  }

  def apply[A](of: Option[File], action: File => A) = {
    of match {
      case Some(f: File) => action(f)
      case _ => throw new Throwable("The executable has not been found in the cde-package tree. The packaging failed")
    }
  }

  def list(in: File) = for (f <- new RichFile(in).andTree) yield f

  class RichFile(file: File) {

    def children = new Iterable[File] {
      def iterator = if (file.isDirectory) file.listFiles.iterator else Iterator.empty
    }

    def andTree: Iterable[File] =
      Seq(file) ++ children.flatMap(child => new RichFile(child).andTree)
  }

}
