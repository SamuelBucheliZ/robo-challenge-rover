name := "AkkaHarvesterSb"

version := "1.0"

scalaVersion := "2.12.1"


libraryDependencies ++= Seq(
  "net.sigusr" %% "scala-mqtt-client" % "0.6.0",
  "com.typesafe.akka" %% "akka-actor" % "2.4.17"
)

