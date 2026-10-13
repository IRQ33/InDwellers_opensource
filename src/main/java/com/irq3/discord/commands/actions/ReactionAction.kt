package com.irq3.discord.commands.actions

import com.irq3.core.Event
import com.irq3.core.EventBus
import com.irq3.core.DIManager
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEmojiEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.LoggerFactory

class ReactionAction : ListenerAdapter() {

    private val  logger = LoggerFactory.getLogger(this.javaClass)


    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {
        if (event.userIdLong == event.jda.selfUser.idLong) return
        val conversationService = DIManager.conversationService

        val message = conversationService.getMessageByDiscordId(event.messageIdLong)
        val conversation = conversationService.getConversationById(message.idConversation)
        val user = DIManager.userService.getUser(conversation.oponentId(message.idUser))
        val event = Event.EmojiAdded(message,event.emoji,user!!.id!!)
        logger.debug("We made Event {}", event.toString())
        EventBus.emit(event)
    }

    override fun onMessageReactionRemoveEmoji(event: MessageReactionRemoveEmojiEvent) {
        val conversationService = DIManager.conversationService
        val message = conversationService.getMessageByDiscordId(event.messageIdLong)
        val user = DIManager.userService.getUser(event.channel.asPrivateChannel().user!!.idLong)

        EventBus.emit(Event.EmojiRemoved(message,event.emoji,user!!.id!!))
     }
}