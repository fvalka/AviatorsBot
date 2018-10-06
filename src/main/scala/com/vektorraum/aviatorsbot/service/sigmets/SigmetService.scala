package com.vektorraum.aviatorsbot.service.sigmets
import com.vektorraum.aviatorsbot.service.regions.Regions

import scala.concurrent.Future

trait SigmetService {

  /**
    * Retrieve a sigmet map and the map with further information about the SIGMETs plot onto the
    * map from the service.
    *
    * @param region Region for which to retrieve the map
    * @return Map url, SIGMETs which could not be plotted and SIGMET texts for the other sigmets.
    */
  def get(region: Regions): Future[PlotData]
}
