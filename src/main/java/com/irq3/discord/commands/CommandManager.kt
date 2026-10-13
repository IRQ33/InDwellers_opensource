package com.irq3.discord.commands

import com.irq3.discord.utils.AdminChecker
import com.irq3.discord.DiscordBot
import com.irq3.discord.commands.games.RandomOne
import com.irq3.discord.commands.moderation.AddTopic
import com.irq3.discord.commands.moderation.Announcements
import com.irq3.discord.commands.moderation.BanUser
import com.irq3.discord.commands.moderation.BanUserID
import com.irq3.discord.commands.moderation.CheckReports
import com.irq3.discord.commands.moderation.GiveVip
import com.irq3.discord.commands.moderation.PeopleAnnounce
import com.irq3.discord.commands.moderation.UnbanUser
import com.irq3.discord.utils.Embed
import com.irq3.infrastucture.EnvController
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.LoggerFactory

class CommandManager : ListenerAdapter{
    private val commands = ArrayList<Command>()
    private val jda: JDA
    private val logger = LoggerFactory.getLogger(this.javaClass)

    constructor(jda : JDA){
        this.jda = jda
        addCommands()
    }

    fun addCommands(){
        commands.add(PeopleAnnounce())
        commands.add(LeaveCommand())
        commands.add(JoinCommand())
        commands.add(BanUser())
        commands.add(BanUserID())
        commands.add(UnbanUser())
        commands.add(CheckReports())
        commands.add(Report())
        commands.add(Block())
        commands.add(BugReport())
        commands.add(DiscordServer())
        commands.add(Help())
        commands.add(Info())
        commands.add(Announcements())
        commands.add(AddTopic())
        commands.add(Topic())
        commands.add(Unblock())
        commands.add(RandomOne())
        commands.add(GiveVip())
    }

    override fun onReady(event: ReadyEvent) {
        logger.info("Guilds: {}",jda.guilds.size)
        for(g in jda.guilds){
            logger.info("{}",g.toString())
        }
        registerCommands()
    }


    private fun registerCommands() {
        val commands2 = commands
        jda.updateCommands().addCommands(commands2
            .filter { !it.isAdminOnly && it.isDMAvailable }
            .map { it.mapToCommandData() }).queue()

        for (guild in jda.guilds) {
            if (guild.idLong == DiscordBot.getGuild()) {
                guild.updateCommands().addCommands(commands2
                    .filter { it.isAdminOnly && it.isServerAvailable}
                    .map { it.mapToCommandData() }).queue()
                continue
            }
            if(guild.idLong== EnvController.getData("DISCORD_GUILD").toLong()){
                guild.updateCommands().addCommands(commands2
                    .filter { it.isServerAvailable && !(!it.isAdminOnly && it.isDMAvailable) }
                    .map { it.mapToCommandData() }).queue()
            }
        }
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        for(command in commands){
            if(command.name == event.name){
                if((command.isAdminOnly )){
                    if(!AdminChecker.isAdmin(event.user.idLong)){
                            event.replyEmbeds(
                                Embed("System","Nie masz dostępu do tej komendy! 1",
                                    color = Embed.errorColor).build()).queue()
                            return
                        }
                }

                if(command.isOwnerOnly && !AdminChecker.isOwner(event.user.idLong)){
                    event.replyEmbeds(Embed("System","Nie masz dostępu do tej komendy! 2", color = Embed.errorColor).build()).queue()
                    return
                }
                command.invoke(event)
                return
            }
        }

    }
}