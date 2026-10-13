package com.irq3.discord

import com.irq3.discord.commands.CommandManager
import com.irq3.discord.commands.actions.ReactionAction
import com.irq3.discord.commands.actions.ButtonsAction
import com.irq3.discord.commands.actions.TypingAction
import com.irq3.discord.server.ServerManager
import com.irq3.discord.utils.Embed
import com.irq3.website.BOT_STATUS
import com.irq3.website.Website
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.cache.CacheFlag
import net.dv8tion.jda.internal.utils.JDALogger
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

class DiscordBot {

    private val logger = LoggerFactory.getLogger(this.javaClass)
    companion object{
        lateinit var jda : JDA

        fun interactUserDM(idDiscord: Long, action: IUserDM){
            jda.retrieveUserById(idDiscord).queue { userToSend ->
                action.invoke(userToSend)
            }
        }

        fun sendOnServerChannel(idChannel: Long, embed: Embed){
            jda.getGuildById(getGuild())!!
                .getChannelById(TextChannel::class.java,idChannel)
                ?.sendMessageEmbeds(embed.build())!!.queue()

        }

        fun getGuild(): Long{
            return 1487622348034277666
        }
    }

    constructor(token: String){
        JDALogger.setFallbackLoggerEnabled(false)
        jda = JDABuilder.create(GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT,
            GatewayIntent.GUILD_PRESENCES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS,
            GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.GUILD_EXPRESSIONS, GatewayIntent.DIRECT_MESSAGE_TYPING,
            GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.GUILD_VOICE_STATES)
            .setToken(token)
            .enableCache(CacheFlag.ACTIVITY, CacheFlag.EMOJI)
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .setChunkingFilter(ChunkingFilter.ALL)
            .setStatus(OnlineStatus.ONLINE)
            .setActivity(Activity.listening("Napisz do mnie!"))
            .build()

        initEventHandlers(jda)
        logger.info("Bot is running")
        Website.status = BOT_STATUS.ONLINE
    }

    private fun initEventHandlers(jda: JDA) {
        jda.addEventListener(CommandManager(jda),
            DMManager(jda), ButtonsAction(), ReactionAction(), TypingAction(),
            ServerManager())
    }
    fun shutDown(){
        jda.shutdown()
        if(!jda.awaitShutdown(10, TimeUnit.SECONDS)){
            jda.shutdownNow()
        }
    }

}

fun interface IUserDM {
    fun invoke(user: User)
}