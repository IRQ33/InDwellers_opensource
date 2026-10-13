package com.irq3.discord.commands

import com.irq3.core.DIManager
import com.irq3.discord.utils.Embed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class Topic : Command(
    "topic",
    "Proponuje temat do rozmowy.",
    false,
    false,
    true,
    false) {

    override fun invoke(event: SlashCommandInteractionEvent) {
        val usr = DIManager.userService.getUserDiscord(event.user.idLong)
        if(usr==null){
            event.replyEmbeds(Embed(description = "Nie masz konta!",color = Embed.errorColor).build()).queue()
            return
        }
        if(!usr.isTalking){
            event.replyEmbeds(Embed(description = "Nie jesteś w rozmowie!",color = Embed.errorColor).build()).queue()
            return
        }
        val conversationId = DIManager.conversationService.getConversationByUserId(usr.id!!)
        val conversation = DIManager.conversationService.getConversationById(conversationId!!)
        val topic = DIManager.topicsService.getRandomTopic()
        event.deferReply().queue()
        DIManager.stuffSender.broadcastConversation(conversation, Embed("System","Proponowany temat rozmowy to: ${topic.topic}"))
    }
}