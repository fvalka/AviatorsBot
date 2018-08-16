package com.vektorraum.aviatorsbot.bot.commands

case class Argument(name: String, matcher: String => Boolean, min: Option[Int] = None, max: Option[Int] = None,
                    preprocessor: String => String = identity) {

}
