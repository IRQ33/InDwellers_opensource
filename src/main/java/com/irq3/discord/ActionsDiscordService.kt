package com.irq3.discord

import com.irq3.core.DIManager
import com.irq3.core.services.RegisterResult
import com.irq3.discord.utils.Embed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent

class ActionsDiscordService {
    fun getAction(interaction: ButtonInteractionEvent): Boolean{
        val text = interaction.componentId

        when(text){
            "next_conversation"->{
                nextConversation(interaction)
                return true
            }
            "accept_rules"->{
                acceptRules(interaction)
                return true
            }
            "leave"->{
                leaveConversation(interaction)
                return true
            }
        }
        return false
    }

    private fun nextConversation(interaction: ButtonInteractionEvent) {
        val joinService = JoinService()
        runBlocking {
            joinService.join(interaction.user.idLong){
                    embed-> interaction.replyEmbeds(embed)
            }
        }
    }

    private fun acceptRules(interaction: ButtonInteractionEvent) {
        val registerService = DIManager.userService
        when(val response = registerService.registerUser(interaction.user.idLong)){
            is RegisterResult.UserBanned -> {
                interaction.replyEmbeds(Embed("System", "Zostałeś zbanowany za: "+response.reason, color = Embed.criticalColor).build()).queue()
                return
            }
            is RegisterResult.Error -> {
                return
            }
            is RegisterResult.Success -> {
                DIManager.userService.giveVip(response.user.id!!)
                DIManager.interactDM(response.user.idDiscord){
                    it.openPrivateChannel().queue {
                        it.sendMessageEmbeds(
                            Embed("System","Dziękujemy za rejestrację w naszym systemie. W ramach podziękowania dostajesz vipa na miesiąc!",
                                Embed.successColor).build()
                        )
                    }
                }
                val joinService = JoinService()
                CoroutineScope(Dispatchers.IO).launch  {
                    joinService.join(interaction.user.idLong){
                            embed-> interaction.replyEmbeds(embed)
                    }
                }
            }
        }
    }

    private fun leaveConversation(interaction: ButtonInteractionEvent) {
        val user = DIManager.userService.getUserDiscord(interaction.user.idLong)
        runBlocking {
            PeopleQueue.remove(user!!)
        }
        interaction.replyEmbeds(Embed("System","Wyszedłeś/aś z kolejki", color = Embed.infoColor).build()).queue()
    }

}