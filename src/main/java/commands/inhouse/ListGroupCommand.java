package commands.inhouse;

import commands.util.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;

class ListGroupCommand {
    ListGroupCommand(CommandEvent event, GroupStruct groupStruct)
    {
        List<Group> currentGroups = groupStruct.getGroups();
        MessageChannel messageChannel = event.getChannel();

        if (!currentGroups.isEmpty()) {
            messageChannel.sendMessage("Here are the groups currently being formed: ").queue();
            for (Group l : currentGroups) {
                StringBuilder listOfUsers = new StringBuilder();
                List<String> nullUsers = new ArrayList<>();

                for (String u : l.getUserList()) {
                        Member x = event.getGuild().getMemberById(u);

                        if(x == null)
                        {
                            nullUsers.add(u);
                            continue;
                        }

                        listOfUsers.append(x.getEffectiveName());
                        listOfUsers.append("\n");
                }

                l.getUserList().removeAll(nullUsers);

                Member creator = event.getGuild().getMemberById(l.getUserList().get(0));
                String creatorName;

                if(creator == null)
                    creatorName = "Could not find creator.";
                else
                    creatorName = creator.getNickname();

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.addField("Name: ", l.getInhouseName(), true);
                embedBuilder.addField("No. of Players Needed:", Integer.toString(l.getRequiredPlayers()), true);
                embedBuilder.addField("No. of Players in Queue:", Integer.toString(l.getPlayerCount()), true);
                embedBuilder.addField("Creator: ", creatorName, true);
                embedBuilder.addField("Players in Queue: ", listOfUsers.toString(), false);

                messageChannel.sendMessage(embedBuilder.build()).queue();
            }
        } else {
            messageChannel.sendMessage("There are no groups currently being formed. Use !help lfg to learn how to start one!").queue();
        }
    }
}
