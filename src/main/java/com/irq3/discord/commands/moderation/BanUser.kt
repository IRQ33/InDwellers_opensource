package com.irq3.discord.commands.moderation

import com.irq3.core.DIManager
import com.irq3.discord.commands.Command
import com.irq3.discord.utils.Embed
import com.irq3.infrastucture.EnvController
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class BanUser : Command(
    "ban",
    "Banuje daną osobę",
    false,
    true,
    false,
    true,
    arrayListOf(
        OptionData(OptionType.USER, "user","person to ban", true),
        OptionData(OptionType.STRING,"reason", "Reason", true)
    )
) {


    override fun invoke(event: SlashCommandInteractionEvent) {
        val manager = DIManager.userService
        val user = event.getOption("user")?.asUser
        if(user==null){
            event.reply("Bad user").queue()
            return
        }
        val reason = event.getOption("reason")?.asString
        manager.banPerson(user.idLong,reason,event.user.idLong)
        event.replyEmbeds(Embed("System","Zbanowałeś  ${user.effectiveName}").sendAsList()).queue()

        val guild = event.jda.getGuildById(EnvController.getData("DISCORD_GUILD"))
        guild?.ban(listOf(user), null)
    }
}