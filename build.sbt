import AssemblyKeys._
import sbtassembly.Plugin._
import com.typesafe.sbt.SbtNativePackager.Universal
import NativePackagerKeys._

name := "yapa"

scalaVersion := "2.10.4"

version := "0.3-SNAPSHOT"

packageArchetype.java_application

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Sonatype Release" at "https://oss.sonatype.org/content/repositories/releases",
  "openmole-snapshots" at "http://maven.openmole.org/public"
)

libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.10.4"

libraryDependencies += "org.openmole.ide" %% "org-openmole-ide-plugin-task-systemexec" % "0.10.0-SNAPSHOT"

assemblySettings

scalariformSettings

jarName in assembly := "yapa.jar"

//FIXME: refactor openmole so that the dependencies are well separated
excludedJars in assembly <<= (fullClasspath in assembly) map { cp =>
    cp filter {f => List("akka-actor_2.10-2.2.0.jar",
                 "akka-transactor_2.10-2.1.4.jar",
                 "com-google-guava_2.10-0.10.0-SNAPSHOT.jar",
                 "com-thoughtworks-xstream_2.10-0.10.0-SNAPSHOT.jar",
                 "com-db4o_2.10-0.10.0-SNAPSHOT.jar",
                 "commons-digester-1.8.jar",
                 "commons-collections-3.2.1.jar",
                 "commons-lang-2.4.jar",
                 "commons-exec-1.1.jar",
                 "commons-math3-3.0.jar",
                 "config-1.0.2.jar",
                 "cloning-1.7.4.jar",
                 "db4o-full-java5-8.1-SNAPSHOT.jar",
                 "de-erichseifert-gral_2.10-0.10.0-SNAPSHOT.jar",
                 "gral-core-0.9-SNAPSHOT.jar",
                 "gridscale-1.56.jar",
                 "groovy-all-2.1.9.jar",
                 "jsyntaxpane-0.9.6.jar",
                 "objenesis-1.2.jar",
                 "log4j-1.2.17.jar",
                 "org.eclipse.osgi-3.8.2.v20130124-134944.jar",
                 "org-scalaj-scalaj-http_2.10-0.10.0-SNAPSHOT.jar",
                 "org-openide-awt-RELEASE73.jar",
                 "org-openide-nodes-RELEASE73.jar",
                 "org-openide-util-RELEASE73.jar",
                 "org-openide-windows-RELEASE73.jar",
                 "org-netbeans-bootstrap-RELEASE73.jar",
                 "org-netbeans-api-progress-RELEASE73.jar1",
                 "org-netbeans-core-startup-RELEASE73.jar",
                 "org-openide-explorer-RELEASE73.jar",
                 "org-openide-loaders-RELEASE73.jar",
                 "org-netbeans-api-annotations-common-RELEASE73.jar",
                 "org-netbeans-api-visual-RELEASE73.jar",
                 "org-openide-filesystems-RELEASE73.jar",
                 "org-netbeans-modules-editor-mimelookup-RELEASE73.jar",
                 "org-openide-modules-RELEASE73.jar",
                 "org-netbeans-modules-queries-RELEASE73.jar",
                 "org-scala-lang-scala-swing_2.10-0.10.0-SNAPSHOT.jar",
                 "org-openide-util-lookup-RELEASE73.jar",
                 "org-openide-dialogs-RELEASE73.jar",
                 "org-openide-actions-RELEASE73.jar",
                 "org-netbeans-swing-tabcontrol-RELEASE73.jar",
                 "org-netbeans-modules-settings-RELEASE73.jar",
                 "org-netbeans-api-progress-RELEASE73.jar",
                 "org-apache-commons-configuration_2.10-0.10.0-SNAPSHOT.jar",
                 "org-apache-log4j_2.10-0.10.0-SNAPSHOT.jar",
                 "org-openmole-misc-osgi_2.10-0.10.0-SNAPSHOT.jar",
                 "org-openmole-core-batch_2.10-0.10.0-SNAPSHOT.jar",
                 "commons-logging-1.1.1.jar",
                 "org-scalaz_2.10-0.10.0-SNAPSHOT.jar",
                 "org-openide-text-RELEASE73.jar",
                 "org-openmole-web-misc-tools_2.10-0.10.0-SNAPSHOT.jar",
                 "org-openmole-ide-misc-widget_2.10-0.10.0-SNAPSHOT.jar",
                 "org-openmole-ide-misc-visualization_2.10-0.10.0-SNAPSHOT.jar",
                 "org-openmole-misc-pluginmanager_2.10-0.10.0-SNAPSHOT.jar",
                 "fr.iscpif.gridscale-1.56.jar",
                 "scala-stm_2.10-0.7.jar",
                 "scalaj-http_2.10-0.3.10.jar",
                 "org-joda-time_2.10-0.10.0-SNAPSHOT.jar",
                 "org-openmole-ide-plugin-misc-tools_2.10-0.10.0-SNAPSHOT.jar",
                 "org-openmole-misc-updater_2.10-0.10.0-SNAPSHOT.jar",
                 "org-netbeans-api_2.10-0.10.0-SNAPSHOT.jar",
                 "uk-com-robustit-cloning_2.10-0.10.0-SNAPSHOT.jar",
                 "kxml2-2.3.0.jar",
                 "miglayout-3.7.4.jar",
                 "jsyntaxpane_2.10-0.10.0-SNAPSHOT.jar",
                 "org-codehaus-groovy_2.10-0.10.0-SNAPSHOT.jar",
                 "net-miginfocom-swing-miglayout_2.10-0.10.0-SNAPSHOT.jar",
                 "jansi-1.4.jar",
                 "jline-2.10.3.jar",
                 "jasypt-1.8.jar",
                 "commons-beanutils-core-1.8.0.jar",
                 "commons-beanutils-1.7.0.jar",
//                 "scala-reflect-2.10.3.jar",
                 "scala-library.jar",
                 "scala-io-core_2.10-0.4.2.jar",
                 "scala-io-file_2.10-0.4.2.jar",
                 "jsr305-1.3.9.jar",
                 "xmlpull-1.1.3.1.jar",
                 "guava-14.0.1.jar",
                 "joda-time-1.6.jar",
                 "scala-arm_2.10-1.3.jar",
                 "scala-actors-2.10.3.jar",
                 "org-netbeans-swing-outline-RELEASE73.jar"
                 ).contains(f.data.getName)
    }
 }

 resourceGenerators in Compile <+= (target in Compile, scalaBinaryVersion) map { (dir, version) =>
  val yapadir = dir / "scala-" + version + "/yapa"
  val file = new File(yapadir)
  IO.write(file, "#!/bin/sh\njava -Xmx256M -jar ../lib/yapa.jar \"$@\"")
  Seq(file)
}

mappings in Universal := Nil

mappings in Universal <++= (managedResources in Compile, assembly).map { (rs,as) => {
   Seq(as -> "lib/yapa.jar") ++
   rs.map{f=> (f, "bin/"+f.getName)}
   }
}
