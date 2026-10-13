package com.irq3.discord.commands.moderation

import com.irq3.core.DIManager
import com.irq3.discord.commands.Command
import com.irq3.discord.utils.Embed
import com.irq3.infrastucture.EnvController
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import java.awt.Color

class PeopleAnnounce : Command(
    "people_announce",
    "Wysyła wiadomość do wszystkich na dm",
    false,
    true,
    false,
    true,
    arrayListOf(
        OptionData(OptionType.STRING,"name","Name of announcement"),
        OptionData(OptionType.STRING,"description","Description of announcement"),
        OptionData(OptionType.STRING, "color", "Color of the emebed", false),
        OptionData(OptionType.ATTACHMENT, "image", "Image for the embed", false)
    )) {

    val role = EnvController.getData("NEWSLETTER_ROLE")


    override fun invoke(event: SlashCommandInteractionEvent) {
        val name = event.getOption("name")!!.asString
        val description = event.getOption("description")!!.asString
        val (r,g,b) = event.getOption("color")?.asString?.split(",")?: "0,43,255".split(",")
        val thumbnail = event.getOption("image")?.asAttachment

        if(thumbnail!=null && !thumbnail.isImage){
            event.replyEmbeds(Embed(description="To nie jest obraz!, ale treść tego to $name i $description", color = Embed.errorColor).build())
                .setEphemeral(true).queue()
            return
        }
        val embed = Embed(name,description, Color(r.toInt(),g.toInt(),b.toInt()),image=thumbnail?.url)
        DIManager.stuffSender.emit(embed)

        event.deferReply().queue()

        event.channel
            .sendMessage(event.guild!!.getRoleById(role)!!.asMention)
            .setEmbeds(embed.build()).queue()

    }

}