package com.irq3.discord.commands.moderation

import com.irq3.core.DIManager
import com.irq3.discord.commands.Command
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class AddTopic : Command(
    "add_topic",
    "Dodaje temat do rozmów",
    false,
    true,
    true,
    true,
    arrayListOf(
        OptionData(OptionType.STRING,"temat","Nazwa tematu",true)
    )
){

    override fun invoke(event: SlashCommandInteractionEvent) {
        val option = event.getOption("temat")!!.asString
        DIManager.topicsService.createTopic(option,event.user.idLong)
        event.reply("Zrobiony temat do rozmów").queue()
    }
}