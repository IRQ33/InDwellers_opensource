package com.irq3.database

import com.irq3.models.Report
import com.irq3.database.models.Reports
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.slf4j.LoggerFactory

object ReportDAO {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun initReport(reporting: Long,reported: Long, conversation: Long, reason: String): Long{
       return  transaction {
            val report =  Reports.insert {
                it[Reports.reporting] = reporting
                it[Reports.reported] = reported
                it[Reports.chatId] = conversation
                it[Reports.reason] = reason
            }
           logger.info("New report was created with id {}",report[Reports.id])
            report[Reports.id]
        }
    }

    fun writeOpinion(reportId: Long, opinion: Boolean, adminId: Long){
        transaction {
            Reports.update({ Reports.id eq reportId}){
                it[Reports.approved] = opinion
                it[Reports.admin] = adminId
            }
        }
    }

    fun getReport(id: Long): Report?{
        return transaction {
            Reports.selectAll().where{
                Reports.id eq id
            }.limit(1).firstOrNull()?.let {
                Reports.map(it)
            }
        }

    }

    fun getReport(): Report?{
        return transaction {
            Reports.selectAll().where{
                Reports.admin eq null
            }.limit(1).firstOrNull()?.let {
                Reports.map(it)
            }
        }
    }
}