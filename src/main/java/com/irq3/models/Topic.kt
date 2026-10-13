package com.irq3.models

import java.time.OffsetDateTime

data class Topic(
    val id: Long?,
    val topic: String,
    val dateCreated: OffsetDateTime,
    val adminId:Long)

