
import sbt._
import Keys._

object YapaBuild extends Build {

 override def settings = super.settings ++ Seq(scalaVersion := "2.10.1", organization := "fr.iscpif")

 lazy val yapa = Project(id = "yapa", base = file("yapa"))

 lazy val core = Project(id = "core", base = file("core")) settings (libraryDependencies ++= Seq("net.schmizz" % "sshj" % "0.8.1")) dependsOn(yapa)

 lazy val gui = Project(id = "gui", base = file("gui")) settings (libraryDependencies ++= Seq("org.scala-lang" % "scala-swing" % "2.10.1")) dependsOn(yapa, core, uri("git://github.com/roguePanda/java-terminal.git#88eb0e64670adcb112af14832cdc57721b94af14"))

 lazy val all = Project(id = "all", base = file(""))  settings (publish := { }) dependsOn(yapa, core, gui) aggregate(yapa, core, gui)
}



