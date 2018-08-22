package com.vektorraum.aviatorsbot.bot.util

import java.time.format.DateTimeFormatter
import java.time.{ZoneOffset, ZonedDateTime}

/**
  * Formats ZonedDateTimes with as much brevity as possible.
  *
  * If the ZonedDateTime is on the same day only hour and minutes will be returned.
  * Otherwise the whole date with time is returned.
  */
object TimeFormatter {
  private val hourFormatter = DateTimeFormatter.ofPattern("HHmmX")
  private val dayFormatter = DateTimeFormatter.ofPattern("yyyy-M-d ")

  /**
    * Formats the ZonedDateTime to {hour}{minute}Z if it is on current day and
    * to {year}-{month}-{day} {hour}{minute}Z else
    *
    * @param dateTime DateTime to be formatted
    * @return Hour and minute and date if the day is different from the current day
    */
  def shortUTCDateTimeFormat(dateTime: ZonedDateTime): String = {
    val now = ZonedDateTime.now(ZoneOffset.UTC)
    if(now.getYear == dateTime.getYear && now.getDayOfYear == dateTime.getDayOfYear) {
      hourFormatter.format(dateTime)
    } else {
      dayFormatter.format(dateTime) + hourFormatter.format(dateTime)
    }
  }

  /**
    * Formats the ZonedDateTime to {hour}{minute}Z if it is on current day and
    * to {year}-{month}-{day} {hour}{minute}Z else
    *
    * @param dateString DateTime as ISO String
    * @return Hour and minute and date if the day is different from the current day
    */
  def shortUTCDateTimeFormat(dateString: String): String = {
    shortUTCDateTimeFormat(ZonedDateTime.parse(dateString))
  }

}
