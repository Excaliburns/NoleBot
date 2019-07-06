package commands.inhouse;

import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import util.JSONLoader;

class LeaveGroupCommand {
    LeaveGroupCommand(CommandEvent event, String[] args, InhouseStruct inhouseStruct)
    {
        MessageChannel messageChannel = event.getChannel();

        if(args.length == 1)
        {
            messageChannel.sendMessage("You did not specify a group to leave. Use !lfg joined to see a list of inhouses you have joined.").queue();
            return;
        }

        Inhouse foundInhouse = inhouseStruct.getInhouses().stream().filter( inhouse -> args[1].equals(inhouse.getInhouseName())).findAny().orElse(null);
        User author = event.getEvent().getAuthor();

        if(foundInhouse == null)
        {
            messageChannel.sendMessage("There is no group with name **" + args[1] + "**.").queue();
        }
        else if(foundInhouse.getUserList().contains(event.getEvent().getAuthor().getId()))
        {
            if(foundInhouse.getUserList().get(0).equals(author.getId()))
            {
                messageChannel.sendMessage("**" + author.getName() + "**, you are the creator of **" + foundInhouse.getInhouseName() + "**! Please use \"!lfg delete\" instead.").queue();
            }
            else
            {
                foundInhouse.getUserList().remove(author.getId());
                foundInhouse.setPlayerCount(foundInhouse.getPlayerCount()-1);
                JSONLoader.saveInhouseData(inhouseStruct, event.getGuildID());

                messageChannel.sendMessage("**" + author.getName() + "**, you have been removed from **" + foundInhouse.getInhouseName() + "**.").queue();
            }
        }
        else
        {
            messageChannel.sendMessage("**" + author.getName() +"**, you have not joined **"+ foundInhouse.getInhouseName() + "**.").queue();
        }
    }
}
