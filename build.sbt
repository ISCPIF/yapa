name := "yapa"

scalaVersion := "2.10.2"

version := "0.1"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Sonatype Release" at "https://oss.sonatype.org/content/repositories/releases"

resolvers += "openmole-public" at "http://maven.openmole.org/public"

libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.10.2"

libraryDependencies += "org.openmole.core" %% "org.openmole.ide.plugin.task.systemexec" % "0.10.0-SNAPSHOT  "
