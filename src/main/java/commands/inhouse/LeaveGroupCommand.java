package commands.inhouse;

import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import util.JSONLoader;

class LeaveGroupCommand {
    LeaveGroupCommand(CommandEvent event, String[] args, GroupStruct groupStruct)
    {
        String prefix = event.getPrefix();
        MessageChannel messageChannel = event.getChannel();

        if(args.length == 1)
        {
            messageChannel.sendMessage("You did not specify a group to leave. Use " + prefix + "lfg joined to see a list of groups you have joined.").queue();
            return;
        }

        Group foundGroup = groupStruct.getGroups().stream().filter(inhouse -> args[1].equals(inhouse.getInhouseName())).findAny().orElse(null);
        User author = event.getEvent().getAuthor();

        if(foundGroup == null)
        {
            messageChannel.sendMessage("There is no group with name **" + args[1] + "**.").queue();
        }
        else if(foundGroup.getUserList().contains(event.getEvent().getAuthor().getId()))
        {
            if(foundGroup.getUserList().get(0).equals(author.getId()))
            {
                messageChannel.sendMessage("**" + author.getName() + "**, you are the creator of **" + foundGroup.getInhouseName() + "**! Please use " + prefix + "lfg delete instead.").queue();
            }
            else
            {
                foundGroup.getUserList().remove(author.getId());
                foundGroup.setPlayerCount(foundGroup.getPlayerCount()-1);
                JSONLoader.saveInhouseData(groupStruct, event.getGuildID());

                messageChannel.sendMessage("**" + author.getName() + "**, you have been removed from **" + foundGroup.getInhouseName() + "**.").queue();
            }
        }
        else
        {
            messageChannel.sendMessage("**" + author.getName() +"**, you have not joined **"+ foundGroup.getInhouseName() + "**.").queue();
        }
    }
}
