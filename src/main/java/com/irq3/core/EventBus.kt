package com.irq3.core

import com.irq3.models.Message
import com.irq3.models.User
import com.irq3.website.discord.UserWebhook
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import net.dv8tion.jda.api.entities.emoji.Emoji

object EventBus {
    private val bus = MutableSharedFlow<Event>(replay = 32)
    val events = bus.asSharedFlow()

    fun emit(e: Event){
       val e = bus.tryEmit(e)
        if(!e){
            throw RuntimeException("Didnt emitted :/")
        }
    }
    suspend fun collectEvents(action: suspend (Event) -> Unit) {
        events.collect { event ->
            action(event)
        }
    }
}

sealed class Event{
    data class EmojiAdded(val message: Message, val emoji: Emoji, val userID: Long) : Event()
    data class EmojiRemoved(val message: Message, val emoji: Emoji, val userID: Long) : Event()
    data class MessageSent(val usr: User, val message: String, val idMessage: Long) : Event()
    data class BugReportCreated(val report: String, val userId: Long): Event()
    data class GreetingMessageWebhook(val webhook: UserWebhook): Event()
    data class ConversationDeleted(val conversationId: Long): Event()
}