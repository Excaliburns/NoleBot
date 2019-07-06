package commands.inhouse;

import commands.util.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.List;

class ListGroupCommand {
    ListGroupCommand(CommandEvent event, InhouseStruct inhouseStruct)
    {
        List<Inhouse> currentInhouses = inhouseStruct.getInhouses();
        MessageChannel messageChannel = event.getChannel();

        if (currentInhouses != null) {
            messageChannel.sendMessage("Here are the groups currently being formed: ").queue();
            for (Inhouse l : currentInhouses) {
                StringBuilder listofUsers = new StringBuilder();
                for (String u : l.getUserList()) {
                    listofUsers.append(event.getGuild().getMemberById(u).getNickname());
                    listofUsers.append("\n");
                }
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.addField("Name: ", l.getInhouseName(), true);
                embedBuilder.addField("No. of Players Needed:", Integer.toString(l.getRequiredPlayers()), true);
                embedBuilder.addField("No. of Players in Queue:", Integer.toString(l.getPlayerCount()), true);
                embedBuilder.addField("Creator: ", event.getGuild().getMemberById(l.getUserList().get(0)).getNickname(), true);
                embedBuilder.addField("Players in Queue: ", listofUsers.toString(), false);
                messageChannel.sendMessage(embedBuilder.build()).queue();
            }
        } else {
            messageChannel.sendMessage("There are no inhouses currently being formed. Use !help inhouse to learn how to start one!").queue();
        }
    }
}
