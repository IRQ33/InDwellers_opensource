package com.irq3.core.services

import com.irq3.core.security.MessageEncrypter
import com.irq3.database.MessageDao
import com.irq3.database.ReportDAO
import com.irq3.models.Conversation
import com.irq3.models.Message
import com.irq3.models.Report

class ReportServiceImpl(
    val conversationManager: ConversationService,
    val userService: UserService,
    val reportManager: ReportDAO) {


    fun report(id: Long, reason: String): Long {
        val conversation = conversationManager.getConversationByUserId(id) ?: throw RuntimeException("No conversation")
        return reportConversation(conversationManager.getConversationById(conversation),id,reason)
    }

    fun reportConversation(conservation: Conversation, reporting: Long, reason: String): Long{
       return reportManager.initReport(reporting,
           if (conservation.idPerson2 == reporting)
           conservation.idPerson1 else conservation.idPerson2,
           conservation.id,reason)
    }

    fun getLastReport(): ConversationReport{
        val report = reportManager.getReport() ?: return ConversationReport.Empty("Hurra! There are no reports!")
        val sb = StringBuilder()
        sb.append("## Reported for ${report.reason} \n")
        sb.append(presentConversation(report))
        return ConversationReport.Success(report,sb.toString())
    }

    fun presentConversation(report: Report): String{
        val messages = MessageDao.getMessagesFromConversation(report.chatId)
        return showConversation(report.reported,messages, true)
    }
    fun showConversation(reportedPersonId: Long, messages: List<Message>, isReport: Boolean): String{
        val sb = StringBuilder()
        if(isReport){
            for (mes in messages){
                val textMessage = MessageEncrypter.decrypt(mes.message)
                sb.append((if (mes.idUser == reportedPersonId)"Reported: " else "inDweller: ")+textMessage+ "\n")
            }
        }
//        else{
//            val person1 = messages[0].idUser
//            for (mes in messages){
//                val textMessage = MessageEncrypter.decrypt(mes.message)
//
//                sb.append((if (mes.idUser==person1)"inDweller1: " else "inDweller2: ")+ textMessage+ "\n")
//            }
//        }
        return sb.toString()

    }

    fun banReport(idReport: Long, idDiscordAdmin: Long, reason: String) {
        makeOpinion(idReport,idDiscordAdmin,true,reason)
    }

    fun rejectReport(idReport: Long, idDiscordAdmin: Long) {
        makeOpinion(idReport,idDiscordAdmin,false,"")
    }


    private fun makeOpinion(idReport: Long, idDiscordAdmin: Long, opinion: Boolean, reason: String){

        val admin = userService.getUserDiscord(idDiscordAdmin)?: throw RuntimeException("Admin doesn't exist")
        val report = ReportDAO.getReport(idReport) ?: throw RuntimeException("We doesn't have this report...")
        ReportDAO.writeOpinion(report.id!!,opinion,admin.id!!)
        if(opinion){
            val conversationManager = ConversationService()
            conversationManager.leaveConservation(report.chatId)

            userService.banPerson(report.reported,reason, admin.id)
        }
    }
}

sealed class ConversationReport{
    data class Success(val report: Report, val text: String) :ConversationReport()
    data class Empty(val status: String) : ConversationReport()
}