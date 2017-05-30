package com.vektorraum.aviatorsbot.bot.util

import java.time.{ZoneOffset, ZonedDateTime}
import java.time.format.DateTimeFormatter

/**
  * Created by fvalka on 30.05.2017.
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

}
