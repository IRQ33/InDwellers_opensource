package com.irq3.infrastucture

import com.irq3.core.DIManager
import com.irq3.core.Event

class EventHandler {

     fun handle(e: Event) = when(e){

        is Event.EmojiAdded -> {
            handleEmojiAdded(e)
        }
        is Event.EmojiRemoved ->{
            handleEmojiRemoved(e)
        }
        is Event.MessageSent->{
            handleMessageSent(e)
        }
         is Event.BugReportCreated -> {
            handleBugReport(e)
         }
         is Event.GreetingMessageWebhook -> {
             handleUserWebhookGreetings(e)
         }
         is Event.ConversationDeleted -> {
            handleConversationDelete(e)
         }
     }


    private fun handleEmojiAdded(e: Event.EmojiAdded) {
        DIManager.conversationService.giverReaction(e)
    }
    private fun handleEmojiRemoved(e: Event.EmojiRemoved) {
        DIManager.conversationService.removeReaction(e)

    }
    private fun handleMessageSent(e: Event.MessageSent) {
        DIManager.conversationService.sendMessage(
            e.idMessage,e.message,e.usr
        )
    }
    private fun handleBugReport(e: Event.BugReportCreated){
        DIManager.bugReportsManager.createReport(e.report,e.userId)
    }
    private fun handleUserWebhookGreetings(e: Event.GreetingMessageWebhook){
        DIManager.stuffSender.sendWelcome(e.webhook.event.data.user.id.toLong())
    }

    private fun handleConversationDelete(e: Event.ConversationDeleted){
        DIManager.stuffSender.deleteConversation(e.conversationId)
    }
}