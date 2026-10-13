package com.irq3.core.services

import com.irq3.core.DIManager
import com.irq3.database.BugReportsDAO
import com.irq3.discord.utils.Embed
import com.irq3.infrastucture.EnvController
import com.irq3.models.BugReport
import org.slf4j.LoggerFactory

class BugReportServices(val bugReports: BugReportsDAO) {

    private val  logger = LoggerFactory.getLogger(this.javaClass)

    fun createReport(bugDescription:String, userId: Long){
        val report = bugReports.createBugReport(bugDescription,userId)
        logger.info("New report was made {}",report.toString())
        sendReport(report)
    }

    private fun sendReport(report: BugReport){
        DIManager.sendOnGuildChannel(EnvController.getData("BUG_CHANNEL").toLong(),
            Embed("System", "Report ${report.id}\n " +
                    "${report.bugDescription} \n -# " +
                    "${report.dateCreated}"))
    }
}