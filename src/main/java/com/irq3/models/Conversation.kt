package com.irq3.models

import java.time.OffsetDateTime

data class Conversation(val id: Long,
                        val dateCreated: OffsetDateTime,
                        val idPerson1: Long,
                        val idPerson2: Long) {


    fun oponentId(userId: Long): Long{
        return if (userId==idPerson2){
            idPerson1
        }else{
            idPerson2
        }
    }
}