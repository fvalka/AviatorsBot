package com.vektorraum.aviatorsbot.persistence.regions

import com.vektorraum.aviatorsbot.persistence.regions.model.RegionSetting
import com.vektorraum.aviatorsbot.persistence.{Db, DbUtil, WriteResult}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, Macros}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RegionsDAOProduction(db: Db) extends RegionsDAO {
  def collection: Future[BSONCollection] = db.aviatorsDb.map(_.collection("regions"))

  implicit def regionPreferenceReader: BSONDocumentReader[RegionSetting] = Macros.reader[RegionSetting]
  implicit def regionPreferenceWriter: BSONDocumentWriter[RegionSetting] = Macros.writer[RegionSetting]

  /**
    * Set the region preference for a specific user, identified by their chatId
    *
    * @param regionSetting RegionSetting to be updated or created
    * @return Write result okay if the update or store operation succeeded
    */
  override def set(regionSetting: RegionSetting): Future[WriteResult] = {
    get(regionSetting.chatId) flatMap {
      case Some(_) =>
        val selector = BSONDocument("chatId" -> regionSetting.chatId)
        val update = BSONDocument("$set" -> BSONDocument("region" -> regionSetting.region))
        collection.flatMap(_.update(selector, update)) map DbUtil.convertWriteResult
      case None =>
        collection.flatMap(_.insert(regionSetting)) map DbUtil.convertWriteResult
    }

  }

  /**
    * Search for a users RegionSetting
    * @param chatId Users chatId
    * @return RegionSetting if found in the database
    */
  override def get(chatId:Long): Future[Option[RegionSetting]] = {
    val query = BSONDocument("chatId" -> chatId)
    collection flatMap(_.find(query).one[RegionSetting])
  }

}
