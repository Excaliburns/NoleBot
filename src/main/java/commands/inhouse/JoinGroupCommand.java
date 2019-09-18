package commands.inhouse;

import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.MessageChannel;
import util.JSONLoader;

class JoinGroupCommand {
    JoinGroupCommand(CommandEvent event, String[] args, GroupStruct groupStruct)
    {
        String prefix = event.getPrefix();
        MessageChannel messageChannel = event.getChannel();

        if (args[0] == null) {
            messageChannel.sendMessage("You did not specify a group to join. Use " + prefix + "lfg or " + prefix + "lfg list to see all available groups.").queue();
            return;
        }

        Group foundGroup = groupStruct.getGroups().stream().filter(inhouse -> args[1].toLowerCase().equals(inhouse.getInhouseName().toLowerCase())).findAny().orElse(null);

        if(foundGroup == null)
        {
            messageChannel.sendMessage("There is no group with that name. Use " + prefix + "lfg or " + prefix + "lfg list to list all available groups.").queue();
        }
        else if(foundGroup.getUserList().contains(event.getEvent().getAuthor().getId()))
        {
            messageChannel.sendMessage("You cannot join a group you have already joined.").queue();
        }
        else
        {
            groupStruct.getGroups().get(groupStruct.getGroups().indexOf(foundGroup)).addPlayer(event.getEvent().getAuthor().getId());
            JSONLoader.saveInhouseData(groupStruct, event.getGuildID());
            messageChannel.sendMessage("**" + event.getEvent().getAuthor().getName() + "**" + ", you have joined group: " + "**" + foundGroup.getInhouseName() + "**.").queue();

            if(foundGroup.getRequiredPlayers() == foundGroup.getPlayerCount()) {
                messageChannel.sendMessage("Creating voice and text channel for group: **" + foundGroup.getInhouseName() + "**.").queue();
                new ExecuteGroup(event, foundGroup, groupStruct);
                groupStruct.getGroups().remove(foundGroup);
                JSONLoader.saveInhouseData(groupStruct, event.getGuildID());
            }
        }
    }
}
