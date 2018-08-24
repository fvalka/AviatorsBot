package com.vektorraum.aviatorsbot.bot.subscriptions

import com.typesafe.scalalogging.Logger
import nl.grons.metrics4.scala.DefaultInstrumented
import org.quartz.{DisallowConcurrentExecution, Job, JobExecutionContext, JobExecutionException}

import scala.util.control.NonFatal

@DisallowConcurrentExecution
class SchedulerJob extends Job with DefaultInstrumented {
  private val logger = Logger(getClass)
  private val meter = metrics.meter("subscription-handler-job-exec")

  override def execute(context: JobExecutionContext): Unit = {
    try {
      logger.info("Executing SubscriptionHandlerJob")
      meter.mark()

      SchedulerJob.executor()
    } catch {
      case ex: RetriableException =>
        logger.warn(s"Retriable exception thrown maxRetries=${ex.maxRetries} retries=${context.getRefireCount}")
        val refire = context.getRefireCount < ex.maxRetries
        throw new JobExecutionException(ex, refire)
      case NonFatal(ex) =>
        logger.warn("Exception thrown during SchedulerJob execution", ex)
        throw new JobExecutionException(ex, false)
    }
  }
}

object SchedulerJob {
  private val logger = Logger(getClass)

  protected var setAlready = false
  protected var executor: () => Unit = () => Unit

  def setExecutor(func: () => Unit): Unit = {
    if(setAlready) {
      logger.warn("SchedulerJob setExecutor was called again. Overwriting the previous function!")
    }
    executor = func
    setAlready = true
  }
}
