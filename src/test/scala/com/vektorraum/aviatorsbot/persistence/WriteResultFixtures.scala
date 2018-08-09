package com.vektorraum.aviatorsbot.persistence

import com.vektorraum.aviatorsbot.persistence.subscriptions.WriteResult

object WriteResultFixtures {

  val WriteResultOk = WriteResult(ok = true)
  val WriteResultFailed = WriteResult(ok = false)
}
