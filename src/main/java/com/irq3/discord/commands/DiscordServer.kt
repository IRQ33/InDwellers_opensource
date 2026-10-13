package com.irq3.discord.commands

import com.irq3.discord.utils.Embed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class DiscordServer : Command(
    "discord",
    "Daje zaproszenie do oficjalnego serwera discord.",
    false,
    false,
    true,
    true
) {
    override fun invoke(event: SlashCommandInteractionEvent) {
        event.replyEmbeds(
            Embed("System","Główny serwer [discord](https://discord.gg/HHcNkQ56VM)",color = Embed.inDwellerColor).build()
        ).queue()
    }
}