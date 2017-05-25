package com.vektorraum.aviatorsbot.persistence

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}
import reactivemongo.api.{DefaultDB, MongoConnection, MongoDriver}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

/**
  * Created by fvalka on 25.05.2017.
  */
object Db {
  protected val dbConfig: Config = ConfigFactory.parseFile(new File("conf/aviatorsbot.conf")).getConfig("db")
  protected val mongoUri: String = dbConfig.getString("url")

  import scala.concurrent.ExecutionContext.Implicits.global // use any appropriate context

  // Connect to the database: Must be done only once per application
  val driver = MongoDriver()
  val parsedUri: Try[MongoConnection.ParsedURI] = MongoConnection.parseURI(mongoUri)
  val connection: Try[MongoConnection] = parsedUri.map(driver.connection)

  // Database and collections: Get references
  val futureConnection: Future[MongoConnection] = Future.fromTry(connection)

  def aviatorsDb: Future[DefaultDB] = futureConnection.flatMap(_.database("aviatorsbot"))

}