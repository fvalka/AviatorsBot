package com.vektorraum.aviatorsbot.bot.util

import com.typesafe.scalalogging.Logger
import scalacache._
import scalacache.caffeine._
import scalacache.memoization._

import scala.concurrent.duration._
import scala.io.Source
import scala.language.postfixOps

/**
  * Loads help messages from local resource files
  *
  * Created by fvalka on 07.06.2017.
  */
object HelpMessages {
  implicit val scalaCache: ScalaCache[NoSerialization] = ScalaCache(CaffeineCache())

  private val safeFileNames = "[a-zA-Z0-9]*".r

  val logger = Logger(getClass)


  /**
    * Retrieve a help message based upon the command name.
    * The result will be cached for 1 hour
    *
    * @param command Name of the command without slashes
    * @return The stored static HTML file
    */
  def apply(command: String): String = memoizeSync(1 hour) {
    logger.debug(s"Loading help file for command=$command")
    if(!safeFileNames.pattern.matcher(command).matches) {
      throw new IllegalArgumentException("Could not load help, unsafe filename")
    }
    load(s"help/$command.html")
  }

  private def load(resource: String): String = {
    try {
      val file = Source.fromResource(resource)
      file.getLines().mkString("\n")
    } catch {
      case npe: NullPointerException => logger.error("Help file not found or empty", npe)
        "Error while loading help file"
    }
  }

}
