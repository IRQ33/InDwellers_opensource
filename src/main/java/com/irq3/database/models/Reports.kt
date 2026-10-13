package com.irq3.database.models

import com.irq3.models.Report
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object Reports : Table("reports") {
    val id = long("id").autoIncrement()
    val reporting = long("reporting")
    val reported = long("reported")
    val chatId = long("chat_id")
    val reason = text("reason").nullable()
    val approved = bool("approved").nullable()
    val admin = long("admin").nullable()

    override val primaryKey = PrimaryKey(id)

    fun map(result: ResultRow): Report{
        return Report(result[id],
            result[reporting],
            result[reported],
            result[chatId],
            result[reason].orEmpty())
    }

}