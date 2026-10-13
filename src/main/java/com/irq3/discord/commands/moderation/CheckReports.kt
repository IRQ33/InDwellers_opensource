package com.irq3.discord.commands.moderation

import com.irq3.core.services.ConversationReport
import com.irq3.core.DIManager
import com.irq3.discord.commands.Command
import com.irq3.discord.utils.Embed
import net.dv8tion.jda.api.components.actionrow.ActionRow
import net.dv8tion.jda.api.components.buttons.Button
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class CheckReports : Command(
    "check_reports",
    "Opiniuje reporty",
    false,
    true,
    false,
    true
    ){

    override fun invoke(event: SlashCommandInteractionEvent) {
        val service = DIManager.reportService
        when(val text = service.getLastReport()){
           is ConversationReport.Empty -> {
               event.replyEmbeds(Embed("System",text.status, color = Embed.inDwellerColor).build()).queue()
               return
           }
            is ConversationReport.Success-> {
                event.replyEmbeds(Embed("System",text.text).sendAsList())
                    .setComponents(ActionRow.of(
                        Button.danger("ban:${text.report.id}","Ban"),
                        Button.success("reject:${text.report.id!!}","Reject")
                    ))
                    .queue()
            }
        }

    }
}