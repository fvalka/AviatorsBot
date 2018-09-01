package com.vektorraum.aviatorsbot.persistence

import java.io.File

import com.softwaremill.macwire.wire
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait DatabaseSetup {

  protected val config: Config = ConfigFactory.parseFile(new File("conf/aviatorsbot-test.conf"))
  val db: Db = wire[Db]

  /**
    * Used instead of before/after methods because it is also async which
    * is not supported by ScalaTest in the current version
    *
    * @return Future, which when completed means that the db is ready for
    *         testing
    */
  protected def cleanDb: Future[Unit] = {
    db.aviatorsDb flatMap { aviatorsDb =>
      aviatorsDb.drop()
    }
  }
}
