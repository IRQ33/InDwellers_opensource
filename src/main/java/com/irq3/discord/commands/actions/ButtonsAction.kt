package com.irq3.discord.commands.actions

import com.irq3.core.DIManager
import com.irq3.discord.ActionsDiscordService
import com.irq3.discord.utils.AdminChecker
import com.irq3.discord.utils.Embed
import net.dv8tion.jda.api.components.label.Label
import net.dv8tion.jda.api.components.textinput.TextInput
import net.dv8tion.jda.api.components.textinput.TextInputStyle
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.modals.Modal
import org.slf4j.LoggerFactory


class ButtonsAction: ListenerAdapter() {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if(event.componentId.startsWith("ban") || event.componentId.startsWith("reject")){
            val banVals = event.componentId.split(":")
            if(banVals.size!=2) throw RuntimeException("Error, no arguments or bad arguments")

            if(event.componentId.startsWith("ban")){
                val subject = TextInput.create("ban", TextInputStyle.SHORT)
                    .setPlaceholder("Reason of the ban")
                    .setMinLength(1)
                    .setMaxLength(100)
                    .build()

                val modal = Modal.create("ban:"+banVals[1], "Ban")
                    .addComponents(Label.of("Reason", subject))
                    .build()

                event.replyModal(modal).queue()
                return
            } else if(event.componentId.startsWith("reject")){
                val manager = DIManager.reportService
                manager.rejectReport(banVals[1].toLong(),idDiscordAdmin = event.user.idLong)
                event.replyEmbeds(Embed("System","Congratulation, you rejected ${banVals[1]}", Embed.successColor).build()).queue()
                return
            }
            logger.error(event.toString())

        }
        if(!ActionsDiscordService().getAction(event)){
            return
        }

        super.onButtonInteraction(event)
    }

    override fun onModalInteraction(event: ModalInteractionEvent) {
        if(!AdminChecker.isAdmin(event.user.idLong)){
            event.replyEmbeds(Embed("System","Nie masz admina").build()).queue()
            return
        }
        if(event.modalId.startsWith("ban")){
            val args = event.modalId.split(":")
            if(args.size!=2) throw RuntimeException("Error, no arguments")
            val (_, idReport) = args

            val reason = event.getValue("ban")?.asString.orEmpty()
            val manager = DIManager.reportService
            manager.banReport(idDiscordAdmin = event.user.idLong, reason = reason, idReport = idReport.toLong())
            event.replyEmbeds(Embed("System","Zbanowałeś/aś $idReport za $reason", Embed.mutedColor).build()).queue()
        }
    }
}