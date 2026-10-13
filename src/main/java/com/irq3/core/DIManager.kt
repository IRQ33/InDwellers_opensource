package com.irq3.core

import com.irq3.core.services.BugReportServices
import com.irq3.core.services.ConversationService
import com.irq3.core.services.ReportServiceImpl
import com.irq3.core.services.TopicService
import com.irq3.core.services.UserService
import com.irq3.database.*
import com.irq3.discord.DiscordBot
import com.irq3.discord.IUserDM
import com.irq3.discord.StuffSender
import com.irq3.discord.utils.Embed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object DIManager {
    val userDAO: UserDAO = UserDAO
    val conversationDAO: ConversationsDao = ConversationsDao
    val messageDAO: MessageDao = MessageDao
    val reportDAO: ReportDAO = ReportDAO
    val bannedPeopleDAO: BannedPeopleDAO = BannedPeopleDAO
    val blockedUserDAO = BlockUsersDAO
    val bugReportDAO = BugReportsDAO
    val topicsDAO = TopicDAO

    val conversationService: ConversationService by lazy { ConversationService(userDAO, conversationDAO, messageDAO) }
    val userService: UserService by lazy { UserService(userDAO, bannedPeopleDAO, conversationDAO, conversationService, blockedUserDAO) }
    val reportService: ReportServiceImpl by lazy { ReportServiceImpl(conversationService, userService, reportDAO) }
    val bugReportsManager: BugReportServices by lazy { BugReportServices(bugReportDAO) }
    val stuffSender: StuffSender by lazy { StuffSender() }
    val topicsService: TopicService by lazy { TopicService(topicsDAO) }

    val interactDM: (Long, IUserDM) -> Unit = DiscordBot::interactUserDM
    val sendOnGuildChannel: (Long, Embed) -> Unit = DiscordBot::sendOnServerChannel
}
object BotScope : CoroutineScope by CoroutineScope(Dispatchers.IO + SupervisorJob())
