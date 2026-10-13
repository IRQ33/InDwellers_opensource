package com.irq3.discord.commands

import com.irq3.core.DIManager
import com.irq3.core.Event
import com.irq3.core.EventBus
import com.irq3.discord.utils.Embed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class BugReport : Command(
    "bug_report",
    "Zgłasza błąd",
    false,
    false,
    true,
    true,
    arrayListOf(
        OptionData(OptionType.STRING, "description", "Description of bug",true)
    )
) {

    override fun invoke(event: SlashCommandInteractionEvent) {
        val text = event.getOption("description")!!.asString
        val user = DIManager.userService.getUserDiscord(event.user.idLong)
        if(user==null){
            event.replyEmbeds(Embed("System","Nie masz konta!", color = Embed.errorColor).build()).queue()
            return
        }
        EventBus.emit(Event.BugReportCreated(text,user.id!!))
        event.replyEmbeds(Embed("System","Dziękujemy za zgłoszenie błędu :)").build()).setEphemeral(true).queue()
    }
}