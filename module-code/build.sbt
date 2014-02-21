name := "play-aauth"

organization := "ru.mirari"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
    "ru.mirari" %% "play-wished" % "1.0-SNAPSHOT",
    "ru.mirari" %% "play-mongo" % "1.0-SNAPSHOT",
    "ws.securesocial" %% "securesocial" % "2.1.3"
)

publishTo := {
  val artifactory = "http://mvn.quonb.org/artifactory/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("Artifactory Realm" at artifactory + "plugins-snapshot-local/")
  else
    Some("Artifactory Realm" at artifactory + "plugins-release-local/")
}

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

play.Project.playScalaSettings

resolvers ++= Seq(
  "quonb" at "http://mvn.quonb.org/repo/",
  "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
)

testOptions in Test += Tests.Argument("junitxml")
