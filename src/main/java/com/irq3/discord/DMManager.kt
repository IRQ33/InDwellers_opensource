package com.irq3.discord

import com.irq3.core.DIManager
import com.irq3.core.Event
import com.irq3.core.EventBus
import com.irq3.discord.utils.Embed
import com.irq3.discord.utils.RulesMessage
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.components.actionrow.ActionRow
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class DMManager: ListenerAdapter {

     private val jda: JDA
     val regex ="""((([A-Za-z]{3,9}:(?:\/\/)?)(?:[-;:&=\+\$,\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\+\$,\w]+@)[A-Za-z0-9.-]+)((?:\/[\+~%\/.\w-_]*)?\??(?:[-\+=&;%@.\w_]*)#?(?:[\w]*))?)""".toRegex()

    constructor(jda : JDA){
        this.jda = jda
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if(event.channelType== ChannelType.PRIVATE && !event.author.isBot){
            val service = DIManager.userService
            val conservationService = DIManager.conversationService
            var user = service.getUserDiscord(event.author.idLong)

            if(user == null){
                event.message.replyEmbeds(RulesMessage.getRules().first)
                    .addComponents(ActionRow.of(RulesMessage.getRules().second)).queue()
                user = DIManager.userService.getUserDiscord(event.author.idLong)
            }
            require(user!=null){
                throw RuntimeException("Error with creating user!")
            }
            if(user.isTalking ){
                if(isUrl(event.message.contentRaw)){
                    DiscordBot.interactUserDM(event.author.idLong){ it ->
                        it.openPrivateChannel().queue{
                            it.sendMessageEmbeds(Embed("System","Nie możesz wysyłać linków!", color = Embed.errorColor).build()).queue()
                        }
                    }
                    return
                }
                if(event.message.attachments.isNotEmpty()){
                    DiscordBot.interactUserDM(event.author.idLong){ it ->
                        it.openPrivateChannel().queue{
                            it.sendMessageEmbeds(Embed("System","Nie możesz wysyłać zdjęć!", color = Embed.errorColor).build()).queue()
                        }
                    }
                    return
                }
                val message = event.message.referencedMessage
                if(message!=null){
                    val myMessage = DIManager.conversationService.getMessageByDiscordId(message.idLong)
                    conservationService.sendReplyingMessage(event.message.idLong,event.message.contentRaw,user,myMessage)
                    return
                }
                EventBus.emit(Event.MessageSent(
                    user, event.message.contentRaw,
                    event.message.idLong
                ))
            }
        }
    }
    fun isUrl(s: String): Boolean {
        return regex.containsMatchIn(s)
    }
}