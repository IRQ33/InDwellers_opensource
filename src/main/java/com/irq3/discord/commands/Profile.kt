package com.irq3.discord.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class Profile: Command(
    "profile",
    "Ustawia informacje o profilu",
    false,
    false,
    true,
    true
) {

    override fun invoke(event: SlashCommandInteractionEvent) {

    }
}