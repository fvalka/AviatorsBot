package com.vektorraum.aviatorsbot.service.weather.fixtures

import com.vektorraum.aviatorsbot.persistence.airfielddata.model.{Airfield, Runway}

/**
  * Created by fvalka on 26.05.2017.
  */
object AirfieldFixtures {
  object RunwayFixtures {
    val NorthSouthRunway = Runway("18/36", List(180, 360))
    val NorthSouthRunwayOneEnded = Runway("36", List(180, 360))
    val NorthSoundRunwayWrong10deg = Runway("18/36", List(170, 350))
    val NorthSoundRunwayWrong15deg = Runway("18/36", List(195, 15))

    val LOWWRunway11_29 = Runway("11/29", List(116, 296))
    val LOWWRunway16_34 = Runway("16/34", List(164, 344))
  }
  val AirfieldNorthSouthRwy = Airfield("XXXX", "XXXX", 0.0, List(RunwayFixtures.NorthSouthRunway))
  val AirfieldNorthSouthRwy10degOff = Airfield("XXXX", "XXXX", 0.0, List(RunwayFixtures.NorthSoundRunwayWrong10deg))
  val AirfieldNorthSouthRwy15degOff = Airfield("XXXX", "XXXX", 0.0, List(RunwayFixtures.NorthSoundRunwayWrong15deg))
  val AirfieldOneEnded = Airfield("XXXX", "XXXX", 0.0, List(RunwayFixtures.NorthSouthRunwayOneEnded))
  val LOWW = Airfield("LOWW", "WIEN-SCHWECHAT", 4.1903814475597345, List(RunwayFixtures.LOWWRunway11_29,
    RunwayFixtures.LOWWRunway16_34))

}
