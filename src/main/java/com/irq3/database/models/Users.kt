package com.irq3.database.models

import com.irq3.discord.utils.LimitArray
import com.irq3.infrastucture.utils.DateUtil
import com.irq3.models.User
import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.time.OffsetDateTime

object Users : Table("users"){
    val id = long("id")
    val discordId = long("id_discord")
    val isTalking = bool("is_talking")
    val currentConversion = long("current_conversation").nullable()
    val vipBuyDate = timestamp("vip_buy_date").nullable()

    override val primaryKey = PrimaryKey(id)


    fun map(result : ResultRow): User {
        return User(
            id = result[id], idDiscord = result[discordId],
            isTalking = result[isTalking],
            currentConversation = result[currentConversion],
            vipDate = result[vipBuyDate]?.toJavaInstant()?.let { DateUtil.fixInstant(it) }
        )
    }
}
