package com.vektorraum.aviatorsbot.persistence.subscriptions.model

import java.util.Date

/**
  * Subscription of one subscriber, identified by their chatId, to one station, identified by its ICAO code.
  *
  * @param chatId Subscribers chatId
  * @param icao Station to which the subscriber subscribed
  * @param validUntil Expires after this date passed
  * @param metar Whether the subscriber subscribed to METARs
  * @param taf Whether the subscriber subscribed to TAFs
  * @param latestMetar Information about the latest METAR already successfully sent to the subscriber
  * @param latestTaf Information about the latest TAF already sent to the subscriber
  */
case class Subscription(chatId: Long, icao: String, validUntil: Date, metar: Boolean = true, taf: Boolean = true,
                        var latestMetar: Option[LatestInfo] = None, var latestTaf: Option[LatestInfo] = None)
