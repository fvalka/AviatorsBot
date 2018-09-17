package com.vektorraum.aviatorsbot.persistence.sigmets

import java.util.Date

import com.vektorraum.aviatorsbot.persistence.sigmets.model.SigmetInfo
import com.vektorraum.aviatorsbot.persistence.{Db, DbUtil, WriteResult}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, Macros}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * DAO for managing SigmetInfos.Those are lists of SIGMETs with their index identifier as plotted
  * on the sigmet map.
  *
  * @param db Database object used for connection establishment
  */
class SigmetInfoDAOProduction(db: Db) extends SigmetInfoDAO {
  def collection: Future[BSONCollection] = db.aviatorsDb.map(_.collection("sigmetinfos"))

  implicit def regionPreferenceReader: BSONDocumentReader[SigmetInfo] = Macros.reader[SigmetInfo]
  implicit def regionPreferenceWriter: BSONDocumentWriter[SigmetInfo] = Macros.writer[SigmetInfo]

  /**
    * Retrieves a SigmetInfo for a specific user from the database
    *
    * @param chatId ChatId of the user
    * @return Future optional of the SigmetInfo. None if no SigmetInfo was found for this user.
    */
  override def get(chatId: Long, index: Int): Future[Option[SigmetInfo]] = {
    purgeOld() flatMap { _ =>
      val query = BSONDocument("chatId" -> chatId, "index" -> index)
      collection flatMap (_.find(query).one[SigmetInfo])
    }
  }

  /**
    * Stores the sigmetinfos.
    *
    * WARNING! It will delete all infos for all chatIds in the input sequence
    *
    * @param sigmetInfos All SigmetInfos to be stored
    * @return Write result, ok if the store was successfull for all elements
    */
  override def store(sigmetInfos: Seq[SigmetInfo]): Future[WriteResult] = {
    require(sigmetInfos.nonEmpty, "Empty sequence could not be stored")

    val deleteFutures = sigmetInfos
      .map(_.chatId)
      .toSet
      .map(removeAll)

    Future.sequence(deleteFutures) flatMap { _ =>
      collection.flatMap(_.insert(ordered = false).many(sigmetInfos)) map DbUtil.convertWriteResult
    }
  }

  /**
    * Delete all stored SigmetInfos for a specific user
    *
    * @param chatId Users chatId for which all SigmetInfos will be deleted
    * @return WriteResult.ok if the delete was successful
    */
  override def removeAll(chatId: Long): Future[WriteResult] = {
    val query = BSONDocument("chatId" -> chatId)
    collection.flatMap(_.delete().one(query)) map DbUtil.convertWriteResult
  }

  /**
    * Remove all subscriptions which are no longer active (validUntil is in the past)
    *
    * @return Write result of the remove operation
    */
  override def purgeOld(): Future[WriteResult] = {
    val query = BSONDocument("validUntil" -> BSONDocument("$lt" -> new Date()))
    collection.flatMap(_.remove(query)) map DbUtil.convertWriteResult
  }


}
