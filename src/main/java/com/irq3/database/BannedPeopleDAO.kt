package com.irq3.database

import com.irq3.database.models.BannedPeople
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object BannedPeopleDAO {

    fun banPerson(idDiscord: Long, reason: String?) {
        transaction {
            BannedPeople.insert {
                it[BannedPeople.idDiscord] = idDiscord
                it[BannedPeople.reason] = reason

            }
        }
    }

    fun unbanPerson(idDiscord: Long) {
        transaction {
            BannedPeople.deleteWhere {
                BannedPeople.idDiscord eq idDiscord
            }
        }
    }

    @JvmStatic
    fun isPersonBanned(idDiscord: Long): Pair<Boolean, String?> {
        return transaction {
            BannedPeople
                .select(BannedPeople.reason)
                .where { BannedPeople.idDiscord eq idDiscord }
                .firstOrNull()
                ?.let { true to it[BannedPeople.reason] }
                ?: (false to null)
        }
    }
}