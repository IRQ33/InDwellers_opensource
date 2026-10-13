package com.irq3.database.models

import com.irq3.models.Conversation
import com.irq3.infrastucture.utils.DateUtil
import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object Conversations : Table("conversations") {

    var id = long("id").autoIncrement()
    val dateCreated = timestamp("date_created")
    var idPerson1  = long("id_person1")
    var idPerson2 = long("id_person2")

    override val primaryKey = PrimaryKey(id)

    fun map(result: ResultRow): Conversation{
      return  Conversation(result[id],
            DateUtil.fixInstant(result[dateCreated].toJavaInstant()),
            result[idPerson1],
            result[idPerson2])
    }
}
