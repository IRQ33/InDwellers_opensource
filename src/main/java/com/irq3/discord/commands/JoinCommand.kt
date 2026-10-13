package com.irq3.discord.commands

import com.irq3.core.DIManager
import com.irq3.discord.JoinService
import com.irq3.discord.utils.RulesMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.components.actionrow.ActionRow
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class JoinCommand : Command(
    "connect",
    "Dołącza cię do kolejki w poszukiwaniu indwellersów!",
    false,
    false,
    true,
    true
){


    override fun invoke(event: SlashCommandInteractionEvent) {
        val user = DIManager.userService.getUserDiscord(event.user.idLong)
        if(user==null){
            event.replyEmbeds(RulesMessage.getRules().first).addComponents(ActionRow.of(RulesMessage.getRules().second)).queue()
            return
        }
        val joinService = JoinService()
        CoroutineScope(Dispatchers.IO).launch  {
            joinService.join(event.user.idLong){
                    embed-> event.replyEmbeds(embed)
            }
        }

    }
}