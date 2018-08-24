package com.vektorraum.aviatorsbot.bot.util

import com.typesafe.config.ConfigFactory
import com.vektorraum.aviatorsbot.bot.AviatorsBotForTesting
import org.scalatest.{FunSuite, GivenWhenThen}
import org.scalatest.Matchers._

class ReporterUtilTest extends FunSuite with GivenWhenThen {
  test("Graphite reporter interprets configuration correctly") {
    Given("Valid Configuration and metrics registry")
    val bot = new AviatorsBotForTesting()
    val registry = bot.metricRegistry
    val config = ConfigFactory.parseString("enabled = true\n    " +
      "host = \"localhost\"\n    " +
      "port = \"2003\"\n    " +
      "prefix = " +
      "\"aviatorsbot\"\n    " +
      "interval_s = 60")

    When("Creating a new graphite reporter")
    val result = ReporterUtil.graphiteReporter(config, registry)

    Then("Reporter should be created")
    result should not be empty
  }

  test("Disabled configuration returns an empty optional") {
    Given("Valid Configuration and metrics registry")
    val bot = new AviatorsBotForTesting()
    val registry = bot.metricRegistry
    val config = ConfigFactory.parseString("enabled = false\n    " +
      "host = \"localhost\"\n    " +
      "port = \"2003\"\n    " +
      "prefix = " +
      "\"aviatorsbot\"\n    " +
      "interval_s = 60")

    When("Creating a new graphite reporter")
    val result = ReporterUtil.graphiteReporter(config, registry)

    Then("Reporter should not be created")
    result shouldBe empty
  }

  test("CSV Reporter interprets configuration correctly") {
    Given("Valid configuration and metrics registry")
    val bot = new AviatorsBotForTesting()
    val registry = bot.metricRegistry
    val config = ConfigFactory.parseString("enabled = true\n    directory = \"logs/metrics.csv\"")

    When("Creating a CSV reporter")
    val result = ReporterUtil.csvReporter(config, registry)

    Then("Reporter should be created")
    result should not be empty
  }

  test("CSV Reporter returns empty result when disabled") {
    Given("Valid configuration and metrics registry")
    val bot = new AviatorsBotForTesting()
    val registry = bot.metricRegistry
    val config = ConfigFactory.parseString("enabled = false\n    directory = \"logs/metrics.csv\"")

    When("Creating a CSV reporter")
    val result = ReporterUtil.csvReporter(config, registry)

    Then("Reporter should be created")
    result shouldBe empty
  }

}
