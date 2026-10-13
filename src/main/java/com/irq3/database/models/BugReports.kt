package com.irq3.database.models

import com.irq3.infrastucture.utils.DateUtil
import com.irq3.models.BugReport
import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object BugReports : Table("bug_reports") {
    val id = long("id").autoIncrement()
    val bugDescription = text("bug_description")
    val date = timestamp("date")
    val user = long("user_id").references(Users.id)

    fun map(result: ResultRow): BugReport{
        return BugReport(result[id],
            result[bugDescription],
            DateUtil.fixInstant(result[date].toJavaInstant()),
            result[user])
    }
}