package commands.inhouse;

import commands.util.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.List;

class JoinedGroupsCommand {
    JoinedGroupsCommand(CommandEvent event, InhouseStruct inhouseStruct)
    {
        MessageChannel messageChannel = event.getChannel();

        List<Inhouse> joinedInhouses = inhouseStruct.getInhouses();
        joinedInhouses.removeIf( inhouse -> !inhouse.getUserList().contains(event.getEvent().getAuthor().getId()));

        if(joinedInhouses.size() == 0)
            messageChannel.sendMessage("You have not joined any groups.").queue();
        else
        {
            messageChannel.sendMessage("Here are the groups you are waiting on, **" + event.getEvent().getAuthor().getName() + "**.").queue();

            for(Inhouse l : joinedInhouses)
            {
                String playersInQueue = l.getPlayerCount() + "/" + l.getRequiredPlayers();

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.addField("Name: ", l.getInhouseName(), true);
                embedBuilder.addField("Creator: ", event.getGuild().getMemberById(l.getUserList().get(0)).getNickname(), true);
                embedBuilder.addField("Players Needed / Players in Queue :", playersInQueue, false);
                messageChannel.sendMessage(embedBuilder.build()).queue();
            }
        }
    }
}
