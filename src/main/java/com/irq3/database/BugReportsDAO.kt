package com.irq3.database

import com.irq3.database.models.BugReports
import com.irq3.infrastucture.utils.DateUtil
import com.irq3.models.BugReport
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object BugReportsDAO {

    fun createBugReport(text:String, userId: Long): BugReport {
        val row = transaction {
            BugReports.insert {
                it[BugReports.bugDescription] = text
                it[BugReports.date] = Clock.System.now()
                it[BugReports.user] = userId
            }
        }
        return BugReport(
            id = row[BugReports.id],
            bugDescription = row[BugReports.bugDescription],
            dateCreated = DateUtil.fixInstant(row[BugReports.date].toJavaInstant()),
            userId = row[BugReports.user]
        )
    }

    fun getBugReport(id: Long){
        return transaction {
            BugReports.map(BugReports.selectAll().where { BugReports.id eq id }.first())
        }
    }
}