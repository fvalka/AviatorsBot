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

    Then("")
    result should not be empty
    result.get.report()
  }

}
