name := "AkkaHarvesterSb"

version := "1.0"

scalaVersion := "2.11.6"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)


libraryDependencies ++= Seq(
  "net.sigusr" %% "scala-mqtt-client" % "0.6.0"
)

