package com.vektorraum.aviatorsbot.persistence

import com.typesafe.config.Config
import reactivemongo.api.{DefaultDB, MongoConnection, MongoDriver}

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.concurrent.Future

/**
  * Database connector for reactive mongo
  */
class Db(config: Config) {
  protected val dbConfig: Config = config.getConfig("db")

  import scala.concurrent.ExecutionContext.Implicits.global // use any appropriate context

  // Connect to the database: Must be done only once per application
  val driver = MongoDriver()
  private val hosts: mutable.Buffer[String] = dbConfig.getStringList("hosts").asScala
  private val connection: MongoConnection = driver.connection(hosts)

  // Database and collections: Get references
  def aviatorsDb: Future[DefaultDB] = connection.database(dbConfig.getString("database"))

}
