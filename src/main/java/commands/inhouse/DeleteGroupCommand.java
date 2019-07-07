package commands.inhouse;

import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import util.JSONLoader;


class DeleteGroupCommand {
     DeleteGroupCommand(CommandEvent event, String[] args, InhouseStruct inhouseStruct) {
         MessageChannel messageChannel = event.getChannel();

         if(args.length == 1)
         {
             messageChannel.sendMessage("You did not specify a group to delete. Use !lfg joined to see a list of groups you have joined.").queue();
             return;
         }

         Inhouse foundInhouse = inhouseStruct.getInhouses().stream().filter(inhouse -> (args[1].equals(inhouse.getInhouseName()))).findAny().orElse(null);
         User author = event.getEvent().getAuthor();

         if(foundInhouse == null)
         {
             messageChannel.sendMessage("There is no group with that name.").queue();
         }
         else if(foundInhouse.getUserList().get(0).equals(author.getId()))
         {
             inhouseStruct.getInhouses().remove(foundInhouse);
             JSONLoader.saveInhouseData(inhouseStruct, event.getGuildID());
             messageChannel.sendMessage("Your group, **" + foundInhouse.getInhouseName() + "** has been disbanded.").queue();
         }
         else
         {
             messageChannel.sendMessage("You are not the owner of that group.").queue();
         }
     }
}
