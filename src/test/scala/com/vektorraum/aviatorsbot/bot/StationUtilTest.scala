package com.vektorraum.aviatorsbot.bot

import com.vektorraum.aviatorsbot.bot.util.StationUtil
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{FunSuite, GivenWhenThen}
import org.scalatest.Matchers._

/**
  * Created by fvalka on 20.05.2017.
  */
class StationUtilTest extends FunSuite with GivenWhenThen with TableDrivenPropertyChecks {
  val validStations = Table("LOWW", "LO", "LO*", "LOW*", "@WY", "~AT", "KJFK", "KY50")
  val invalidStations = Table("L", "LOWÜ","LOWWW", "B LA", "ASDKSDKSAD(", "LSL%", "MNW$")

  test("validation should match valid station names") {
    forAll (validStations) { (station) =>
      StationUtil.isValidInput(station) shouldEqual true
    }
  }

  test("validation should not match invalid station names") {
    forAll (invalidStations) { (station) =>
      StationUtil.isValidInput(station) shouldEqual false
    }
  }
}
