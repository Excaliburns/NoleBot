package commands.inhouse;

import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.MessageChannel;
import util.JSONLoader;

import java.util.ArrayList;
import java.util.List;

class GroupCreateCommand {

    GroupCreateCommand(CommandEvent event, String[] args, InhouseStruct inhouseStruct)
    {
        MessageChannel messageChannel = event.getChannel();
        List<Inhouse> currentInhouses = inhouseStruct.getInhouses();


        if (args.length == 1) {
            messageChannel.sendMessage("You did not specific a name. Please use !help lfg for usage.").queue();
            return;
        }
        if (args.length == 2) {
            messageChannel.sendMessage("You did not specify the required amount of players. Please use !help lfg for usage.").queue();
            return;
        }

        int duration = 3;
        if (args.length == 4)
        {
            try {
                duration = Integer.parseInt(args[3]);
                if(duration <= 0){
                    messageChannel.sendMessage("You cannot create a group with a duration of 0 or less. Please try again.").queue();
                }
            } catch (NumberFormatException e) {
                messageChannel.sendMessage("Please use a number to specify the duration of your room. Please use !help lfg for usage.").queue();
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
            messageChannel.sendMessage("You need to use a number for your required players. Please use !help lfg for usage.").queue();
            return;
        }

        if(currentInhouses == null)
            currentInhouses = new ArrayList<>();

        String name = args[1];

        if(name.length() > 50)
        {
            messageChannel.sendMessage("Your group name must be shorter than 50 characters.").queue();
            return;
        }

        if (currentInhouses.stream().map(Inhouse::getInhouseName).anyMatch(name::equals)) {
            messageChannel.sendMessage("You cannot create a group with the same name as another group. Please choose a different name.").queue();
            return;
        }

        Inhouse inhouse = new Inhouse(name, requiredPlayers);
        inhouse.addPlayer(event.getEvent().getAuthor().getId());

        inhouse.setDuration(duration);
        currentInhouses.add(inhouse);

        inhouseStruct.setInhouses(currentInhouses);

        JSONLoader.saveInhouseData(inhouseStruct, event.getSettings().getGuildID());

        messageChannel.sendMessage("Created Group: **" + inhouse.getInhouseName() + "**. Your group will be messaged when the queue is full.").queue();
    }
}
