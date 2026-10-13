package com.irq3.database

import com.irq3.models.User
import com.irq3.database.models.Users
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.slf4j.LoggerFactory

object UserDAO {
    private val logger = LoggerFactory.getLogger(UserDAO.javaClass)

    fun initPerson(id: Long) {
        transaction {
            Users.insert {
                it[Users.discordId] = id
                it[Users.isTalking] = false
            }
            logger.info("New person is created, with id {}", id)
        }

    }

    fun getPersonById(id: Long, messageType: ID_TYPE): User? = transaction {
        val col = if (messageType == ID_TYPE.ID) Users.id else Users.discordId
        Users.selectAll().where { col eq id }.map { Users.map(it) }.firstOrNull()
    }

    fun getAllPeople(): Array<User> = transaction {
        Users.selectAll().map { Users.map(it) }.toTypedArray()
    }


    fun removePerson(id: Long, idType: ID_TYPE) {
        if (idType == ID_TYPE.ID) {
            transaction {
                Users.deleteWhere { Users.id eq id }
            }
        } else {
            transaction {
                Users.deleteWhere { Users.discordId eq id }
            }
        }
    }

    fun giveVip(id: Long){
        transaction {
            Users.update({ Users.id eq id}){
                it[vipBuyDate] = Clock.System.now()
            }
        }
    }


}