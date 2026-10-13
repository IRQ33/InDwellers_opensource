package com.irq3.discord.commands.actions

import com.irq3.core.DIManager
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.events.user.UserTypingEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class TypingAction : ListenerAdapter() {

    override fun onUserTyping(event: UserTypingEvent) {
        if(event.isFromType(ChannelType.PRIVATE)){
            val user = DIManager.userService.getUserDiscord(event.user.idLong) ?: return
            val conversationId = DIManager.conversationService.getConversationByUserId(user.id!!) ?:return
            val conversation = DIManager.conversationService.getConversationById(conversationId)
            DIManager.stuffSender.sendReplying(user,conversation)
        }
    }
}