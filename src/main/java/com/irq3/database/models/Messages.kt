package com.irq3.database.models

import com.irq3.models.Message
import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.time.ZoneId

object  Messages : Table("messages"){
    val id = long("id").autoIncrement()
    val idUser = long("iduser")
    val message = text("message")
    val dateCreated = timestamp("date_created")
    val idMessage = long("id_message")
    val idConversation = long("conservation_id")
    val discordMessageId = long("discord_message_id").nullable()


    override val primaryKey = PrimaryKey(id)

    fun map(msg : ResultRow): Message{
        return Message(msg[id], msg[idUser], msg[message],
            msg[dateCreated].toJavaInstant().
            atZone(ZoneId.systemDefault()).toOffsetDateTime(), msg[idMessage],
            msg[idConversation],msg[discordMessageId]
            )
    }
}

