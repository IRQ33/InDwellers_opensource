package com.irq3.infrastucture.jobs

import com.irq3.core.DIManager
import com.irq3.core.Event
import com.irq3.core.EventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.hours

class Cleaner {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)
    private var running = true
    private val  logger = LoggerFactory.getLogger(this.javaClass)
    fun start(){
        clean()
        scope.launch {
            while (running){
                clean()
                delay(24.hours)
            }
        }
    }
    fun stop(){
        running = false
    }

    private fun clean(){
        val conversations = DIManager.conversationService.getAllConversations()
        for(c in conversations){
            if(DIManager.conversationService.isConversationExpired(c.id)){
                EventBus.emit(Event.ConversationDeleted(c.id))
                DIManager.conversationService.deleteConversation(c.id)
                logger.info("Conversation with id {} was deleted",c.id)
            }
        }
    }
}