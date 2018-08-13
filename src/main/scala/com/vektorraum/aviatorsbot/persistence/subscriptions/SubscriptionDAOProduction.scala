package com.vektorraum.aviatorsbot.persistence.subscriptions

import java.util.Date

import com.vektorraum.aviatorsbot.persistence.subscriptions.model.{LatestInfo, Subscription}
import com.vektorraum.aviatorsbot.persistence.{Db, subscriptions}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.{Cursor, commands}
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, Macros}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * _    _                                       
  * | |  | |                                      
  * __   _____| | _| |_ ___  _ __ _ __ __ _ _   _ _ __ ___  
  * \ \ / / _ \ |/ / __/ _ \| '__| '__/ _` | | | | '_ ` _ \ 
  * \ V /  __/   <| || (_) | |  | | | (_| | |_| | | | | | |
  * \_/ \___|_|\_\\__\___/|_|  |_|  \__,_|\__,_|_| |_| |_|
  *
  * vektorraum.com
  *
  * Created by fvalka on 27.05.2017.
  */
class SubscriptionDAOProduction(db: Db)  extends SubscriptionDAO {
  def airfieldCollection: Future[BSONCollection] = db.aviatorsDb.map(_.collection("subscriptions"))

  // Reading
  implicit def subscriptionReader: BSONDocumentReader[Subscription] = Macros.reader[Subscription]
  implicit def latestInfoReader: BSONDocumentReader[LatestInfo] = Macros.reader[LatestInfo]

  // Writing
  implicit def subscriptionWriter: BSONDocumentWriter[Subscription] = Macros.writer[Subscription]
  implicit def latestInfoWriter: BSONDocumentWriter[LatestInfo] = Macros.writer[LatestInfo]


  /**
    * Inserts a new subscriptions or updates the validUntil field of the already existing subscription
    *
    * @param subscription Subscription DTO, combination of chatId and icao must be unique
    * @return The result of either the update or insert operation
    */
  override def addOrExtend(subscription: Subscription): Future[WriteResult] = {
    find(subscription.chatId, subscription.icao) flatMap {
      case Some(sub) =>
        val selector = findByChatIdAndIcaoQuery(subscription.chatId, subscription.icao)
        val update = BSONDocument("$set" -> BSONDocument("validUntil" -> subscription.validUntil))
        airfieldCollection.flatMap(_.update(selector, update)) map convertWriteResult
      case None => airfieldCollection.flatMap(_.insert(subscription)) map convertWriteResult
    }
  }

  /**
    * Searches for a subscription using the chatId and ICAO identifier
    *
    * @param chatId Id of the chat with the user
    * @param icao ICAO Code of the station for which this subscription is
    * @return One or no subscription matching the query parameters
    */
  override def find(chatId: Long, icao: String): Future[Option[Subscription]] = {
    purgeOld() flatMap { _ =>
      val query = findByChatIdAndIcaoQuery(chatId, icao)
      airfieldCollection.flatMap(_.find(query).one[Subscription])
    }
  }

  /**
    * Find all subscriptions for a chatId
    *
    * @param chatId Id of the chat with the user
    * @return All subscriptions found for this chat id
    */
  override def findAllByChatId(chatId: Long): Future[List[Subscription]] = {
    purgeOld() flatMap { _ =>
      val query = BSONDocument("chatId" -> chatId)
      val errorHandler = Cursor.FailOnError[List[Subscription]]()

      airfieldCollection.flatMap(_.find(query).cursor[Subscription]().collect[List](-1, errorHandler))
    }
  }

  /**
    * Finds all subscriptions for a specific stations ICAO code
    *
    * @param icao ICAO code of the station for which all subscribers should be found
    * @return List of subscriptions for this station
    */
  override def findAllSubscriptionsForStation(icao: String): Future[List[Subscription]] = {
    purgeOld() flatMap { _ =>
      val query = BSONDocument("icao" -> icao)
      val errorHandler = Cursor.FailOnError[List[Subscription]]()

      airfieldCollection.flatMap(_.find(query).cursor[Subscription]().collect[List](-1, errorHandler))
    }
  }

  /**
    * Find all stations which have any subscriptions
    *
    * @return A set of all ICAO codes to which at least one user has subscribed
    */
  override def findAllStations(): Future[Set[String]] = {
    purgeOld() flatMap { _ =>
      airfieldCollection.flatMap(_.distinct[String, Set]("icao"))
    }
  }

  /**
    * Removes a subscription for a specific chatId and station from the
    * database.
    *
    * @param chatId Id of the chat with the user
    * @param station ICAO identifier of the station to be removed
    * @return Future of the write result
    */
  override def remove(chatId: Long, station: String): Future[WriteResult] = {
    val query = findByChatIdAndIcaoQuery(chatId, station)
    airfieldCollection.flatMap(_.remove(query)) map convertWriteResult
  }

  /**
    * Remove all subscriptions which are no longer active (validUntil is in the past)
    *
    * @return Write result of the remove operation
    */
  override def purgeOld(): Future[WriteResult] = {
    val query = BSONDocument("validUntil" -> BSONDocument("$lt" -> new Date()))
    airfieldCollection.flatMap(_.remove(query)) map convertWriteResult
  }

  protected def findByChatIdAndIcaoQuery(chatId: Long, icao: String): BSONDocument = {
    BSONDocument("chatId" -> chatId, "icao" -> icao)
  }

  /**
    * Converts a database writeResult into an encapsulated one
    *
    * @param original ReactiveMongo WriteResult
    * @return Encapsulated WriteResult
    */
  protected def convertWriteResult(original: commands.WriteResult): WriteResult = {
    subscriptions.WriteResult(original.ok)
  }


}
