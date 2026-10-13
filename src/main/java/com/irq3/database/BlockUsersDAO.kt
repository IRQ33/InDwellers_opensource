package com.irq3.database

import com.irq3.database.models.BlockedUsers
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object BlockUsersDAO {

    fun blockUser(userId:Long, blockedId:Long){
        transaction {
            val alreadyBlocked = BlockedUsers
                .selectAll().where { (BlockedUsers.userId eq userId) and (BlockedUsers.blockedId eq blockedId) }.any()
           if(!alreadyBlocked){
               BlockedUsers.insert {
                   it[BlockedUsers.userId] = userId
                   it[BlockedUsers.blockedId] = blockedId
               }
           }
        }
    }

    fun isBlocked(userId: Long, blockedId: Long): Boolean = transaction{
        val result = BlockedUsers.selectAll().where {(BlockedUsers.userId eq userId) and (BlockedUsers.blockedId eq blockedId) }.firstOrNull()
        result !=null
    }

    fun unBlock(id: Long) {
        transaction {
            val last = BlockedUsers
                .selectAll()
                .where { BlockedUsers.userId eq id }
                .orderBy(BlockedUsers.createdAt, SortOrder.DESC)
                .limit(1)
                .firstOrNull()

            if (last != null) {
                BlockedUsers.deleteWhere {
                    (BlockedUsers.userId eq id) and (BlockedUsers.blockedId eq last[BlockedUsers.blockedId])
                }
            }
        }
    }
}