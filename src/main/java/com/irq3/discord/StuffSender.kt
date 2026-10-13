package com.irq3.discord

import com.irq3.core.services.ConversationService
import com.irq3.core.DIManager
import com.irq3.core.services.UserService
import com.irq3.discord.utils.Embed
import com.irq3.discord.utils.RulesMessage
import com.irq3.models.Conversation
import com.irq3.models.Message
import com.irq3.models.User
import net.dv8tion.jda.api.components.actionrow.ActionRow

class StuffSender(
    val userManager:UserService = DIManager.userService,
    val conservationService: ConversationService = DIManager.conversationService
){


    fun send(user: User, message: Message){
        val conversationId = user.currentConversation!!
        val conversation  = conservationService.getConversationById(conversationId)
        val userId = conversation.oponentId(user.id!!)
        val usr = userManager.getUser(userId)


        DIManager.interactDM(usr!!.idDiscord) { userToSend ->
            userToSend.openPrivateChannel().queue { c ->
                val slogan = if(user.isVip){
                    "✪ Dweller: "
                }else{
                    "Dweller: "
                }
                println(usr.isVip)
                println(usr.toString())

                c.sendMessage(slogan+ message.message).queue{
                    conservationService.setMessageDiscordId(message,it.idLong)
                }
            }
        }
    }

    fun sendWithReplying(user: User, message: Message, replyingMessage: Message){
        val conversationId = user.currentConversation!!
        val conversation  = conservationService.getConversationById(conversationId)
        val userId = conversation.oponentId(user.id!!)
        val usr = userManager.getUser(userId)
        val slogan = if(user.isVip){
            "Dweller: "
        }else{
            "Dweller: "
        }
        DIManager.interactDM(usr!!.idDiscord) { userToSend ->
            userToSend.openPrivateChannel().queue { c ->
               c.retrieveMessageById(replyingMessage.idMessage).queue {
                    it.reply(slogan+ message.message).queue{ msg->
                        conservationService.setMessageDiscordId(message,msg.idLong)
                    }
                }
            }
        }
    }

    fun sendReplying(user: User, conversation: Conversation){
        val userToSend = conversation.oponentId(user.id!!)
        val usr = userManager.getUser(userToSend)

        DIManager.interactDM(usr!!.idDiscord){
            it.openPrivateChannel().queue{ c->
                c.sendTyping().queue()
            }
        }
    }

    fun broadcastConversation(conversation: Conversation, embed: Embed){
        val users = listOf(userManager.getUser(conversation.idPerson1)!!,
            userManager.getUser(conversation.idPerson2)!!)

        users.forEach {
            DIManager.interactDM(it.idDiscord){ dm ->
                dm.openPrivateChannel().queue{ c->
                    c.sendMessageEmbeds(embed.build()).queue()
                }
            }
        }
    }
    
    fun sendWelcome(id: Long){
        DIManager.interactDM(id){ it ->
            it.openPrivateChannel().queue {
                it.sendMessageEmbeds(Embed("System",
                    "Dziękujemy za autoryzację w naszym systemie! Zachęcamy do dołączenia na nasz oficjalny" +
                            " [serwer discord](https://discord.gg/HHcNkQ56VM), aby korzystać z pełni możliwości inDwellers!",
                    color = Embed.inDwellerColor).build()).queue()
                it.sendMessageEmbeds(RulesMessage.getRules().first).addComponents(ActionRow.of(RulesMessage.getRules().second)).queue()
            }
        }
    }

    fun emit(embed: Embed){
        val people = DIManager.userDAO.getAllPeople()
        for (p in people){
            DIManager.interactDM(p.idDiscord){
                it.openPrivateChannel().queue { c ->
                    c.sendMessageEmbeds(embed.build()).queue()
                }
            }
        }
    }

    fun deleteConversation(conversationId: Long){
        val conversation = DIManager.conversationService.getConversationById(conversationId)

        val participants = listOf(
            DIManager.userService.getUser(conversation.idPerson1)!!,
            DIManager.userService.getUser(conversation.idPerson2)!!
        )

        participants.filter {
            it.currentConversation==conversationId
        }.forEach { user ->
            DIManager.interactDM(user.idDiscord){
                it.openPrivateChannel().queue{ c->
                    c.sendMessageEmbeds(Embed("System","Witaj, twoja obecna konwersacja została usunięta!").build()).queue()
                }
            }
        }
    }

}