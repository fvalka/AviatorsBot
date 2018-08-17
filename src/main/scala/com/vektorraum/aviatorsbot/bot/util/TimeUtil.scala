package com.vektorraum.aviatorsbot.bot.util

import java.text.ParseException
import java.time.{LocalTime, ZoneOffset, ZonedDateTime}
import java.time.format.DateTimeFormatter

import scala.util.matching.Regex

/**
  * Helps checking and parsing times and durations
  */
object TimeUtil {
  val timePattern: Regex = "^(2[0-3]|[01]?[0-9]):?([0-5][0-9])Z?$".r
  val durationPattern: Regex = "^\\d{1,2}$".r

  private val timeFormatter = DateTimeFormatter.ofPattern("HHmm")

  /**
    * Is it a valid time input, valid are for example: 01:20, 120, 1:20, 1:20Z, etc.
    *
    * @param input String to check
    * @return Valid time string check
    */
  def isTime(input: String): Boolean = {
    timePattern.pattern.matcher(input).matches()
  }

  /**
    * Is it a valid duration, matches any positive number from 0 to 99
    *
    * @param input String to check
    * @return Valid duration string check
    */
  def isDuration(input: String): Boolean = {
    durationPattern.pattern.matcher(input).matches()
  }

  /**
    * Is it a valid time or duration
    *
    * @param input String to check
    * @return Valid duration or time check
    */
  def isTimeOrDuration(input: String): Boolean = {
    isTime(input) || isDuration(input)
  }

  /**
    * Parses a string to a ZonedDateTime from either a time or duration.
    *
    * It has the current day or the next day if the result would lie in the past.
    *
    * @param input String to parse
    * @return ZonedDateTime with time from the input or duration from the input
    */
  def parseDurationOrTime(input: String): ZonedDateTime = {
    val zonedNow = ZonedDateTime.now(ZoneOffset.UTC)

    val date = if(isTime(input)) {
      val processedInput = input
        .replace(":", "")
        .replace("Z","")

      val time = LocalTime.parse(processedInput, timeFormatter)

      zonedNow.`with`(time)
    } else if(isDuration(input)) {
      zonedNow.plusHours(input.toInt)
    } else {
      throw new ParseException("Input is neither a time nor a duration", -1)
    }

    if(date.isBefore(zonedNow)) {
      date.plusDays(1)
    } else {
      date
    }
  }

}
