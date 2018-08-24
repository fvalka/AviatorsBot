package com.vektorraum.aviatorsbot.bot.subscriptions.helpers

import java.time.Instant

import scala.collection.mutable.ListBuffer

class CallTracker() {
  var calls = 0
  var callTimes: ListBuffer[Instant] = ListBuffer.empty

  /**
    * Run a function while counting the number of times the function has been called
    *
    * @param func Function to be run
    */
  def run(func: () => Unit = () => Unit)(): Unit = {
    calls += 1
    callTimes += Instant.now()
    func()
  }

  /**
    * Throws the exception on first call and on all other calls runs func
    *
    * @param ex Throwable thrown on the first run
    * @param func Function to be executed on all later runs
    */
  def runWithOneException(ex: Throwable, func: () => Unit = () => Unit)(): Unit = {
    calls += 1
    callTimes += Instant.now()

    if(calls == 1) {
      throw ex
    } else {
      func()
    }
  }

}
