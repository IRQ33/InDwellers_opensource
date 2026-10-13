package com.irq3.database

import com.irq3.models.Conversation
import com.irq3.database.models.Conversations
import com.irq3.database.models.Conversations.dateCreated
import com.irq3.database.models.Users
import com.irq3.infrastucture.utils.DateUtil
import com.irq3.models.User
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import org.slf4j.LoggerFactory
import java.time.ZoneId

object ConversationsDao{
    private val logger = LoggerFactory.getLogger(ConversationsDao.javaClass)

    fun initConversation(person1: Long, person2: Long): Conversation{
          return transaction {
                val conv = Conversations.insert {
                    it[Conversations.idPerson1] = person1
                    it[Conversations.idPerson2] = person2
                    it[Conversations.dateCreated] = Clock.System.now()
                }
                val convId = conv[Conversations.id]

                Users.update({(Users.id eq person1) or (Users.id eq person2)}){
                    it[Users.currentConversion] = convId
                }
                logger.info("Conversation init with id {}",conv[Conversations.id] )
                 Conversation(conv[Conversations.id],
                     DateUtil.fixInstant(conv[dateCreated].toJavaInstant()),
                     conv[Conversations.idPerson1],
                     conv[Conversations.idPerson2])
            }
    }

    fun isConversationExpired(id : Long): Boolean{
        val time = transaction {
            Conversations.select(dateCreated).where {
                Conversations.id eq id
            }.singleOrNull()
        }
        if(time==null){
            return false
        }
        return time[dateCreated].toJavaInstant()
            .isBefore(
                Instant.now()!!
                    .minus(30, ChronoUnit.DAYS))
    }

    fun joinConversation(user1: User, user2: User){
        val id1 = requireNotNull(user1.id) { "User 1 ID cannot be null" }
        val id2 = requireNotNull(user2.id) { "User 2 ID cannot be null" }

        transaction {
            Users.update({ (Users.id inList listOf(id1,id2))}) {
                it[Users.isTalking] = true
            }

        }
    }
    fun getLastConversationByUserId(userId : Long) : Long?{
        val result = transaction {
            Conversations.select(Conversations.id)
                .where {
                    (Conversations.idPerson1 eq userId) or (Conversations.idPerson2 eq userId)
                }.lastOrNull()
        }
        return result?.get(Conversations.id)
    }

    fun getConversation(id : Long) : Conversation {
        return transaction {
            Conversations.selectAll().where{
                Conversations.id eq id

            }.map{ Conversation (it[Conversations.id],
                it[dateCreated].toJavaInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime(),
                it[Conversations.idPerson1], it[Conversations.idPerson2])}.first()
        }
    }

    fun disconnectConversation(conversation: Conversation)
    {
        transaction {
            Users.update({ (Users.id eq conversation.idPerson1) or (Users.id eq conversation.idPerson2)}){
                it[Users.isTalking] = false
                it[Users.currentConversion] = null
            }
        }
    }

    fun countConversations(): Long{
        return transaction {
            Users.selectAll().where { Users.isTalking eq true }.count()
        }
    }

    fun getConversationById(id: Long): Conversation{
        return transaction {
            Conversations.selectAll().where { Conversations.id eq id}.map {
                Conversations.map(it)
            }.first()
        }
    }
    fun getAllConversations(): List<Conversation>{
        return transaction {
            Conversations.selectAll().map { Conversations.map(it) }
        }
    }
    fun deleteById(id: Long){
        transaction {
            Conversations.deleteWhere{
                Conversations.id eq id
            }
        }
    }


}

