package com.irq3.discord.server

import com.irq3.discord.utils.Embed
import com.irq3.discord.utils.RulesMessage
import com.irq3.infrastucture.EnvController
import net.dv8tion.jda.api.components.actionrow.ActionRow
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class ServerManager : ListenerAdapter(){

    val guild = EnvController.getData("DISCORD_GUILD")
    val channelId = EnvController.getData("CHANNEL_WELCOME").toLong()

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        if(event.guild.id!=guild) return
        val channel = event.guild.getTextChannelById(channelId)
        channel!!.sendMessageEmbeds(
            Embed("Powitalnia", description = "Witaj ${event.member.asMention}! \n Miło że dołączyłeś na nasz oficjalny serwer," +
                    " dzięki temu możesz w pełni korzystać z bota inDwellers! \n Dzięki tobie jest nas już ${event.guild.memberCount}!", image = event.member.avatarUrl, color = Embed.inDwellerColor).build()
        ).queue()
    }

    override fun onGuildJoin(event: GuildJoinEvent) {
       for(c in event.guild.channels){
           if(c.type== ChannelType.TEXT){
               val channel = event.guild.getTextChannelById(c.idLong)
               channel?.sendMessageEmbeds(Embed("System",
                   description = "Miło nam że nas zaprosiłeś, ale niestety ten bot działa tylko poprzez dm! \n" +
                           "Ale jeśli chcesz skorzystać z niego, przeczytaj i zaakceptuj regulamin!",
                   color = Embed.errorColor).build())?.queue()

               channel?.sendMessageEmbeds(RulesMessage.getRules().first)?.
               addComponents(ActionRow.of(RulesMessage.getRules().second))?.queue()
               return
           }
       }
    }

}