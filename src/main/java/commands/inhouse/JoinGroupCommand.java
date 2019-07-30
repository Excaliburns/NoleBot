package commands.inhouse;

import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.MessageChannel;
import util.JSONLoader;

class JoinGroupCommand {
    JoinGroupCommand(CommandEvent event, String[] args, InhouseStruct inhouseStruct)
    {
        MessageChannel messageChannel = event.getChannel();

        if (args[0] == null) {
            messageChannel.sendMessage("You did not specify a group to join. Use !lfg or !lfg list to see all available groups.").queue();
            return;
        }

        Inhouse foundInhouse = inhouseStruct.getInhouses().stream().filter( inhouse -> args[1].equals(inhouse.getInhouseName())).findAny().orElse(null);

        if(foundInhouse == null)
        {
            messageChannel.sendMessage("There is no group with that name. Use !lfg or !lfg list to list all available groups.").queue();
        }
        else if(foundInhouse.getUserList().contains(event.getEvent().getAuthor().getId()))
        {
            messageChannel.sendMessage("You cannot join a group you have already joined.").queue();
        }
        else
        {
            inhouseStruct.getInhouses().get(inhouseStruct.getInhouses().indexOf(foundInhouse)).addPlayer(event.getEvent().getAuthor().getId());
            JSONLoader.saveInhouseData(inhouseStruct, event.getGuildID());
            messageChannel.sendMessage("**" + event.getEvent().getAuthor().getName() + "**" + ", you have joined group: " + "**" + foundInhouse.getInhouseName() + "**.").queue();

            if(foundInhouse.getRequiredPlayers() == foundInhouse.getPlayerCount()) {
                messageChannel.sendMessage("Creating voice and text channel for group: **" + foundInhouse.getInhouseName() + "**.").queue();
                new ExecuteGroup(event, foundInhouse, inhouseStruct);
                inhouseStruct.getInhouses().remove(foundInhouse);
                JSONLoader.saveInhouseData(inhouseStruct, event.getGuildID());
            }
        }
    }
}
