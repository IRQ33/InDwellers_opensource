package com.irq3.discord.commands

import com.irq3.discord.utils.Embed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class Help : Command(
    "help",
    "Wyświetla ważne komendy",
    false,
    false,
    true,
    true
){

    override fun invoke(event: SlashCommandInteractionEvent) {
        event.replyEmbeds(
            Embed("System",
                "## Najważniejsze komendy \n" +
                        "`/connect` - Dodaje cię do kolejki. \n" +
                        "`/leave` - Opuszcza aktualną konwersację. \n" +
                        "`/report` - Zgłaszą aktualną lub ostatnią konwersację. \n" +
                        "`/info` - Wyświetla informacje o użytkowniku. \n" +
                        "`/topic` - Wyświetla losowy temat do rozmowy. \n" +
                        "`/discord` - Daje zaproszenie do głównego serwera discord inDwellers. \n",
                color = Embed.successColor)
                .build()
        ).setEphemeral(true).queue()
    }
}