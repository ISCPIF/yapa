import AssemblyKeys._
import sbtassembly.Plugin._

name := "yapa"

scalaVersion := "2.10.2"

version := "0.1"

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Sonatype Release" at "https://oss.sonatype.org/content/repositories/releases",
  "openmole-snapshots" at "http://maven.openmole.org/public"
)

libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.10.2"

libraryDependencies += "org.openmole.ide" %% "org-openmole-ide-plugin-task-systemexec" % "0.10.0-SNAPSHOT"

assemblySettings

jarName in assembly := "yapa.jar"

excludedJars in assembly <<= (fullClasspath in assembly) map { cp =>
    cp filter {f =>
    val b = List("akka-actor_2.10-2.2.0.jar",
                 "akka-transactor_2.10-2.1.4.jar",
                 "com-thoughtworks-xstream_2.10-0.10.0-SNAPSHOT.jar",
                 "config-1.0.2.jar",
                 "db4o-full-java5-8.1-SNAPSHOT.jar",
                 "de-erichseifert-gral_2.10-0.10.0-SNAPSHOT.jar",
                 "gral-core-0.9-SNAPSHOT.jar",
                 "gridscale-1.56.jar",
                 "groovy-all-2.1.9.jar",
                 "org-openide-util-RELEASE73.jar",
                 "org-openide-windows-RELEASE73.jar",
                 "org-netbeans-bootstrap-RELEASE73.jar",
                 "org-netbeans-api-progress-RELEASE73.jar1",
                 "org-netbeans-core-startup-RELEASE73.jar",
                 "org-openide-explorer-RELEASE73.jar",
                 "org-openide-loaders-RELEASE73.jar",
                 "org-netbeans-api-annotations-common-RELEASE73.jar",
                 "org-scala-lang-scala-swing_2.10-0.10.0-SNAPSHOT.jar",
                 "org-scalaz_2.10-0.10.0-SNAPSHOT.jar",
                 "org-openide-text-RELEASE73.jar",
                 "org-openmole-web-misc-tools_2.10-0.10.0-SNAPSHOT.jar",
                // "org-openmole-ide-misc-tools_2.10-0.10.0-SNAPSHOT.jar",
                 "org-openmole-ide-plugin-misc-tools_2.10-0.10.0-SNAPSHOT.jar",
                 "kxml2-2.3.0.jar",
                 "miglayout-3.7.4.jar",
                 "jsyntaxpane_2.10-0.10.0-SNAPSHOT.jar",
                 "org-codehaus-groovy_2.10-0.10.0-SNAPSHOT.jar",
                 "jansi-1.4.jar",
                 "jline-2.10.3.jar",
                 "commons-beanutils-core-1.8.0.jar",
                 "commons-beanutils-1.7.0.jar",
                 "scala-reflect-2.10.3.jar",
                // "scala-swing-2.10.3.jar",
                 "scala-library.jar",
                 "xmlpull-1.1.3.1.jar"
                 //"xstream-1.4.4.jar"
                 ).contains(f.data.getName)
    println("XX : " + f.data.getName + " " +b)
    b
    }
 }