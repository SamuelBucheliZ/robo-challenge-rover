name := "AkkaHarvesterSb"

version := "1.0"

scalaVersion := "2.11.6"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"),
  "Artima Maven Repository" at "http://repo.artima.com/releases"
)


libraryDependencies ++= Seq(
  "net.sigusr" %% "scala-mqtt-client" % "0.6.0",
  "io.argonaut" %% "argonaut" % "6.1",
  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

