import sbt._
import Keys._

object Plugins extends Build {
  lazy val root = Project("root", file("."))
  managedResources.map {rs =>
    rs.map {
      f => f-> "bin/"+f.getName}
  }
}

