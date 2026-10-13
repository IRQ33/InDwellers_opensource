package com.irq3.website.discord

import com.google.gson.annotations.SerializedName

data class UserWebhook(
    val version: Int,
    @SerializedName("application_id")
    val applicationId: String,
    val type: Int,
    val event: DiscordEvent
)

data class DiscordEvent(
    val type: String,
    val timestamp: String,
    val data: DiscordEventData
)

data class DiscordEventData(
    @SerializedName("integration_type")
    val integrationType: Int,
    val scopes: List<String>,
    val user: DiscordUser
)

data class DiscordUser(
    val id: String,
    val username: String? = null,
    @SerializedName("global_name")
    val globalName: String? = null,
    val avatar: String? = null
)