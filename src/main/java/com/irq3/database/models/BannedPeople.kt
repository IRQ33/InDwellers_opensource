package com.irq3.database.models

import com.irq3.models.BannedPerson
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object BannedPeople : Table("banned_people")  {
    var id = long("id").autoIncrement()
    var idDiscord = long("id_discord")
    var reason = text("reason").nullable()

    override val primaryKey = PrimaryKey(id)


    fun map(result : ResultRow): BannedPerson {
        return BannedPerson(
            id = result[id],
            idDiscord = result[idDiscord],
            reason = result[reason].orEmpty()
        )
    }
}