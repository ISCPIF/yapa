import sbt._

object Plugins extends Build {
  lazy val root = Project("root", file(".")) dependsOn(
   uri("https://github.com/HouzuoGuo/schale.git#32630288beb95e1c732cdf8e7087b36e7c453d0c")
  )
}
