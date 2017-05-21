package com.vektorraum.aviatorsbot.bot
import com.vektorraum.aviatorsbot.service.weather.AddsWeatherService
import com.vektorraum.aviatorsbot.service.weather.fixtures.ResponseFixtures
import com.vektorraum.aviatorsbot.service.weather.mocks.AddsWeatherServiceForTest
import info.mukel.telegrambot4s.methods.ParseMode.ParseMode
import info.mukel.telegrambot4s.models._
import org.scalamock.scalatest.MockFactory

import scala.concurrent.Future
import scala.xml.Elem

/**
  * Created by fvalka on 21.05.2017.
  */
class AviatorsBotForTesting(metarResponseStub: Elem) extends AviatorsBot with MockFactory {
  var replySent: String = ""
  override lazy val weatherService = new AddsWeatherServiceForTest(metarResponseStub)

  override def reply(text                  : String,
                     parseMode             : Option[ParseMode] = None,
                     disableWebPagePreview : Option[Boolean] = None,
                     disableNotification   : Option[Boolean] = None,
                     replyToMessageId      : Option[Long] = None,
                     replyMarkup           : Option[ReplyMarkup] = None)
                    (implicit message: Message): Future[Message] =
  {
    replySent = text
    Future {
      val chat = Chat(123L, ChatType.Private)
      Message(messageId = 123L, chat = chat, date = 1234444)
    }
  }

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
}
