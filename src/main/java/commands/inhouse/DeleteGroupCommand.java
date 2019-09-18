package commands.inhouse;

import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import util.JSONLoader;


class DeleteGroupCommand {
     DeleteGroupCommand(CommandEvent event, String[] args, GroupStruct groupStruct) {
         MessageChannel messageChannel = event.getChannel();

         if(args.length == 1)
         {
             messageChannel.sendMessage("You did not specify a group to delete. Use !lfg joined to see a list of groups you have joined.").queue();
             return;
         }

         Group foundGroup = groupStruct.getGroups().stream().filter(inhouse -> (args[1].equals(inhouse.getInhouseName()))).findAny().orElse(null);
         User author = event.getEvent().getAuthor();

         if(foundGroup == null)
         {
             messageChannel.sendMessage("There is no group with that name.").queue();
         }
         else if(foundGroup.getUserList().get(0).equals(author.getId()) || event.getUserPermLevel() >=  (new GroupCommand().getRequiredPermission() * 2))
         {
             groupStruct.getGroups().remove(foundGroup);
             JSONLoader.saveInhouseData(groupStruct, event.getGuildID());
             messageChannel.sendMessage("Your group, **" + foundGroup.getInhouseName() + "** has been disbanded.").queue();
         }
         else
         {
             messageChannel.sendMessage("You are not the owner of that group.").queue();
         }
     }
}
