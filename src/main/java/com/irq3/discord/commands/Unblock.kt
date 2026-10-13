package com.irq3.discord.commands

import com.irq3.core.DIManager
import com.irq3.discord.utils.Embed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class Unblock: Command(
    "unblock",
    "Odblokowuje poprzednio zablokowaną osobę",
    false,
    false,
    true,
    true
) {
    override fun invoke(event: SlashCommandInteractionEvent) {
        val user = DIManager.userService.getUserDiscord(event.user.idLong)
        if(user==null){
            event.replyEmbeds(Embed("System","Nie masz konta!", color = Embed.mutedColor).sendAsList()).queue()
            return
        }
        DIManager.userService.unBlockPerson(user.id!!)

        event.replyEmbeds(
            Embed("System","Odblokowałeś/aś ostatnio zablokowaną osobę!", color = Embed.successColor).build()
        ).setEphemeral(true).queue()
    }
}