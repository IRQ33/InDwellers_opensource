package com.irq3.discord.commands.moderation

import com.irq3.core.DIManager
import com.irq3.discord.commands.Command
import com.irq3.discord.utils.Embed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class GiveVip: Command(
    "give_vip",
    "Nadaje vipa na miesiąc",
    false,
    true,
    false,
    true,
    arrayListOf(
        OptionData(OptionType.STRING,"id","Id of user",true)
    )
) {

    override fun invoke(event: SlashCommandInteractionEvent) {
        val userDcId = event.getOption("id")!!.asString.toLong()
        val user = DIManager.userService.getUserDiscord(userDcId)
        if(user==null){
            event.replyEmbeds(
                Embed("System","Nie ma takiego użytkownika!", Embed.errorColor).build()
            ).queue()
            return
        }
        DIManager.userService.giveVip(user.id!!)
        event.replyEmbeds(Embed("System","Nadałeś vipa na 30 dni!").build()).queue()
    }
}