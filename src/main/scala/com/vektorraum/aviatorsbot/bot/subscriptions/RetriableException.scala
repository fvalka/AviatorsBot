package com.vektorraum.aviatorsbot.bot.subscriptions

class RetriableException(val cause: Throwable, val maxRetries: Int = 1) extends Exception {
  super.initCause(cause)

}
