package com.irq3.discord.commands

import com.irq3.core.DIManager
import com.irq3.discord.utils.Embed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class Block : Command(
    "block",
    "Blokuje osobę z poprzedniej konwersacji",
    false,
    false,
    true,
    false
) {

    override fun invoke(event: SlashCommandInteractionEvent) {
        val user = DIManager.userService.getUserDiscord(event.user.idLong)
        if(user==null){
            event.replyEmbeds(Embed("System","Nie masz konta!", color = Embed.mutedColor).build()).queue()
            return
        }
        DIManager.userService.blockPerson(user.id!!)
        event.replyEmbeds(
            Embed("System","Zablokowałeś/aś osobę z ostatniej konwersacji!", color = Embed.successColor).build()
        ).setEphemeral(true).queue()
    }
}