package commands.inhouse;

import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.MessageChannel;
import util.JSONLoader;

import java.util.ArrayList;
import java.util.List;

class GroupCreateCommand {

    GroupCreateCommand(CommandEvent event, String[] args, GroupStruct groupStruct)
    {
        String prefix = event.getSettings().getPrefix();
        MessageChannel messageChannel = event.getChannel();
        List<Group> currentGroups = groupStruct.getGroups();


        if (args.length == 1) {
            messageChannel.sendMessage("You did not specific a name. Please use " + prefix + "help lfg for usage.").queue();
            return;
        }
        if (args.length == 2) {
            messageChannel.sendMessage("You did not specify the required amount of players. Please use " + prefix + "help lfg for usage.").queue();
            return;
        }

        int duration = 3;
        if (args.length == 4)
        {
            try {
                duration = Integer.parseInt(args[3]);
                if(duration <= 0){
                    messageChannel.sendMessage("You cannot create a group with a duration of 0 or less. Please try again.").queue();
                } else if(duration >= 12)
                {
                    messageChannel.sendMessage("There is a maximum length of 12 hours. Please try again.").queue();
                }
            } catch (NumberFormatException e) {
                messageChannel.sendMessage("Please use a number to specify the duration of your room. Please use " + prefix + "help lfg for usage.").queue();
                return;
            }
        }

        int requiredPlayers;
        try {
            requiredPlayers = Integer.parseInt(args[2]);
            if(requiredPlayers <= 1){
                messageChannel.sendMessage("You cannot create a group with one or less people. Please try again.").queue();
            }
        } catch (NumberFormatException e) {
            messageChannel.sendMessage("You need to use a number for your required players. Please use " + prefix + "help lfg for usage.").queue();
            return;
        }

        if(currentGroups == null)
            currentGroups = new ArrayList<>();

        String name = args[1];

        if(name.length() > 50)
        {
            messageChannel.sendMessage("Your group name must be shorter than 50 characters.").queue();
            return;
        }

        if (currentGroups.stream().map(Group::getInhouseName).anyMatch(name::equals)) {
            messageChannel.sendMessage("You cannot create a group with the same name as another group. Please choose a different name.").queue();
            return;
        }

        Group group = new Group(name, requiredPlayers);
        group.addPlayer(event.getEvent().getAuthor().getId());

        group.setDuration(duration);
        currentGroups.add(group);

        groupStruct.setGroups(currentGroups);

        JSONLoader.saveInhouseData(groupStruct, event.getSettings().getGuildID());

        messageChannel.sendMessage("Created Group: **" + group.getInhouseName() + "**. Your group will be messaged when the queue is full.").queue();
    }
}
