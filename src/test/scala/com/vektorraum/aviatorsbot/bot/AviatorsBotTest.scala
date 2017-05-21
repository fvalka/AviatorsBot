package com.vektorraum.aviatorsbot.bot

import org.scalatest.{FeatureSpec, GivenWhenThen}

/**
  * Created by fvalka on 21.05.2017.
  */
class AviatorsBotTest extends FeatureSpec with GivenWhenThen {
  info("As a pilot I want to")
  info("Be able to retrieve up to date METAR and TAF weather information")
  info("Enhanced with mark up for faster recognition of the current flight conditions and trend")

  feature("Fetch current METAR and TAF information") {
    scenario("Pilot requests weather for a valid station which has METARs available") {

    }
  }

}
