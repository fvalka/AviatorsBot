package com.vektorraum.aviatorsbot.bot.calculators

import com.vektorraum.aviatorsbot.bot.AviatorsBotForTesting
import com.vektorraum.aviatorsbot.service.weather.fixtures.{AirfieldFixtures, METARFixtures}
import org.scalamock.scalatest.{AsyncMockFactory, MockFactory}
import org.scalatest.Matchers._
import org.scalatest.{AsyncFunSuite, GivenWhenThen}

import scala.concurrent.Future

class DensityAltitudeCalculatorTest extends AsyncFunSuite with GivenWhenThen with AsyncMockFactory {
  test("Density Altitude for valid METAR") {
    Given("METAR containing normal wind, with no variation or gusts")
    val metar = METARFixtures.ValidAndCompleteLOWW
    val bot = new AviatorsBotForTesting()

    When("Density altitude is calculated")
    val result = new DensityAltitudeCalculator(bot.airfieldDAO, bot.weatherService)(metar)

    Then("result matches precalculated values")
    result map {
      _ shouldEqual "METAR observation time: 2017-5-20 1920Z\n" +
        "Elevation: 623 ft\n" +
        "Density altitude: <strong>865 ft</strong>"
    }
  }

  test("Missing values result in error message") {
    Given("METARs with missing values")
    val bot = new AviatorsBotForTesting()
    val calculator = new DensityAltitudeCalculator(bot.airfieldDAO, bot.weatherService)

    val invalid_metars = List(METARFixtures.ValidAndCompleteLOWW.copy(temp_c = None),
      METARFixtures.ValidAndCompleteLOWW.copy(dewpoint_c = None),
      METARFixtures.ValidAndCompleteLOWW.copy(elevation_m = None))

    val futures = invalid_metars map { metar =>
      calculator(metar)
    }

    Future.sequence(futures).map { list =>
      list.forall(_ == "Missing values in METAR, calculation can not be performed.") shouldEqual true
    }
  }

  test("Missing observation time returns valid message with information about the problem") {
    Given("METAR containing normal wind, with no variation or gusts")
    val metar = METARFixtures.ValidAndCompleteLOWW.copy(observation_time = None)
    val bot = new AviatorsBotForTesting()
    val calculator = new DensityAltitudeCalculator(bot.airfieldDAO, bot.weatherService)

    When("Density altitude is calculated")
    val result = calculator(metar)

    Then("result matches precalculated values")
    result map {
      _ shouldEqual
        "METAR observation time: Unknown\n" +
          "Elevation: 623 ft\n" +
          "Density altitude: <strong>865 ft</strong>"
    }
  }

  test("Nearby METAR is used if the METAR doesn't contain an altimeter setting") {
    Given("METAR without altimeter setting and mocks of weather service and airfield dao")
    val metar = METARFixtures.ValidAndCompleteLOWG.copy(altim_in_hg = None)
    val metar_nearby = METARFixtures.ValidAndCompleteLOWW
    val bot = new AviatorsBotForTesting()

    bot.airfieldDAO.near _ expects("LOWG", *) returning Future.successful(Seq(AirfieldFixtures.LOWW))
    bot.weatherService.getMetars _ expects Seq("LOWW") returning Future.successful(Map("LOWW" -> Seq(metar_nearby)))

    When("Calculating the density altitude for a station with missing altimeter setting")
    val calculator = new DensityAltitudeCalculator(bot.airfieldDAO, bot.weatherService)
    val result = calculator(metar)

    Then("Result should be a valid calculation with warning")
    result map { res =>
      res should include ("The METAR didn't contain an altimeter setting!")
      res should include ("The altimeter setting of LOWW was used: 1019 hPa")
      res should include ("Density altitude: <strong>1432 ft</strong>")
    }
  }

  test("Nearby METAR is used but the weather service result is empty") {
    Given("METAR without altimeter setting and mocks of weather service and airfield dao")
    val metar = METARFixtures.ValidAndCompleteLOWG.copy(altim_in_hg = None)
    val bot = new AviatorsBotForTesting()

    bot.airfieldDAO.near _ expects("LOWG", *) returning Future.successful(Seq(AirfieldFixtures.LOWW))
    bot.weatherService.getMetars _ expects Seq("LOWW") returning Future.successful(Map.empty)

    When("Calculating the density altitude for a station with missing altimeter setting")
    val calculator = new DensityAltitudeCalculator(bot.airfieldDAO, bot.weatherService)
    val result = calculator(metar)

    Then("An error message should be returned ")
    result map { res =>
      res shouldEqual "Missing values in METAR, calculation can not be performed."
    }
  }

  test("Nearby METAR is used but the other station also contains no altimeter setting") {
    Given("METAR without altimeter setting and mocks of weather service and airfield dao")
    val metar = METARFixtures.ValidAndCompleteLOWG.copy(altim_in_hg = None)
    val metar_nearby = METARFixtures.ValidAndCompleteLOWW.copy(altim_in_hg = None)
    val bot = new AviatorsBotForTesting()

    bot.airfieldDAO.near _ expects("LOWG", *) returning Future.successful(Seq(AirfieldFixtures.LOWW))
    bot.weatherService.getMetars _ expects Seq("LOWW") returning Future.successful(Map("LOWW" -> Seq(metar_nearby)))

    When("Calculating the density altitude for a station with missing altimeter setting")
    val calculator = new DensityAltitudeCalculator(bot.airfieldDAO, bot.weatherService)
    val result = calculator(metar)

    Then("Result should be an error message")
    result map { res =>
      res shouldEqual "Missing values in METAR, calculation can not be performed."
    }
  }

}
