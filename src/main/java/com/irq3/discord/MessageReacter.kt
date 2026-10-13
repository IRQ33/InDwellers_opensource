package com.irq3.discord

import com.irq3.core.DIManager
import com.irq3.core.services.UserService
import com.irq3.models.Message
import net.dv8tion.jda.api.entities.emoji.Emoji

class MessageReacter(
    val userService: UserService = DIManager.userService
) {


    fun react(message: Message, reaction: Emoji){
        val sender = userService.getUser(message.idUser)
        DiscordBot.interactUserDM(sender!!.idDiscord){
            it.openPrivateChannel().queue {  channel ->
                channel.addReactionById(message.idMessage, reaction).queue()
            }
        }
    }

    fun unReact(message: Message, reaction: Emoji){
        val sender = userService.getUser(message.idUser)
        DiscordBot.interactUserDM(sender!!.idDiscord){
            it.openPrivateChannel().queue {  channel ->
                channel.removeReactionById(message.idMessage, reaction).queue()
            }
        }
    }
}