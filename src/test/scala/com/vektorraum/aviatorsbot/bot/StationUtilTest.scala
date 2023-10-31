package com.vektorraum.aviatorsbot.bot

import com.vektorraum.aviatorsbot.bot.util.StationUtil
import org.scalatest.matchers.should.Matchers._
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.GivenWhenThen
import org.scalatest.funsuite.AnyFunSuite

/**
  * Created by fvalka on 20.05.2017.
  */
class StationUtilTest extends AnyFunSuite with GivenWhenThen with TableDrivenPropertyChecks {
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
