
import sbt._
import Keys._

object YapaBuild extends Build {

 override def settings = super.settings ++ Seq(scalaVersion := "2.10.1", organization := "fr.iscpif")

 val scalaSwing = "org.scala-lang" % "scala-swing" % "2.10.1"

 lazy val yapa = Project(id = "yapa", base = file("yapa"))

 lazy val core = Project(id = "core", base = file("core")) dependsOn(yapa)

 lazy val gui = Project(id = "gui", base = file("gui")) settings (libraryDependencies ++= Seq("org.scala-lang" % "scala-swing" % "2.10.1")) dependsOn(yapa)

 lazy val all = Project(id = "all", base = file(""))  settings (publish := { }) dependsOn(yapa, core, gui) aggregate(yapa, core, gui)
}



