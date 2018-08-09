package com.vektorraum.aviatorsbot.bot.util

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


  /**
    * Retrieve a help message based upon the command name.
    * The result will be cached for 1 hour
    *
    * @param command Name of the command without slashes
    * @return The stored static HTML file
    */
  def apply(command: String): String = memoizeSync(1 hour) {
    if(!safeFileNames.pattern.matcher(command).matches) {
      throw new IllegalArgumentException("Could not load help, unsafe filename")
    }
    load(s"help/$command.html")
  }

  private def load(resource: String): String = {
    Source.fromResource(resource).getLines().mkString("\n")
  }

}
