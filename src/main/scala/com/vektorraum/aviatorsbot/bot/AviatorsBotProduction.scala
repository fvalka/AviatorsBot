package com.vektorraum.aviatorsbot.bot

/**
  * Created by fvalka on 21.05.2017.
  */
object AviatorsBotProduction extends AviatorsBot {
  def main(args: Array[String]): Unit = {
    logger.info("Starting production instance of AviatorsBot")
    this.run()
  }

}
