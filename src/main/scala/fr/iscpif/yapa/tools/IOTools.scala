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
import java.io.{OutputStream, InputStream, File}

object IOTools {
  implicit def stringToFile(s: String) = new File(s)

  implicit def toRichFile(file: File) = new RichFile(file)


  def copy(is: InputStream, to: OutputStream): Unit = {
    val buffer = new Array[Byte](8 * 1024)
    Iterator.continually(is.read(buffer)).takeWhile(_ != -1).foreach {
      to.write(buffer, 0, _)
    }
    is.close
    to.close
  }

  def find(file: File, in: File) = list(in).filter {
    _.getName == file.getName
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