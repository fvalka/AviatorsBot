package com.vektorraum.aviatorsbot.persistence

class DbForTesting extends Db {
  override protected val mongoUri: String = dbConfig.getString("url-test")

}
