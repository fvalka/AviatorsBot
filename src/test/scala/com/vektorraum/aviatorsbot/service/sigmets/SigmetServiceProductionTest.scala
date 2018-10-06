package com.vektorraum.aviatorsbot.service.sigmets

import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.softwaremill.sttp.akkahttp.AkkaHttpBackend
import com.softwaremill.sttp.testing.SttpBackendStub
import com.typesafe.config.{Config, ConfigFactory}
import com.vektorraum.aviatorsbot.service.regions.Regions
import org.scalatest.AsyncFeatureSpec
import org.scalatest.Matchers._

import scala.concurrent.Future
import scala.util.{Failure, Success}

class SigmetServiceProductionTest extends AsyncFeatureSpec {
  val stub_config: Config = ConfigFactory.parseString(
    "sigmet.url = \"http://only-for-testing.invalid\"\n")

  feature("Retrieve the SIGMET map from the web service") {
    scenario("Web service returns a valid response") {
      val backend = SttpBackendStub[Future, Source[ByteString, Any], Source[ByteString, Any]](AkkaHttpBackend())
        .whenRequestMatches(_.uri.path.endsWith(List("na")))
        .thenRespond(SigmetServiceWebServiceResponseFixtures.validInfos)

      val service = new SigmetServiceProduction(stub_config, backend)

      service.get(Regions.NorthAmerica) flatMap { res =>
        res.url shouldEqual "/static/MQ4aOs9MLozxPMMOgyqosqkxAOM-wHdmAmT_ngMOodI.png"
        res.infos(1) shouldEqual "WSNO34 ENMI 060600\nENBD SIGMET C01 VALID 060630/061030 ENVV-\nENOR NORWAY " +
          "FIR SEV TURB FCST WI N6200 E01210 - N6200 E00500 -\nN6320 E01200 - N6200 E01210 FL200/360 STNR NC="
        res.infos.size shouldBe 12
        res.failed shouldBe empty
      }
    }

    scenario("Backend call fails with server error") {
      val backend = SttpBackendStub[Future, Source[ByteString, Any], Source[ByteString, Any]](AkkaHttpBackend())
        .whenRequestMatches(_.uri.path.endsWith(List("na")))
        .thenRespondServerError()

      val service = new SigmetServiceProduction(stub_config, backend)

      service.get(Regions.NorthAmerica) transformWith {
        case Success(_) => assert(false)
        case Failure(exception) => exception.getMessage should include ("server error")
      }
    }

    scenario("Invalid response causes failed future") {
      val backend = SttpBackendStub[Future, Source[ByteString, Any], Source[ByteString, Any]](AkkaHttpBackend())
        .whenRequestMatches(_.uri.path.endsWith(List("na")))
        .thenRespond("this text is invalid json")

      val service = new SigmetServiceProduction(stub_config, backend)

      service.get(Regions.NorthAmerica) transformWith {
        case Success(_) => assert(false)
        case Failure(exception) => assert(true)
      }
    }
  }

}
