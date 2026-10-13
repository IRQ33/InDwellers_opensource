package com.irq3.models

import java.time.OffsetDateTime

data class Message(
    val id: Long? = null,
    val idUser: Long,
    var message: String,
    val dateCreated: OffsetDateTime,
    val idMessage: Long,
    val idConversation: Long,
    var idDiscordMessage: Long?=null,
    ){


    override fun toString(): String {
        return "Message(id=$id, idUser=$idUser, dateCreated=$dateCreated, idConversation=$idConversation, idDiscordMessage=$idDiscordMessage)"
    }


}
