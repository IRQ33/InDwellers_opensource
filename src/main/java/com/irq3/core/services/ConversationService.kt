package com.irq3.core.services

import com.irq3.core.DIManager
import com.irq3.core.Event
import com.irq3.core.security.MessageEncrypter
import com.irq3.database.*
import com.irq3.discord.DiscordBot
import com.irq3.discord.MessageReacter
import com.irq3.discord.PeopleQueue
import com.irq3.discord.utils.Buttons
import com.irq3.discord.utils.Embed
import com.irq3.models.Conversation
import com.irq3.models.Message
import com.irq3.models.User
import com.irq3.website.discord.DiscordUtils
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.components.actionrow.ActionRow
import org.slf4j.LoggerFactory
import java.time.OffsetDateTime

class ConversationService(val userManager: UserDAO = UserDAO,
                          val conversationManager: ConversationsDao = ConversationsDao,
                          val messageManager : MessageDao = MessageDao
)  {
    private val  logger = LoggerFactory.getLogger(this.javaClass)

    suspend fun connectPeople() : Boolean{
        val people = PeopleQueue.randomPeople()

        if(people.size<2) return false
        val conversation = conversationManager.initConversation(people[0].id!!,people[1].id!!)
        ConversationsDao.joinConversation(people[0],people[1])
        people[0].isTalking = true
        people[1].isTalking = true

        for (person in people){
            val user = DIManager.userService.getUser(conversation.oponentId(person.id!!))
            val status = DiscordUtils.onlineUser(user!!.idDiscord, DiscordBot.jda)
            if(status!=null){
                val statusName = when(status){
                    OnlineStatus.ONLINE->"jest online"
                    OnlineStatus.DO_NOT_DISTURB->"jest online"
                    OnlineStatus.IDLE -> "zaraz wróci"
                    OnlineStatus.OFFLINE -> "jest offline"
                    else -> {}
                }

                DiscordBot.interactUserDM(person.idDiscord) { user ->
                    user.openPrivateChannel().queue{ c->
                        c.sendMessageEmbeds(Embed(title = "System",
                            description = "Znaleźliśmy dwellersa, który $statusName!, przywitaj się!"
                        ).build()).queue()
                    }
                }
            }else{
                DiscordBot.interactUserDM(person.idDiscord) { user ->
                    user.openPrivateChannel().queue{ c->
                        c.sendMessageEmbeds(Embed(title = "System",
                            description = "Znaleźliśmy dwellersa!, przywitaj się!"
                        ).build()).queue()
                    }
                }
            }
        }
        return true
    }
    fun countConversations(): Long{
        return ConversationsDao.countConversations()
    }

    fun getConversationByUserId(userId: Long): Long? {
        return conversationManager.getLastConversationByUserId(userId)
    }

    fun sendMessage(idLong: Long, contentRaw: String, user: User) {
        val message =  makeMessage(idLong,contentRaw,user)
        DIManager.stuffSender.send(user,message)
    }
    fun sendReplyingMessage(idLong: Long, contentRaw: String, user: User, messageReplying: Message){
        val message =  makeMessage(idLong,contentRaw,user)
        DIManager.stuffSender.sendWithReplying(user,message,messageReplying)
    }

    fun makeMessage(idLong: Long, contentRaw: String, user: User): Message{
        val messageText = MessageEncrypter.encrypt(contentRaw)
        val message = messageManager.initMessage(idMessage = idLong, message = messageText,
            dateCreated = OffsetDateTime.now(), usr = user, idConversation = getConversationByUserId(user.id!!)!!
        )
        logger.debug("Message is {}",message.toString())
        message.message = contentRaw
        return message
    }
    fun leave(discordId: Long){
        val user = userManager.getPersonById(discordId, ID_TYPE.DISCORD)
        if(user?.currentConversation==null){
            return
        }
        val conv = leaveConservation(user.currentConversation)
        val personId = userManager.getPersonById(discordId, ID_TYPE.DISCORD) ?: throw RuntimeException("Null person left...")
        val person = if(conv.idPerson2==personId.id){
            userManager.getPersonById(conv.idPerson1, ID_TYPE.ID)!!.idDiscord
            }else{
            userManager.getPersonById(conv.idPerson2, ID_TYPE.ID)!!.idDiscord
        }

        DiscordBot.interactUserDM(person){ userToSend->
            userToSend.openPrivateChannel().queue { c ->
                c.sendMessageEmbeds(listOf(Embed(title = "System",
                    description = "Dwellers wyszedł :/", color = Embed.warningColor
                ).build()))
                    .setComponents(ActionRow.of(
                        Buttons.nextConversation()
                    )).queue()
            }
        }
    }

    fun leaveConservation(conservationId: Long): Conversation {
        val conversation  = ConversationsDao.getConversation(conservationId)
        ConversationsDao.disconnectConversation(conversation)
        return conversation
    }
    fun getConversationById(id: Long): Conversation{
        return ConversationsDao.getConversationById(id)
    }

    fun getMessageByDiscordId(idDiscord: Long): Message{
        return messageManager.getMessageByDiscordSentId(idDiscord) ?: throw RuntimeException("Didnt find message! with id $idDiscord")
    }

    fun setMessageDiscordId(msg: Message, id: Long){
        MessageDao.setDiscordId(msg,id)
    }

    fun giverReaction(event: Event.EmojiAdded){
        val reacter = MessageReacter()
        logger.debug("We made new emote {} for message {}",event.emoji, event.message )
        reacter.react(event.message, event.emoji)
    }

    fun removeReaction(event: Event.EmojiRemoved){
        val reacter = MessageReacter()
        logger.debug("We deleted emote {} for message {}",event.emoji, event.message )
        reacter.unReact(event.message, event.emoji)
    }

    fun isBlocked(userId: Long, blockId: Long): Boolean{
        return BlockUsersDAO.isBlocked(userId,blockId)
    }

    fun getAllConversations(): List<Conversation>{
        return conversationManager.getAllConversations()
    }

    fun deleteConversation(id: Long){

        conversationManager.deleteById(id)
    }

    fun isConversationExpired(id : Long): Boolean{
       return conversationManager.isConversationExpired(id)
    }
}