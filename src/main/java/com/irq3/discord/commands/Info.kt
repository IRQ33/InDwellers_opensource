package com.irq3.discord.commands

import com.irq3.core.DIManager
import com.irq3.discord.utils.Embed
import com.irq3.website.discord.DiscordUtils
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class Info : Command(
    "info",
    "Wyświetla informację o aktualnym rozmówcy.",
    false,
    false,
    true,
    true
) {

    override fun invoke(event: SlashCommandInteractionEvent) {
        val conversationManager = DIManager.conversationService
        val userManager = DIManager.userService

        val user = userManager.getUserDiscord(event.user.idLong)
        if(user==null){
            event.replyEmbeds(Embed(description = "Nie jesteś zalogowany!",color =Embed.errorColor).build()).queue()
            return
        }
        if(!user.isTalking){
            event.replyEmbeds(Embed(description = "Nie jesteś w konwersacji!",color =Embed.errorColor).build()).queue()
            return
        }
        val conversationId = conversationManager.getConversationByUserId(user.id!!)
        val conversation = conversationManager.getConversationById(conversationId!!)

        val oponentId = conversation.oponentId(user.id)

        val oponent = userManager.getUser(oponentId)!!
        val status = DiscordUtils.onlineUser(oponent.idDiscord,event.jda)

        if(status!=null) {
            val statusName = when (status) {
                OnlineStatus.ONLINE -> "jest online"
                OnlineStatus.DO_NOT_DISTURB -> "jest online"
                OnlineStatus.IDLE -> "zaraz wróci"
                OnlineStatus.OFFLINE -> "jest offline"
                else -> {}
            }
            event.replyEmbeds(
                Embed(description = "inDweller $statusName", color = Embed.infoColor).build()
            ).setEphemeral(true).queue()
            return
        }
        event.replyEmbeds(
            Embed(description = "Nie mamy informacji o tym inDwellerze :<", color = Embed.warningColor).build()
        ).setEphemeral(true).queue()
    }
}