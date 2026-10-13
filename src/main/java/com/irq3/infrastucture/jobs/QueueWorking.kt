package com.irq3.infrastucture.jobs

import com.irq3.core.services.ConversationService
import com.irq3.discord.PeopleQueue
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class QueueWorking {
    private val manager = ConversationService()
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)
    private val  logger = LoggerFactory.getLogger(this.javaClass)
    val MAX_TRIES=50

    fun start(workerCount: Int = 1){
        logger.info("Queue started working")
        logger.info("Queue started with $workerCount workers")
        repeat(workerCount) { id ->
            scope.launch {
                while (true) {
                    try {
                        if(PeopleQueue.userList.size<2){
                            delay(500.milliseconds)
                            yield()
                            continue
                        }
                        var i = 0
                        var connected = false
                        while (i<=MAX_TRIES && !connected){
                            connected = manager.connectPeople()
                            i++
                        }
                        if(!connected){
                            delay(1.seconds)
                        }

                        yield()
                    } catch (e: Exception) {
                        logger.error("Worker $id error", e)
                        delay(1000.milliseconds)
                    }
                }
            }
        }
    }

    fun stop(){
        job.cancel()
        PeopleQueue.stop()
    }
}