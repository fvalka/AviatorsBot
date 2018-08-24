package com.vektorraum.aviatorsbot.bot.subscriptions

import java.time.Instant

import com.typesafe.config.ConfigFactory
import com.vektorraum.aviatorsbot.bot.subscriptions.helpers.CallTracker
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.Eventually
import org.scalatest.time.{Millis, Span}
import org.scalatest.{FeatureSpec, GivenWhenThen}
import org.scalatest.Matchers._

class SchedulerTest extends FeatureSpec with GivenWhenThen with MockFactory with Eventually {
  info("As a pilot I want to received scheduled messages")
  info("these messages must arrive on time and be fault tolerant")

  // Comparison tolerance setting
  val delta = 200

  private val config400msDelay50ms = ConfigFactory.parseString(
    "interval = 50 milliseconds\n" +
      "initial_delay = 400 milliseconds\n")

  private val configNoDelay50ms = ConfigFactory.parseString(
    "interval = 50 milliseconds\n" +
      "initial_delay = 0 milliseconds\n")

  private val configNoDelay400ms = ConfigFactory.parseString(
    "interval = 400 milliseconds\n" +
    "initial_delay = 0 milliseconds\n")

  implicit override val patienceConfig: PatienceConfig =
    PatienceConfig(timeout = scaled(Span(4000, Millis)), interval = scaled(Span(30, Millis)))

  feature("Without exceptions the scheduler runs as configured") {
    scenario("Scheduler is started without delay") {
      Given("Configuration for no delay and short interval")
      val tracker = new CallTracker()

      When("Starting the Scheduler")
      Scheduler(configNoDelay400ms, tracker.run())

      Then("Scheduler has executed the function multiple times")
      eventually {
        tracker.calls should be > 2
        val runtime = tracker.callTimes(2).toEpochMilli - tracker.callTimes(1).toEpochMilli
        (runtime - delta < 400 && runtime + delta > 400) shouldEqual true
      }
    }

    scenario("Initial delay is respected") {
      Given("Configuration for no delay and short interval")
      val tracker = new CallTracker()

      When("Starting the Scheduler")
      Scheduler(config400msDelay50ms, tracker.run())
      val startTime = Instant.now()

      Then("Scheduler has executed the function multiple times")
      eventually {
        tracker.calls should be >= 1
        val runtime = tracker.callTimes.head.toEpochMilli - startTime.toEpochMilli
        (runtime - delta < 400 && runtime + delta > 400) shouldEqual true
      }
    }
  }

  feature("Exceptions are handled gracefully") {
    scenario("Function gets called again, even if exceptions are thrown") {
      Given("Configuration for no delay and short interval")
      val tracker = new CallTracker()

      When("Starting the Scheduler")
      Scheduler(configNoDelay400ms, tracker.runWithOneException(new RuntimeException))

      Then("Scheduler has executed the function multiple times")
      eventually {
        tracker.calls should be > 1
      }
    }

    scenario("Retriable exception leads to immediate retry") {
      Given("Configuration for no delay and short interval")
      val tracker = new CallTracker()

      When("Starting the Scheduler")
      Scheduler(configNoDelay400ms,
        tracker.runWithOneException(
          new RetriableException(new RuntimeException(), 1)))
      val startTime = Instant.now()

      Then("Scheduler has executed the function multiple times")
      eventually {
        tracker.calls should be > 1
        val runtime = tracker.callTimes(1).toEpochMilli - tracker.callTimes.head.toEpochMilli
        (runtime < 100) shouldEqual true
      }
    }
  }

}
