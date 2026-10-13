package com.irq3.database

import com.irq3.database.models.Messages
import com.irq3.models.Message
import com.irq3.models.User
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.slf4j.LoggerFactory
import java.time.OffsetDateTime

object MessageDao {
    private val  logger = LoggerFactory.getLogger(this.javaClass)

    fun initMessage(usr: User, message: String, idMessage: Long, dateCreated: OffsetDateTime, idConversation: Long): Message{
        val row = transaction {
            Messages.insert {
                it[Messages.idUser] = usr.id!!
                it[Messages.message] = message
                it[Messages.dateCreated] = dateCreated.toInstant().toKotlinInstant()
                it[Messages.idMessage] = idMessage
                it[Messages.idConversation] = idConversation
            }
        }
        return Message(
            id = row[Messages.id],
            idUser = usr.id!!,
            message = message, idMessage = idMessage,
            dateCreated = dateCreated, idConversation = idConversation
        )

    }
    fun getMessagesFromConversation(id: Long) : List<Message>{
        return transaction {
            Messages.selectAll().where {
                Messages.idConversation eq id
            }.map { Messages.map(it) }
        }

    }
    fun setDiscordId(msg: Message, id: Long){
        logger.debug("Set id {} for message {}",id,msg.id)
        transaction {
            Messages.update({ Messages.id eq msg.id!!}){
                it[Messages.discordMessageId] = id
            }
        }
    }

    fun getMessageByDiscordSentId(discordMessageId: Long): Message?{
        return transaction {
            Messages.selectAll().where { Messages.discordMessageId eq discordMessageId }
                .map { Messages.map(it) }.firstOrNull()
        }
    }

    fun getMessageByDiscordId(discordMessageId: Long): Message?{
        return transaction {
            Messages.selectAll().where { Messages.idMessage eq discordMessageId }
                .map { Messages.map(it) }.firstOrNull()
        }
    }

}
enum class ID_TYPE{
    ID,DISCORD
}