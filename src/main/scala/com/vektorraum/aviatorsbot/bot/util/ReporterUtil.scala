package com.vektorraum.aviatorsbot.bot.util

import java.io.File
import java.net.InetSocketAddress
import java.util.Locale
import java.util.concurrent.TimeUnit

import com.codahale.metrics.graphite.{Graphite, GraphiteReporter}
import com.codahale.metrics.{CsvReporter, MetricFilter, MetricRegistry}
import com.typesafe.config.Config

object ReporterUtil {
  /**
    * Builds a dropwizard graphite reporter based upon the configuration
    *
    * Correct configuration example:
    *
    * enabled = true
    * host = "localhost"
    * port = 2003
    * prefix = "aviatorsbot"
    *
    * @param config Configuration at most detailed level
    * @param registry Metrics registry
    * @return Configured reporter ready to be started
    */
  def graphiteReporter(config: Config, registry: MetricRegistry): Option[GraphiteReporter] = {
    if(config.getBoolean("enabled")) {
      val graphite = new Graphite(new InetSocketAddress(config.getString("host"), config.getInt("port")))

      val reporter = GraphiteReporter
        .forRegistry(registry)
        .prefixedWith(config.getString("prefix"))
        .convertRatesTo(TimeUnit.SECONDS)
        .convertDurationsTo(TimeUnit.MILLISECONDS)
        .filter(MetricFilter.ALL)
        .build(graphite)

      Some(reporter)
    } else {
      None
    }
  }


  /**
    * Builds a dropwizard CSV Reporter based upon the configuration in config
    *
    * Valid configuration example:
    * enabled = false
    * directory = "logs/"
    *
    * @param config Configuration at most detailed level
    * @param registry Metrics registry
    * @return Configured reporter ready to be started
    */
  def csvReporter(config: Config, registry: MetricRegistry): Option[CsvReporter] = {
    if(config.getBoolean("enabled")) {
      val directory = new File(config.getString("directory"))
      if(!directory.exists()) { directory.mkdirs() }

      val reporter = CsvReporter.forRegistry(registry)
        .formatFor(Locale.US)
        .convertRatesTo(TimeUnit.SECONDS)
        .convertDurationsTo(TimeUnit.MILLISECONDS)
        .filter(MetricFilter.ALL)
        .build(directory)

      Some(reporter)
    } else {
      None
    }
  }

}
