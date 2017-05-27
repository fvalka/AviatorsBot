package com.vektorraum.aviatorsbot.bot.util

import info.mukel.telegrambot4s.api.{CommandParser, Commands}
import info.mukel.telegrambot4s.models.Message

/**
  * Created by fvalka on 23.05.2017.
  */
trait AliasCommands extends Commands {
  private type Args = Seq[String]
  private type MessageActionWithArgs = Message => Args => Unit

  override def on(command: String, description: String, parser: CommandParser = AliasCommandParser)
                 (actionWithArgs: MessageActionWithArgs): Unit = super.on(command, description, parser)(actionWithArgs)

}

object AliasCommandParser extends CommandParser {
  override def unapply(msg: Message): Option[(String, AliasCommandParser.Args)] =
    CommandParser.Default.unapply(msg)

  override def matchCommands(fromParser: String, target: String): Boolean =
    super.matchCommands(fromParser.stripPrefix("/"), target.stripPrefix("/"))
}
