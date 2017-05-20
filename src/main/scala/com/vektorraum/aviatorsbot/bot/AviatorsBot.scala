package com.vektorraum.aviatorsbot.bot

import info.mukel.telegrambot4s.api.{Polling, TelegramBot}
import info.mukel.telegrambot4s.Implicits._
import info.mukel.telegrambot4s.api._
import info.mukel.telegrambot4s.methods._
import info.mukel.telegrambot4s.models._

import scala.io.Source

/**
  * Created by fvalka on 18.05.2017.
  */
object AviatorsBot extends TelegramBot with Polling with Commands {
  lazy val token: String = scala.util.Properties
    .envOrNone("BOT_TOKEN")
    .getOrElse(Source.fromResource("bot.token").getLines().mkString)

  on("hello") { implicit msg => _ => reply("My token is SAFE!") }

}
