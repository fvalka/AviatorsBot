package com.vektorraum.aviatorsbot.service.strikes
import com.vektorraum.aviatorsbot.service.regions.Regions

trait StrikesService {

  /**
    * Get the strike image url for a specific region
    *
    * The url includes a time parameter which changes every minute to reduce server load and
    * enable caching
    *
    * @param region Region for which to retrieve the strikes
    * @return Url including time parameter which can be send to Telegram
    */
  def getUrl(region: Regions): Option[String]
}
