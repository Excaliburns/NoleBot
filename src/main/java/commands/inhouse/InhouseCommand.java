package commands.inhouse;

import commands.util.Command;
import commands.util.CommandEvent;
import util.JSONLoader;
import util.Settings;

public class InhouseCommand extends Command {
    public InhouseCommand() {
        name = "lfg";
        description = "Looking for Group command to find and create groups within your guild!";
        helpDescription = "This command is used to create groups for players in your guild. Deletion of groups must be carried out by the owner of the group, or a user than has 2x the permission level as this command.";
        requiredPermission = 100;
        usages.add("list");
        usages.add("joined");
        usages.add("join <name>");
        usages.add("create <name> <# of players>");
        usages.add("create <name> <# of players> [Duration in hours]");
        usages.add("leave <name>");
        usages.add("delete <name>");
    }

    @Override
    public void onCommandReceived(CommandEvent event) {
        Settings guildSettings = event.getSettings();
        String[] eventMessage = event.getMessage().clone();
        InhouseStruct inhouseStruct = JSONLoader.inhouseLoader(guildSettings);

        if (eventMessage[1] == null || eventMessage[1].equals("list"))
            new ListGroupCommand(event, inhouseStruct);

        else {

        String message = eventMessage[1];
            String[] args = message.split("\\s");

            if (args[0].trim().equals("create"))
                new GroupCreateCommand(event, args, inhouseStruct);
            else if(inhouseStruct.getInhouses() == null)
                new ListGroupCommand(event, inhouseStruct);
            else if(args[0].trim().equals("joined"))
                new JoinedGroupsCommand(event, inhouseStruct);
            else if (args[0].trim().equals("join"))
                new JoinGroupCommand(event, args, inhouseStruct);
            else if (args[0].trim().equals("leave"))
                new LeaveGroupCommand(event, args, inhouseStruct);
            else if (args[0].trim().equals("delete"))
                new DeleteGroupCommand(event, args, inhouseStruct);
        }
    }
}
