package com.vektorraum.aviatorsbot.persistence

import com.typesafe.config.Config
import reactivemongo.api.{DefaultDB, MongoConnection, MongoDriver}

import scala.concurrent.Future
import scala.util.Try

/**
  * Database connector for reactive mongo
  */
class Db(config: Config) {
  protected val dbConfig: Config = config.getConfig("db")
  protected val mongoUri: String = dbConfig.getString("url")

  import scala.concurrent.ExecutionContext.Implicits.global // use any appropriate context

  // Connect to the database: Must be done only once per application
  val driver = MongoDriver()
  val parsedUri: Try[MongoConnection.ParsedURI] = MongoConnection.parseURI(mongoUri)
  val connection: Try[MongoConnection] = parsedUri.map(driver.connection)

  // Database and collections: Get references
  val futureConnection: Future[MongoConnection] = Future.fromTry(connection)

  def aviatorsDb: Future[DefaultDB] = futureConnection.flatMap(_.database(dbConfig.getString("database")))

}
