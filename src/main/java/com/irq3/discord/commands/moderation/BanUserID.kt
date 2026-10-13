package com.irq3.discord.commands.moderation

import com.irq3.core.DIManager
import com.irq3.discord.commands.Command
import com.irq3.discord.utils.Embed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class BanUserID : Command(
    "banid",
    "Banuje daną osobę",
    false,
    true,
    false,
    true,
    arrayListOf(
        OptionData(OptionType.STRING, "user","person to ban", true),
        OptionData(OptionType.STRING,"reason", "Reason", true)
    )
) {

    override fun invoke(event: SlashCommandInteractionEvent) {
        val manager = DIManager.userService
        val user = event.getOption("user")!!.asString.toLong()
        val reason = event.getOption("reason")?.asString
        manager.banPerson(user,reason,event.user.idLong)
        event.replyEmbeds(Embed("System","You banned person with id ${manager.getUserDiscord(user)}").build()).queue()
    }
}