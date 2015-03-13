import sbt._
import play.Project._

object ApplicationBuild extends Build {

  def fromEnv(name: String) = System.getenv(name) match {
    case null => None
    case value => Some(value)
  }

  val appName = fromEnv("project.artifactId").getOrElse("guppy-play")
  val appVersion = fromEnv("project.version").getOrElse("DEV")

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean
//    "mysql" % "mysql-connector-java" % "5.1.18"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
//    resolvers += "google-api-services" at "http://mavenrepo.google-api-java-client.googlecode.com/hg",
  )

}
