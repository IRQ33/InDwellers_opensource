package com.irq3.discord.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.IntegrationType
import net.dv8tion.jda.api.interactions.IntegrationType.USER_INSTALL
import net.dv8tion.jda.api.interactions.InteractionContextType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

abstract class Command(
    val name:String,
    val description:String,
    val isOwnerOnly: Boolean,
    val isAdminOnly: Boolean,
    val isDMAvailable: Boolean,
    val isServerAvailable: Boolean,
    val optionList: ArrayList<OptionData> = ArrayList(),
    val isVipOnly: Boolean = false
) {

    fun work(event: SlashCommandInteractionEvent){
//        if(!wantRespond){
//            event.deferReply().setEphemeral(true).queue()
//        }
        invoke(event)
    }
    abstract fun invoke(event: SlashCommandInteractionEvent)

    fun mapToCommandData(): SlashCommandData {
        val commandData = Commands.slash(name, description)
        commandData.addOptions(optionList)

        if (isDMAvailable) {
            commandData.setIntegrationTypes(USER_INSTALL, IntegrationType.GUILD_INSTALL)
            commandData.setContexts(InteractionContextType.PRIVATE_CHANNEL, InteractionContextType.GUILD, InteractionContextType.BOT_DM)
        }
        return commandData
    }
}
