package com.vektorraum.aviatorsbot.persistence.sigmets.model

import java.time.{ZoneOffset, ZonedDateTime}
import java.util.Date

case class SigmetInfo(chatId: Long, index: Int, info: String, validUntil: Date = SigmetInfo.defaultValidity())

object SigmetInfo {
  def defaultValidity(): Date = {
    val dateZDT = ZonedDateTime.now(ZoneOffset.UTC).plusHours(2)
    Date.from(dateZDT.toInstant)
  }
}