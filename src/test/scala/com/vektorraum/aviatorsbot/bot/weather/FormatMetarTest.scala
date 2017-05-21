package com.vektorraum.aviatorsbot.bot.weather

import com.vektorraum.aviatorsbot.service.weather.fixtures.METARFixtures
import org.scalatest.Matchers._
import org.scalatest.{FunSuite, GivenWhenThen}

/**
  * Created by fvalka on 20.05.2017.
  */
class FormatMetarTest extends FunSuite with GivenWhenThen {

  test("FormatMetar should return the raw string with markup and emoji when neither diff nor sparklines are set") {
    Given("Valid METAR with VFR flight category")
    val metar = METARFixtures.ValidAndCompleteLOWW
    When("Formatting without sparklines")
    val result = FormatMetar(List(metar))
    Then("Result contains METAR and MarkUp")
    result shouldEqual "<strong>LOWW</strong> ✅ 201920Z 32019KT 9999 FEW032 BKN036 16/09 Q1019 NOSIG"
  }

}
