name := "aviatorsbot-scala"

version := "1.0"

scalaVersion := "2.12.2"

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"

libraryDependencies += "info.mukel" %% "telegrambot4s" % "2.2.8-SNAPSHOT"
libraryDependencies ++= Seq("org.slf4j" % "slf4j-api" % "1.7.5",
  "org.slf4j" % "slf4j-simple" % "1.7.5")
        