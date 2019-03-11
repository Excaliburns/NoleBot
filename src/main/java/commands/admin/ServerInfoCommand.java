package commands.admin;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import util.BotEmbed;

import java.awt.*;
import java.util.*;

/**
 * This command gets all of the roles from a server and outputs their IDs. It also displays the Guild owner, and the total amount of members that the Guild has.
 * <p>
 * Potentially useful for hardcoding RoleIDs into things, however in the future an outside way of registering permissions to users will be used.
 */
public class ServerInfoCommand extends Command {
    public ServerInfoCommand() {
        name = "serverinfo";
        description = "Gets relevant server information.";
        helpDescription = "Sends information about your server. Information such as the owner, the total members, total role count, etc.";
        requiredPermission = 1000;
        usages.add("serverinfo");
    }

    @Override
    public void onCommandReceived(CommandEvent event) {
        MessageChannel messageChannel = event.getChannel();

        Guild sentGuild = event.getGuild();

        EmbedBuilder embedBuilder = BotEmbed.getBotEmbed(event);

        embedBuilder.setThumbnail(event.getGuild().getIconUrl());
        embedBuilder.addField("Guild Name: ", sentGuild.getName(), true);
        embedBuilder.addField("Owner: ", sentGuild.getOwner().getEffectiveName(), true);
        embedBuilder.addField("Total Members: " , Integer.toString(sentGuild.getMembers().size()), true);
        embedBuilder.addField("Total Role count: ", Integer.toString(sentGuild.getRoles().size() - 1), true);
        embedBuilder.addField("Region: ", sentGuild.getRegionRaw(), true);
        embedBuilder.addField("Default Channel: ", sentGuild.getDefaultChannel().getAsMention(), true);
        messageChannel.sendMessage(embedBuilder.build()).queue();
    }
}