name := "play-aauth"

organization := "play-infra"

version := "0.1"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
    "play-infra" %% "play-wished" % "0.1",
    "play-infra" %% "play-mongo" % "0.1",
    "ws.securesocial" %% "securesocial" % "2.1.3"
)

play.Project.playScalaSettings

resolvers += "quonb" at "http://repo.quonb.org/"

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Ywarn-dead-code",
  "-language:_",
  "-target:jvm-1.7",
  "-encoding", "UTF-8"
)

publishTo := Some(Resolver.file("file",  new File( "/mvn-repo" )) )

testOptions in Test += Tests.Argument("junitxml")

