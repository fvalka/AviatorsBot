import com.typesafe.sbt.SbtNativePackager.packageArchetype

name := "aviatorsbot-scala"

version := "1.0"

scalaVersion := "2.12.2"

resolvers += Resolver.sonatypeRepo("snapshots")
resolvers += Resolver.sonatypeRepo("public")

// Scalatest
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"

// Telegram Bot
libraryDependencies += "info.mukel" %% "telegrambot4s" % "2.2.8-SNAPSHOT"
libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.25"


// Wire Scala CDI
libraryDependencies += "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
libraryDependencies += "com.softwaremill.macwire" %% "macrosakka" % "2.3.0" % "provided"
libraryDependencies += "com.softwaremill.macwire" %% "util" % "2.3.0"
libraryDependencies += "com.softwaremill.macwire" %% "proxy" % "2.3.0"

// Scala Mock
libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % Test

// Mongodb
libraryDependencies += "org.reactivemongo" % "reactivemongo_2.12" % "0.12.3"// https://mvnrepository.com/artifact/org.scala-lang/scala-xml

// Caching
libraryDependencies += "com.github.cb372" %% "scalacache-caffeine" % "0.9.3"

enablePlugins(JavaServerAppPackaging)
enablePlugins(DebianPlugin)
enablePlugins(SystemdPlugin)

unmanagedSourceDirectories in Compile += baseDirectory.value / "scalaxb-generated/main/scala"

lazy val scalaXml = "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
lazy val scalaParser = "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.6"
lazy val dispatchV = "0.12.0"
lazy val dispatch = "net.databinder.dispatch" %% "dispatch-core" % dispatchV

mainClass in Compile := Some("com.vektorraum.aviatorsbot.bot.AviatorsBotProduction")

maintainer in Linux := "Fabian Valka <fvalka@vektorraum.com>"
packageSummary in Linux := "AviatorsBot"
packageDescription := "A telegram bot for aviators. Focused on providing up to date weather information."
serverLoading in Debian := Some(ServerLoader.Systemd)
startRunlevels  :=Option("3")
stopRunlevels :=Option("3")

mappings in Universal <+=
  (packageBin in Compile, baseDirectory) map { (_, base) =>
    val conf = base / "conf" / "aviatorsbot.conf"
    conf -> "conf/aviatorsbot.conf"
  }


