package com.vektorraum.aviatorsbot.bot.util

import scala.util.matching.Regex


/**
  * Created by fvalka on 20.05.2017.
  */
object StationUtil {
  val ValidationPattern: Regex = "^(@|~)?([a-zA-Z0-9]){2,4}\\*?".r
  val ActualStationPattern: Regex = "^[a-zA-Z]{4}".r

  def isValidInput(input: String): Boolean = {
    ValidationPattern.pattern.matcher(input).matches()
  }

  def isICAOAptIdentifier(input: String): Boolean = {
    ActualStationPattern.pattern.matcher(input).matches()
  }

  def toStationList(input: String): List[String] = {
    if(!isValidInput(input)) {
      throw new IllegalArgumentException("input is invalid")
    }
    input.toUpperCase.split(" ").toList
  }

}
