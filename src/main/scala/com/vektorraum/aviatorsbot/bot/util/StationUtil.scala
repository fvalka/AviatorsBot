package com.vektorraum.aviatorsbot.bot.util

import scala.util.matching.Regex


/**
  * Matcher for ICAO codes and valid ADDS weather service input
  */
object StationUtil {
  val ValidationPattern: Regex = "^(@|~)?([a-zA-Z0-9]){2,4}\\*?".r
  val ActualStationPattern: Regex = "^[a-zA-Z]{4}".r
  val WildcardPattern: Regex = "^[a-zA-Z]{0,4}\\*?".r

  def isValidInput(input: String): Boolean = {
    ValidationPattern.pattern.matcher(input).matches()
  }

  def isICAOAptIdentifier(input: String): Boolean = {
    ActualStationPattern.pattern.matcher(input).matches()
  }

  def isWildcardStation(input: String): Boolean = {
    WildcardPattern.pattern.matcher(input).matches()
  }

}
