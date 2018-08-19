package com.vektorraum.aviatorsbot.bot.util

/**
  * Utility class for parsing the metar or taf setting.
  */
object MetarTafOption {
  /**
    * Checks if this input is supposed to set the metar option to true
    *
    * @param input One argument
    * @return Metar should be set to this
    */
  def isMetar(input: String ): Boolean = {
    input.toLowerCase == "metar"
  }

  /**
    * Checks if this input is supposed to set the taf option to true
    *
    * @param input One argument
    * @return Taf should be set to this
    */
  def isTaf(input: String): Boolean = {
    input.toLowerCase == "taf"
  }

  /**
    * Is a valid argument to the bot
    *
    * @param input One argument
    * @return Valid argument input if true
    */
  def valid(input: String): Boolean = {
    isMetar(input) || isTaf(input)
  }

}
