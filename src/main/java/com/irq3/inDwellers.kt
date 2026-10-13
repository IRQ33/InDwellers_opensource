package com.irq3

import com.irq3.core.EventBus
import com.irq3.database.DatabaseConnection
import com.irq3.core.BotScope
import com.irq3.discord.DiscordBot
import com.irq3.infrastucture.EnvController
import com.irq3.infrastucture.EventHandler
import com.irq3.infrastucture.jobs.Cleaner
import com.irq3.infrastucture.jobs.QueueWorking
import com.irq3.website.Website
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object INDWELLERS{
    val DEV = EnvController.getData("DEV")=="1"
    val DISCORD_GUILD_ID = EnvController.getData("DISCORD_GUILD").toLong()
}
fun main(): Unit = runBlocking{

    DatabaseConnection.init()
    val queue = QueueWorking()
    queue.start()

    launch {
        Website.init()
    }

    val bot = DiscordBot(EnvController.getData("token"))

    BotScope.launch {
        EventBus.collectEvents {
            EventHandler().handle(it)
        }
    }
    val cleaner = Cleaner()
    cleaner.start()
    Runtime.getRuntime().addShutdownHook(Thread{
        bot.shutDown()
        queue.stop()
        cleaner.stop()
    })

}