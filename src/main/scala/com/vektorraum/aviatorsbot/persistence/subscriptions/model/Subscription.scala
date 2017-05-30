package com.vektorraum.aviatorsbot.persistence.subscriptions.model

import java.util.Date

/**
  *            _    _
  *           | |  | |
  * __   _____| | _| |_ ___  _ __ _ __ __ _ _   _ _ __ ___  
  * \ \ / / _ \ |/ / __/ _ \| '__| '__/ _` | | | | '_ ` _ \ 
  *  \ V /  __/   <| || (_) | |  | | | (_| | |_| | | | | | |
  *   \_/ \___|_|\_\\__\___/|_|  |_|  \__,_|\__,_|_| |_| |_|
  *
  * vektorraum.com
  *
  * Created by fvalka on 27.05.2017.
  */
case class Subscription(chatId: Long, icao: String, validUntil: Date, metar: Boolean = true, taf: Boolean = true,
                        latestMetar: Option[LatestInfo], latestTaf: Option[LatestInfo]) {

}