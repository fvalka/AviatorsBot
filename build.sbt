import NativePackagerHelper._

name := "aviatorsbot-scala"

version := "1.0"

scalaVersion := "2.12.6"

resolvers += Resolver.sonatypeRepo("snapshots")
resolvers += Resolver.sonatypeRepo("public")

excludeDependencies += "ch.qos.logback" % "logback-classic"

// Akka
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.13"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.6.13"
libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.2.4"
libraryDependencies += "com.typesafe.akka" %% "akka-http-testkit" % "10.2.4"

// Scalatest
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

// Telegram Bot/
// Core with minimal dependencies, enough to spawn your first bot.
libraryDependencies += "com.bot4s" %% "telegram-core" % "5.0.0"

// Extra goodies: Webhooks, support for games, bindings for actors.
libraryDependencies += "com.bot4s" %% "telegram-akka" % "5.0.0"

// HTTP Client
libraryDependencies += "com.softwaremill.sttp" %% "core" % "1.3.5"
libraryDependencies += "com.softwaremill.sttp" %% "akka-http-backend" % "1.3.5"
libraryDependencies += "com.softwaremill.sttp" %% "json4s" % "1.3.5"

// XML
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.1.1"

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

// Enums
libraryDependencies += "com.beachape" %% "enumeratum" % "1.5.13"
libraryDependencies += "com.beachape" %% "enumeratum-reactivemongo-bson" % "1.5.13"

enablePlugins(JavaServerAppPackaging)
enablePlugins(DebianPlugin)
enablePlugins(SystemdPlugin)

Compile / unmanagedSourceDirectories += baseDirectory.value / "scalaxb-generated/main/scala"

lazy val scalaXml = "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
lazy val scalaParser = "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.6"
lazy val dispatchV = "0.12.0"
lazy val dispatch = "net.databinder.dispatch" %% "dispatch-core" % dispatchV

Compile / mainClass := Some("com.vektorraum.aviatorsbot.bot.AviatorsBotProduction")

Linux / maintainer := "Fabian Valka <contact@vektorraum.com>"
Linux / packageSummary  := "AviatorsBot"
packageDescription := "A telegram bot for aviators. Focused on providing up to date weather information."
Debian / serverLoading := Some(ServerLoader.Systemd)
startRunlevels  :=Option("3")
stopRunlevels :=Option("3")

Universal / mappings ++= directory("conf")

coverageExcludedPackages := "com\\.vektorraum\\.aviatorsbot\\.generated.*;scalaxb.*"
