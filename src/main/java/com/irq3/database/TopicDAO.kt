package com.irq3.database

import com.irq3.database.models.Topics
import com.irq3.models.Topic
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.Random
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object TopicDAO {

    fun createTopic(topic: String, adminId: Long){
        transaction {
            Topics.insert {
                it[Topics.topic] = topic
                it[Topics.adminId] = adminId
                it[Topics.dateCreated] = Clock.System.now()
            }
        }
    }

    fun getTopic(id: Long): Topic?{
        return transaction {
            Topics.select(Topics.topic).where { Topics.id eq id }.limit(1).map{Topics.map(it)}.firstOrNull()
        }
    }

    fun getRandomTopic(): Topic? {
        return transaction {
            Topics.selectAll()
                .orderBy(Random())
                .limit(1)
                .map { Topics.map(it) }
                .firstOrNull()
        }
    }
}