package com.vektorraum.aviatorsbot.bot.weather

import com.vektorraum.aviatorsbot.service.weather.fixtures.TAFFixtures
import org.scalatest.{FunSuite, GivenWhenThen}
import org.scalatest.Matchers._

/**
  * Created by fvalka on 25.05.2017.
  */
class FormatTafTest extends FunSuite with GivenWhenThen {
  test("FormatTafTest returns a correctly formatted string") {
    Given("Valid TAF Input")
    val taf = TAFFixtures.ValidAndCompleteLOWW

    When("Applying the formatter")
    val result = FormatTaf(List(taf))

    Then("A correctly formatted string is returned")
    result shouldEqual "<strong>TAF LOWW</strong> 231715Z 2318/2424 18004KT CAVOK TX22/2318Z TN12/2500Z FM240300 29015G25KT 9999 BKN040 TEMPO 2403/2408 30018G30KT 6000 SHRA FEW030 FEW030CB BKN040 PROB30 2404/2407 4000 TSRA FM241000 32015G25KT CAVOK TEMPO 2410/2416 33022G32KT BECMG 2416/2418 34012KT"
  }

  test("FormatTafTest returns an empty string for an empty list") {
    FormatTaf(List()) shouldEqual ""
  }

}
