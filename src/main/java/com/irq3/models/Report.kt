package com.irq3.models

data class Report(
    var id: Long?,
    val reporting: Long,
    val reported: Long,
    val chatId: Long,
    val reason: String,
    val approved: Boolean? = null,
    val admin: Long? = null
)
