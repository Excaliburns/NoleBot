package commands.manager;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import util.RoleHelper;
import util.Settings;

import java.util.List;

public class AddPerm extends Command {
    public AddPerm() {
        name = "addperm";
        description = "Adds a role to the permission list.";
        helpDescription = "Used to set a permission level for a specific role. This value is saved between bot resets. You MUST use a mentionable role when using this command. If the role already has a permission level, you can also change it using this command.";
        usages.add("addperm <@Role> <PermissionLevel>");
        requiredPermission = 1000;
    }


    @Override
    public void onCommandReceived(CommandEvent event) {
        String[] args = event.getMessage();
        Message message = event.getEvent().getMessage();
        List<Role> roleList = message.getMentionedRoles();
        int permission;

        try {
            permission = Integer.parseInt(args[2]);
        }catch (NumberFormatException e)
        {
            event.getChannel().sendMessage("Message is improperly formatted. Exception: " + e).queue();
            return;
        }

        if (args.length > 3) {
            if (!roleList.isEmpty())
                roleList.forEach(e ->
                {
                    Settings settings = event.getSettings();
                    List<RoleHelper> roleHelperList = settings.getRoleHelper();
                    RoleHelper roleHelper = new RoleHelper(e, permission);

                    roleHelperList.add(roleHelper);

                    event.getCommandListener().getSettingsHashMap().put(event.getGuildID(), settings);

                });
            else
                event.getChannel().sendMessage("You did not mention a role. Use !help addperm for more information.").queue();
        }
        else
            event.getChannel().sendMessage("Too many arguments!").queue();
    }
}
