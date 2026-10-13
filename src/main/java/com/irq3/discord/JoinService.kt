package com.irq3.discord

import com.irq3.core.DIManager
import com.irq3.discord.utils.Buttons
import com.irq3.discord.utils.Embed
import net.dv8tion.jda.api.components.actionrow.ActionRow
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction

class JoinService {

    suspend fun join(userId: Long, replyEmbeds:(MessageEmbed)-> ReplyCallbackAction){
        val service = DIManager.userService
        val user = service.getUserDiscord(userId)

        if(user!!.currentConversation!=null){
            replyEmbeds(Embed("System","Jesteś już w konwersacji!", color = Embed.warningColor).build()).queue()
            return
        }
        if(PeopleQueue.addPerson(user)){
            replyEmbeds(Embed("System","Dodaję cię do kolejki!",
                color = Embed.successColor).build()).addComponents(ActionRow.of(Buttons.leaveQueue())).queue()
            return
        }

        replyEmbeds(Embed("System","Jesteś już w kolejce!", color = Embed.errorColor).build()).queue()
    }

}