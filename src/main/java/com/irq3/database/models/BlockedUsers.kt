package com.irq3.database.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object BlockedUsers : Table("blocked_users") {

    val userId = long("user_id").references(Users.id)
    val blockedId = long("blocked_user_id").references(Users.id)
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)


    override val primaryKey: PrimaryKey = PrimaryKey(userId,blockedId)
}