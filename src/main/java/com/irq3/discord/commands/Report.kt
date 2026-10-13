package com.irq3.discord.commands

import com.irq3.core.DIManager
import com.irq3.discord.utils.Embed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class Report : Command(
    "report",
    "Zgłasza aktualną/poprzednią konwersację.",
    false,
    false,
    true,
    false,
    arrayListOf(
        OptionData(OptionType.STRING, "reason","Reason why you report someone",true)
    )
){

    override fun invoke(event: SlashCommandInteractionEvent) {
        val user = DIManager.userService.getUserDiscord(event.user.idLong)
        if(user==null){
            event.replyEmbeds(Embed("System","Nie masz konta!", color = Embed.errorColor).sendAsList()).queue()
            return
        }
        val reason = event.getOption("reason")?.asString

        val manager = DIManager.reportService
        try {
            manager.report(user.id!!,reason.orEmpty())
        }catch (_: Exception){
            event.replyEmbeds(Embed("System","Nie masz żadnych konwersacji!", color = Embed.errorColor).build()).queue()
            return
        }

        event.replyEmbeds(Embed("System", "Thanks for reporting", color = Embed.successColor).sendAsList()).queue()
    }
}