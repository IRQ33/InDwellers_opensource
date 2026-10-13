package com.irq3.core.services

import com.irq3.database.*
import com.irq3.models.User
import org.slf4j.LoggerFactory

class UserService(
    val userManager: UserDAO,
    val banManager: BannedPeopleDAO,
    val conservationManager: ConversationsDao,
    val conversationService: ConversationService,
    val blockManager: BlockUsersDAO
    ){

    private val  logger = LoggerFactory.getLogger(this.javaClass)

    fun registerUser(id: Long): RegisterResult{
        val ban = banManager.isPersonBanned(id)
        if(ban.first){
            return RegisterResult.UserBanned(ban.second!!)
        }

        val usr = try {
            userManager.getPersonById(id, ID_TYPE.DISCORD)
        } catch (e: Exception) {
            logger.error(e.message)
            throw e
        }
        if(usr==null){
            userManager.initPerson(id)
        }

        return RegisterResult.Success(userManager.getPersonById(id, ID_TYPE.DISCORD)!!)
    }

    fun getUserDiscord(idDiscord: Long): User? {
        val user = userManager.getPersonById(idDiscord, ID_TYPE.DISCORD)
        return user
    }
    fun getUser(id: Long): User? {
        val user = userManager.getPersonById(id, ID_TYPE.ID)
        return user
    }

    fun banPerson(idDiscord: Long, reason: String?, idAdmin: Long) {
        banManager.banPerson(idDiscord,reason)
        conversationService.leave(idDiscord)
        userManager.removePerson(idDiscord, ID_TYPE.DISCORD)
        logger.info("{} banned a person with id {}, for {}",idAdmin,idDiscord,reason.orEmpty())
    }

    fun unBanPerson(idDiscord: Long){
        banManager.unbanPerson(idDiscord)
    }

    fun blockPerson(id: Long){
        val conservationId = conservationManager.getLastConversationByUserId(id) ?: return
        val conservation = conservationManager.getConversationById(conservationId)
        val userId = conservation.oponentId(id)
        blockManager.blockUser(id,userId)
    }

    fun unBlockPerson(id: Long) {
        blockManager.unBlock(id)
    }
    fun giveVip(id: Long){
        userManager.giveVip(id)
    }
}

sealed class RegisterResult{
    data class Success(val user: User) : RegisterResult()
    data class UserBanned(val reason: String) : RegisterResult()
    data class Error(val message: String) : RegisterResult()
}