package com.vektorraum.aviatorsbot.persistence.regions
import com.vektorraum.aviatorsbot.persistence.WriteResult
import com.vektorraum.aviatorsbot.persistence.regions.model.RegionSetting

import scala.concurrent.Future

trait RegionsDAO {

  /**
    * Set the region preference for a specific user, identified by their chatId
    *
    * @param regionSetting RegionSetting to be updated or created
    * @return Write result okay if the update or store operation succeeded
    */
  def set(regionSetting: RegionSetting): Future[WriteResult]

  /**
    * Search for a users RegionSetting
    *
    * @param chatId Users chatId
    * @return RegionSetting if found in the database
    */
  def get(chatId: Long): Future[Option[RegionSetting]]
}
