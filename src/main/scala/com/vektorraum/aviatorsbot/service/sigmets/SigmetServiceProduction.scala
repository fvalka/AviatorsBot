package com.vektorraum.aviatorsbot.service.sigmets

import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.softwaremill.sttp._
import com.softwaremill.sttp.json4s._
import com.typesafe.config.Config
import com.vektorraum.aviatorsbot.service.regions.Regions

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SigmetServiceProduction(config: Config,
                              implicit val backend: SttpBackend[Future, Source[ByteString, Any]])
  extends SigmetService {
  /**
    * Retrieve a sigmet map and the map with further information about the SIGMETs plot onto the
    * map from the service.
    *
    * @param region Region for which to retrieve the map
    * @return Map url, SIGMETs which could not be plotted and SIGMET texts for the other sigmets.
    */
  override def get(region: Regions): Future[PlotData] = {
    val base_url = config.getString("sigmet.url")
    val request = sttp
      .get(uri"$base_url/sigmet_map/${region.value}")
      .response(asJson[PlotData])
    val response: Future[Response[PlotData]] = request.send()

    response map { res =>
      res
        .body
        .getOrElse(throw new RuntimeException(
          res.body.left.getOrElse("Could not load SIGMETs")))
    }
  }
}
