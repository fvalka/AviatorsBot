package com.vektorraum.aviatorsbot.bot

import java.util.concurrent.TimeUnit

import com.vektorraum.aviatorsbot.bot.util.ReporterUtil

import scala.concurrent.duration._

/**
  * Created by fvalka on 21.05.2017.
  */
object AviatorsBotProduction extends AviatorsBot {
  def main(args: Array[String]): Unit = {
    logger.info("Starting production instance of AviatorsBot")
    this.run()

    system.scheduler.schedule(
      Duration(config.getString("subscriptions.handler.initial_delay")).asInstanceOf[FiniteDuration],
      Duration(config.getString("subscriptions.handler.interval")).asInstanceOf[FiniteDuration]) {
      this.subscriptionHandler.run()
    }

    ReporterUtil.graphiteReporter(config.getConfig("metrics.graphite"), this.metricRegistry)
      .foreach(_.start(config.getInt("metrics.graphite.interval_s"), TimeUnit.SECONDS))

  }

}
