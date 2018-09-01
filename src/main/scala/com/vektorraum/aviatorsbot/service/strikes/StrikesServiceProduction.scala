package com.vektorraum.aviatorsbot.service.strikes

import java.time.Instant

import com.vektorraum.aviatorsbot.service.regions.Regions

class StrikesServiceProduction {
  private val urls = Map(
    Regions.Europe -> "http://images.blitzortung.org/Images/image_b_eu.png",
    Regions.Oceania -> "http://images.blitzortung.org/Images/image_b_oc.png",
    Regions.NorthAmerica -> "http://images.blitzortung.org/Images/image_b_us.png",
    Regions.SouthAmerica -> "http://images.blitzortung.org/Images/image_b_sa.png",
    Regions.Asia -> "http://images.blitzortung.org/Images/image_b_as.png",
    Regions.Africa -> "http://images.blitzortung.org/Images/image_b_af.png"
  )

  /**
    * Get the strike image url for a specific region
    *
    * The url includes a time parameter which changes every minute to reduce server load and
    * enable caching
    *
    * @param region Region for which to retrieve the strikes
    * @return Url including time parameter which can be send to Telegram
    */
  def getUrl(region: Regions): Option[String] = {
    val timeParam: Long = Instant.now().getEpochSecond / 60
    urls.get(region)
      .map(url => s"$url?t=$timeParam")
  }

}
