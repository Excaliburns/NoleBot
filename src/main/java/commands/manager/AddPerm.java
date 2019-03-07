package commands.manager;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import util.JSONLoader;
import util.RoleHelper;
import util.Settings;

import java.util.List;
import java.util.Optional;

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

        for (Role i : roleList)
            args[1] = args[1].replace(i.getAsMention(), "");

        try {
            args[1] = args[1].trim();
            permission = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            event.getChannel().sendMessage("Message is improperly formatted.").queue();
            return;
        }
        if (!roleList.isEmpty())
            roleList.forEach(e ->
            {
                Settings settings = event.getSettings();
                List<RoleHelper> roleHelperList = settings.getRoleHelper();
                RoleHelper roleHelper = new RoleHelper(e, permission);

                Optional<RoleHelper> optionalRoleHelper = roleHelperList.stream().filter( c -> c.getRoleID().equals(roleHelper.getRoleID())).findAny();

                System.out.println(optionalRoleHelper);
                if(optionalRoleHelper.isPresent())
                    for (int i = 0; i < roleHelperList.size(); i++) {
                        if (roleHelper == roleHelperList.get(i)) {
                            roleHelperList.set(i, roleHelper);
                            settings.setRoleHelper(roleHelperList);
                            event.getCommandListener().getSettingsHashMap().put(event.getGuildID(), settings);
                            JSONLoader.saveGuildSettings(settings);
                            event.getChannel().sendMessage("Updated Role: *" + e.getName() + "* with permission:*" + roleHelper.getPermID()).queue();
                            return;
                        }
                    }

                roleHelperList.add(roleHelper);
                settings.setRoleHelper(roleHelperList);
                event.getCommandListener().getSettingsHashMap().put(event.getGuildID(), settings);

                JSONLoader.saveGuildSettings(settings);

                event.getChannel().sendMessage("Saved Role : *" + e.getName() + "* with permission:*" + roleHelper.getPermID() + "*.").queue();
            });
        else
            event.getChannel().sendMessage("You did not mention a role. Use !help addperm for more information.").queue();
    }
}
