import NativePackagerHelper._

name := "aviatorsbot-scala"

version := "1.0"

scalaVersion := "2.12.6"

resolvers += Resolver.sonatypeRepo("snapshots")
resolvers += Resolver.sonatypeRepo("public")

// Akka
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.14"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.14"
libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.3"
libraryDependencies += "com.typesafe.akka" %% "akka-http-testkit" % "10.1.3"

// Scalatest
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

// Telegram Bot
libraryDependencies += "info.mukel" %% "telegrambot4s" % "3.0.15"

// Scheduler
libraryDependencies += "org.quartz-scheduler" % "quartz" % "2.3.0"

// Logging
libraryDependencies += "org.apache.logging.log4j" % "log4j-api" % "2.11.1"
libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % "2.11.1"
libraryDependencies += "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.11.1"

// Wire Scala CDI
libraryDependencies += "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
libraryDependencies += "com.softwaremill.macwire" %% "macrosakka" % "2.3.0" % "provided"
libraryDependencies += "com.softwaremill.macwire" %% "util" % "2.3.0"
libraryDependencies += "com.softwaremill.macwire" %% "proxy" % "2.3.0"

// Scala Mock
libraryDependencies += "org.scalamock" %% "scalamock" % "4.1.0" % Test

// Mongodb
libraryDependencies += "org.reactivemongo" % "reactivemongo_2.12" % "0.16.0"// https://mvnrepository.com/artifact/org.scala-lang/scala-xml

// Caching
libraryDependencies += "com.github.cb372" %% "scalacache-caffeine" % "0.9.3"

// Metrics
libraryDependencies += "nl.grons" %% "metrics4-scala" % "4.0.1"
libraryDependencies += "nl.grons" %% "metrics4-akka_a24" % "4.0.1"
libraryDependencies += "nl.grons" %% "metrics4-scala-hdr" % "4.0.1"
libraryDependencies += "io.dropwizard.metrics" % "metrics-log4j2" % "4.0.1"
libraryDependencies += "io.dropwizard.metrics" % "metrics-graphite" % "4.0.1"

// Units of Measure
libraryDependencies += "org.typelevel"  %% "squants"  % "1.3.0"

enablePlugins(JavaServerAppPackaging)
enablePlugins(DebianPlugin)
enablePlugins(SystemdPlugin)

unmanagedSourceDirectories in Compile += baseDirectory.value / "scalaxb-generated/main/scala"

lazy val scalaXml = "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
lazy val scalaParser = "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.6"
lazy val dispatchV = "0.12.0"
lazy val dispatch = "net.databinder.dispatch" %% "dispatch-core" % dispatchV

mainClass in Compile := Some("com.vektorraum.aviatorsbot.bot.AviatorsBotProduction")

maintainer in Linux := "Fabian Valka <contact@vektorraum.com>"
packageSummary in Linux := "AviatorsBot"
packageDescription := "A telegram bot for aviators. Focused on providing up to date weather information."
serverLoading in Debian := Some(ServerLoader.Systemd)
startRunlevels  :=Option("3")
stopRunlevels :=Option("3")

mappings in Universal ++= directory("conf")

coverageExcludedPackages := "com\\.vektorraum\\.aviatorsbot\\.generated.*;scalaxb.*"
