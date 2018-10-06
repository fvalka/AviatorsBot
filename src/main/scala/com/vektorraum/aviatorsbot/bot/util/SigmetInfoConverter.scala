package com.vektorraum.aviatorsbot.bot.util

import com.vektorraum.aviatorsbot.persistence.sigmets.model.SigmetInfo
import com.vektorraum.aviatorsbot.service.sigmets.PlotData

object SigmetInfoConverter {
  def apply(plotData: PlotData, chatId: Long): Seq[SigmetInfo] = {
    plotData.infos.map {
      case (id, body) =>
        SigmetInfo(chatId, id, body)
    } toList
  }
}

