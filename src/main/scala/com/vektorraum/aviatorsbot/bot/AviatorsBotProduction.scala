package com.vektorraum.aviatorsbot.bot

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

    import com.codahale.metrics.ConsoleReporter
    import java.util.concurrent.TimeUnit
    val reporter = ConsoleReporter.forRegistry(this.metricRegistry).convertRatesTo(TimeUnit.SECONDS)
      .convertDurationsTo(TimeUnit.MILLISECONDS).build
    reporter.start(3, TimeUnit.MINUTES)
  }

}
