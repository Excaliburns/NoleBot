package commands.inhouse;

import commands.util.Command;
import commands.util.CommandEvent;
import util.JSONLoader;
import util.Settings;

public class GroupCommand extends Command {
    public GroupCommand() {
        name = "lfg";
        description = "Looking for Group command to find and create groups within your guild!";
        helpDescription = "This command is used to create groups for players in your guild. Deletion of groups must be carried out by the owner of the group, or a user than has 2x the permission level as this command.";
        requiredPermission = 100;
        usages.add("lfg list");
        usages.add("lfg joined");
        usages.add("lfg join <name>");
        usages.add("lfg create <name> <# of players>");
        usages.add("lfg create <name> <# of players> [Duration in hours]");
        usages.add("lfg leave <name>");
        usages.add("lfg delete <name>");
    }

    @Override
    public void onCommandReceived(CommandEvent event) {
        Settings guildSettings = event.getSettings();
        String[] eventMessage = event.getMessage().clone();
        GroupStruct groupStruct = JSONLoader.inhouseLoader(guildSettings);

        if(groupStruct == null) {
            event.getChannel().sendMessage("Something went terribly wrong creating your Guild's inhouse file. Please contact your Guild admins and report this bug.").queue();
            return;
        }

        if (eventMessage[1] == null || eventMessage[1].equals("list"))
            new ListGroupCommand(event, groupStruct);

        else {

        String message = eventMessage[1].toLowerCase();
            String[] args = message.split("\\s");

            if (args[0].trim().equals("create"))
                new GroupCreateCommand(event, args, groupStruct);
            else if(groupStruct.getGroups() == null)
                new ListGroupCommand(event, groupStruct);
            else if(args[0].trim().equals("joined"))
                new JoinedGroupsCommand(event, groupStruct);
            else if (args[0].trim().equals("join"))
                new JoinGroupCommand(event, args, groupStruct);
            else if (args[0].trim().equals("leave"))
                new LeaveGroupCommand(event, args, groupStruct);
            else if (args[0].trim().equals("delete"))
                new DeleteGroupCommand(event, args, groupStruct);
        }
    }
}
