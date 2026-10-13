package com.irq3.discord.utils

import net.dv8tion.jda.api.components.buttons.Button
import net.dv8tion.jda.api.entities.MessageEmbed

object RulesMessage {

    fun getRules(): Pair<MessageEmbed, Button> {
        val text = """            
            1. Musisz mieć ukończone 16 lat.
            2. Zakaz toksycznego zachowania (obrażanie, prowokacje, spam).
            3. Wiadomości na serwerze są archiwizowane przez 30 dni.
            4. Musisz zaakceptować naszą [politykę prywatności](https://indwellers.net/policy).
            Korzystając z bota akceptujesz powyższe zasady.
        """.trimIndent()

        val embed = Embed(
            title = "Regulamin inDwellers",
            description = text,
            color = Embed.successColor
        ).build()

        val acceptButton = Buttons.acceptRules()

        return Pair(embed, acceptButton)
    }
}