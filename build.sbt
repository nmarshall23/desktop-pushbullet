name := "Desktop Push Bullet"

organization := "com.gmail.nmarshall23"

version := "0.0.1"

scalaVersion := "2.10.3"

resolvers += "swt-repo" at "https://swt-repo.googlecode.com/svn/repo/"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "1.9.2" % "test",
  "org.scala-lang" % "scala-swing" % "2.10.3",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.0",
  "org.streum" %% "configrity-core" % "1.0.0",
  "com.typesafe.akka" %% "akka-actor" % "2.2.1",
  "io.argonaut" %% "argonaut" % "6.0.1",
  "com.github.taksan" % "native-tray-adapter" % "1.2-SNAPSHOT"
)

initialCommands := "import desktoppushbullet._"

