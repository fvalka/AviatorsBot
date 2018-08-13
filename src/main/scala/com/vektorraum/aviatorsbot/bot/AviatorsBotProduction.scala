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
      0 minutes,
      5 minutes) {
      this.subscriptionHandler.run()
    }
  }

}
