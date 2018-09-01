package com.vektorraum.aviatorsbot.persistence.subscriptions.model

import java.util.Date

/**
  * Information about the update already successfully sent to the subscriber
  *
  * @param issuedAt Issuance time of the message (METAR, TAF, etc.) already sent to the user
  * @param hash Mumur3Hash of the message sent to the subscriber
  */
case class LatestInfo(issuedAt: Date, hash: Int) {

}
