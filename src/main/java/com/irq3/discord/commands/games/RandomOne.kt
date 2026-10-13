package com.irq3.discord.commands.games

import com.irq3.core.DIManager
import com.irq3.discord.commands.Command
import com.irq3.discord.utils.Embed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import kotlin.random.Random

class RandomOne : Command(
    "random",
    "Losuje osobę, w razie sporów",
    false,
    false,
    true,
    false
) {

    override fun invoke(event: SlashCommandInteractionEvent) {
        val user = DIManager.userService.getUserDiscord(event.user.idLong)
        if(user==null){
            event.replyEmbeds(
                Embed("System", "Nie masz konta discord", Embed.errorColor).build()
            ).queue()
        }
        val conversationId = DIManager.conversationService.getConversationByUserId(user!!.id!!)
        if(conversationId==null){
            event.replyEmbeds(
                Embed("System", "Nie masz konta discord", Embed.errorColor).build()
            ).queue()
        }
        val conversation = DIManager.conversationService.getConversationById(conversationId!!)
        event.deferReply().queue()

        val usr = DIManager.userService.getUser(conversation.idPerson1)!!
        val usr2 = DIManager.userService.getUser(conversation.idPerson2)!!

        val (chosen, oponent) = if(Random.nextBoolean()){
            usr to usr2
        }else{
            usr2 to usr
        }

        DIManager.interactDM(chosen.idDiscord){
            it.openPrivateChannel().queue{ c->
                c.sendMessageEmbeds(Embed("System","Zostałeś wylosowany!", color=Embed.inDwellerColor).build()).queue()
            }
        }
        DIManager.interactDM(oponent.idDiscord) {
            it.openPrivateChannel().queue { c ->
                c.sendMessageEmbeds(Embed("System", "Twój oponent został wylosowany!", color=Embed.inDwellerColor).build()).queue()
            }
        }

    }
}