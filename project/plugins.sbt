logLevel := Level.Warn


resolvers ++= Seq(
  "Artima Maven Repository" at "http://repo.artima.com/releases"
)

addSbtPlugin("com.artima.supersafe" % "sbtplugin" % "1.1.0")