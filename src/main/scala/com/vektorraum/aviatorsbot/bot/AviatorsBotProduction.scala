package com.vektorraum.aviatorsbot.bot

import java.util.concurrent.TimeUnit

import com.vektorraum.aviatorsbot.bot.subscriptions.Scheduler
import com.vektorraum.aviatorsbot.bot.util.ReporterUtil

/**
  * Created by fvalka on 21.05.2017.
  */
object AviatorsBotProduction extends AviatorsBot {
  def main(args: Array[String]): Unit = {
    logger.info("Starting production instance of AviatorsBot")
    this.run()

    Scheduler(config.getConfig("subscriptions.handler"), this.subscriptionHandler.run)

    ReporterUtil.graphiteReporter(config.getConfig("metrics.graphite"), this.metricRegistry)
      .foreach(_.start(config.getInt("metrics.graphite.interval_s"), TimeUnit.SECONDS))

    ReporterUtil.csvReporter(config.getConfig("metrics.csv"), this.metricRegistry)
      .foreach(_.start(config.getInt("metrics.csv.interval_s"), TimeUnit.SECONDS))

  }

}
