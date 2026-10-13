package com.irq3.discord.commands

import com.irq3.core.DIManager
import com.irq3.discord.utils.Buttons
import com.irq3.discord.utils.Embed
import net.dv8tion.jda.api.components.actionrow.ActionRow
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class LeaveCommand : Command(
    "leave",
    "Opuszcza aktualną konwersację",
    false,
    false,
    true,
    false
) {
    override fun invoke(event: SlashCommandInteractionEvent) {
        DIManager.conversationService.leave(event.user.idLong)
        event.replyEmbeds(Embed(title = "System",
            description = "Wyszedłeś z konwersacji!"
        ).build()).setComponents(ActionRow.of(
            Buttons.nextConversation()
        )).queue()
    }
}