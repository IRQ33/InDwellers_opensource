package com.irq3.discord.utils

import com.irq3.discord.DiscordBot
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import java.awt.Color

class Embed(val title: String="System",
            val description: String, val color: Color?= inDwellerColor,
            val thumbnail: String? = null,
            val image: String? = null) {

    fun build(): MessageEmbed{
        return EmbedBuilder().setColor(color).setTitle(title).setDescription(description)
            .setThumbnail(thumbnail)
            .setImage(image)
            .setFooter("inDwellers", DiscordBot.jda.selfUser.avatarUrl)
            .setTimestamp(Clock.System.now().toJavaInstant())
            .build()
    }
    fun sendAsList(): List<MessageEmbed> {
        return listOf(build())
    }
    companion object {
        val inDwellerColor = Color(59, 130, 246)

        val successColor = Color(74, 222, 128)
        val warningColor = Color(250, 204, 21)
        val errorColor = Color(239, 68, 68)
        val criticalColor = Color(185, 28, 28)

        val infoColor = Color(96, 165, 250)
        val mutedColor = Color(71, 85, 105)

        const val hr = "---"
    }
}