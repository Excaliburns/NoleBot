package commands.inhouse;

import commands.util.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.internal.entities.MemberImpl;

import java.util.List;

class JoinedGroupsCommand {
    JoinedGroupsCommand(CommandEvent event, GroupStruct groupStruct)
    {
        MessageChannel messageChannel = event.getChannel();

        List<Group> joinedGroups = groupStruct.getGroups();
        joinedGroups.removeIf(inhouse -> !inhouse.getUserList().contains(event.getEvent().getAuthor().getId()));

        if(joinedGroups.size() == 0)
            messageChannel.sendMessage("You have not joined any groups.").queue();
        else
        {
            messageChannel.sendMessage("Here are the groups you are waiting on, **" + event.getEvent().getAuthor().getName() + "**.").queue();

            for(Group l : joinedGroups)
            {
                Member creator = event.getGuild().getMemberById(l.getUserList().get(0));
                String creatorName;
                String playersInQueue = l.getPlayerCount() + "/" + l.getRequiredPlayers();

                if(creator == null)
                    creatorName = "Could not find creator.";
                else
                    creatorName = creator.getNickname();

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.addField("Name: ", l.getInhouseName(), true);
                embedBuilder.addField("Creator: ", creatorName, true);
                embedBuilder.addField("Players Needed / Players in Queue :", playersInQueue, false);

                if(embedBuilder.length() > 1500)
                {
                    messageChannel.sendMessage(embedBuilder.build()).queue();
                }

                messageChannel.sendMessage(embedBuilder.build()).queue();
            }
        }
    }
}
