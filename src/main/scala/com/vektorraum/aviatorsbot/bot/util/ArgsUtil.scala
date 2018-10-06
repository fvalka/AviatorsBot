package com.vektorraum.aviatorsbot.bot.util

import scala.util.matching.Regex

object ArgsUtil {
  val validNumberPattern: Regex = "\\d*".r

  def isNumber(input: String): Boolean = {
    validNumberPattern.pattern.matcher(input).matches()
  }

}
