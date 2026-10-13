package com.irq3.discord.commands.moderation

import com.irq3.core.DIManager
import com.irq3.discord.commands.Command
import com.irq3.discord.utils.Embed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class UnbanUser : Command(
    "unban",
    "Odbanowuje zbanowaną osobę",
    false,
    true,
    false,
    true,
    arrayListOf(
        OptionData(OptionType.USER,"id","Id of user")
    )
) {

    override fun invoke(event: SlashCommandInteractionEvent) {
        val id = event.getOption("id")?.asUser?.idLong
        if(id==null){
            event.reply("Bad id!").queue()
            return
        }
        val service = DIManager.userService
        service.unBanPerson(id)
        event.replyEmbeds(
            Embed("System","Właśnie odbanowałeś indwellera!").sendAsList()
        ).queue()
    }
}