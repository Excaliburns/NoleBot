package commands.inhouse;

import commands.util.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

class ListGroupCommand {
    ListGroupCommand(CommandEvent event, InhouseStruct inhouseStruct)
    {
        List<Inhouse> currentInhouses = inhouseStruct.getInhouses();
        MessageChannel messageChannel = event.getChannel();

        if (!currentInhouses.isEmpty()) {
            messageChannel.sendMessage("Here are the groups currently being formed: ").queue();
            for (Inhouse l : currentInhouses) {
                StringBuilder listofUsers = new StringBuilder();
                for (String u : l.getUserList()) {
                    try
                    {
                        Member x = event.getGuild().getMemberById(u);
                        listofUsers.append(x.getEffectiveName());
                        listofUsers.append("\n");
                    }
                    catch(NullPointerException e)
                    {
                        listofUsers.append("USER NOT FOUND, DELETING FOR NEXT CALL.");
                        l.getUserList().remove(u);
                    }
                }
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.addField("Name: ", l.getInhouseName(), true);
                embedBuilder.addField("No. of Players Needed:", Integer.toString(l.getRequiredPlayers()), true);
                embedBuilder.addField("No. of Players in Queue:", Integer.toString(l.getPlayerCount()), true);
                embedBuilder.addField("Creator: ", event.getGuild().getMemberById(l.getUserList().get(0)).getEffectiveName(), true);
                embedBuilder.addField("Players in Queue: ", listofUsers.toString(), false);
                messageChannel.sendMessage(embedBuilder.build()).queue();
            }
        } else {
            messageChannel.sendMessage("There are no groups currently being formed. Use !help lfg to learn how to start one!").queue();
        }
    }
}
