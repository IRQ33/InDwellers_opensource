package com.irq3.database.models

import com.irq3.infrastucture.utils.DateUtil
import com.irq3.models.Topic
import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object Topics: Table("topics") {
    val id = long("id").autoIncrement()
    val topic = text("topic")
    val dateCreated = timestamp("date_created")
    val adminId = long("admin_id")

    override val primaryKey: PrimaryKey = PrimaryKey(id)

    fun map(result : ResultRow): Topic{
        return Topic(
            result[id],
            result[topic],
            DateUtil.fixInstant(result[dateCreated].toJavaInstant()),
            result[adminId]

        )
    }
}

/*
CREATE TABLE topics(
    id SERIAL PRIMARY KEY NOT NULL,
    topic TEXT NOT NULL ,
    date_created TIMESTAMP NOT NULL,
    admin_id BIGINT NOT NULL
)
 */