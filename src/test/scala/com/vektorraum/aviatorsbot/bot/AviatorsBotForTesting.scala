package com.vektorraum.aviatorsbot.bot
import com.vektorraum.aviatorsbot.persistence.airfielddata.AirfieldDAO
import com.vektorraum.aviatorsbot.persistence.subscriptions.SubscriptionDAO
import com.vektorraum.aviatorsbot.service.weather.AddsWeatherService
import info.mukel.telegrambot4s.methods.ParseMode.ParseMode
import info.mukel.telegrambot4s.models._
import org.scalamock.scalatest.MockFactory

import scala.concurrent.Future

/**
  * AviatorsBot subclass used exclusively in unit tests.
  *
  * Injects stubs and mocks instead of actual backend services and records the
  * response sent to the user instead of sending it over the Telegram API.
  *
  * The response can be retrieved from the replySent field.
  *
  * Created by fvalka on 21.05.2017.
  */
class AviatorsBotForTesting() extends AviatorsBot with MockFactory {
  var replySent: String = ""
  var parseMode: Option[ParseMode] = None
  override lazy val weatherService: AddsWeatherService = mock[AddsWeatherService]
  override lazy val airfieldDAO: AirfieldDAO = stub[AirfieldDAO]
  override lazy val subscriptionDAO: SubscriptionDAO = mock[SubscriptionDAO]

  // ensures that the bot can not connect to the real Telegram API
  override lazy val token: String = "TESTONLY"

  override def reply(text: String,
                     parseMode: Option[ParseMode] = None,
                     disableWebPagePreview: Option[Boolean] = None,
                     disableNotification: Option[Boolean] = None,
                     replyToMessageId: Option[Long] = None,
                     replyMarkup: Option[ReplyMarkup] = None)
                    (implicit message: Message): Future[Message] = {
    if (replySent != "") {
      throw new IllegalArgumentException("This object must not be reused")
    }
    if (text.length > 4096) {
      throw new IllegalArgumentException("Text is too long and can not be sent to the real Telegram API!")
    }
    replySent = text
    this.parseMode = parseMode
    Future {
      val chat = Chat(123L, ChatType.Private)
      Message(messageId = 123L, chat = chat, date = 1234444)
    }
  }

  /**
    * Used to simulate an incoming Telegram message
    *
    * @param text Simulated text sent by the user
    */
  def receiveMockMessage(text: String): Unit = {
    val chatStub = Chat(119771589, ChatType.Private, None, Some("test_user"), Some("Test"), Some("User"), None)
    val messageStub = Message(messageId = 75,
      from = Some(User(119771589, "Test")),
      date = 1495371318,
      chat = chatStub,
      text = Some(text))
    val updateStub = Update(updateId = 329618378,
      message = Some(messageStub))

    onUpdate(updateStub)
  }

  /**
    * Ensures that the bot can not be started
    */
  override def run(): Unit = {
    throw new RuntimeException("The testing bot must never be started!")
  }
}
