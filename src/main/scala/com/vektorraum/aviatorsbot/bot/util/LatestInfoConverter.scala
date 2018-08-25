package com.vektorraum.aviatorsbot.bot.util

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

import com.vektorraum.aviatorsbot.generated.metar.METAR
import com.vektorraum.aviatorsbot.generated.taf.TAF
import com.vektorraum.aviatorsbot.persistence.subscriptions.model.LatestInfo

import scala.language.implicitConversions
import scala.util.hashing.MurmurHash3

object LatestInfoConverter {
  /**
    * Converts a METAR to a LatestInfo using the MurmurHash3 hash function
    *
    * The METAR must contain an observation time or an exception will be thrown
    *
    * @param metar METAR to be used for the creation of the LatestInfo
    * @return LatestInfo with METAR observation time and raw text hash
    */
  def fromMetar(metar: METAR): LatestInfo = {
    from(metar.observation_time.get, metar.raw_text.getOrElse(""))
  }

  /**
    * Converts a TAF to a LatestInfo using the MurmurHash3 hash function
    *
    * The TAF must contain an issue time or an exception will be thrown.
    *
    * @param taf TAF to be used for the creation of the latest info
    * @return LatestInfo with TAFs issue_time and raw text hash
    */
  def fromTaf(taf: TAF): LatestInfo = {
    from(taf.issue_time.get, taf.raw_text.getOrElse(""))
  }

  /**
    * Creates a LatestInfo using the MurmurHash3
    *
    * @param date Will be used for issuedAt
    * @param hashSource Will be hashed using the MurmurHash3
    * @return LatestInfo with this issuedAt date and hashed hashSource
    */
  protected def from(date: ZonedDateTime, hashSource: String): LatestInfo = {
    LatestInfo(Date.from(date.toInstant), MurmurHash3.stringHash(hashSource))
  }

  /**
    * Implicit conversion of an ISO zoned date time String to a ZonedDateTime object
    *
    * @param in ISO zoned date time converted string
    * @return Corresponding ZonedDateTime object
    */
  protected implicit def parseDate(in: String): ZonedDateTime =
    ZonedDateTime.parse(in, DateTimeFormatter.ISO_ZONED_DATE_TIME)

}
