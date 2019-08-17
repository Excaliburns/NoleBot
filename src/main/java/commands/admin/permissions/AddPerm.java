package commands.admin.permissions;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import util.RoleHelper;
import util.Settings;

import java.util.List;
import java.util.Optional;

public class AddPerm extends Command {
    public AddPerm() {
        name = "addperm";
        description = "Adds a role to the permission list.";
        helpDescription = "Used to set a permission level for a specific role. This value is saved between bot resets. You MUST use a mentionable role when using this command. If the role already has a permission level, you can also change it using this command.";
        usages.add("addperm <@Role> [@Role, as many as you want] <PermissionLevel>");
        requiredPermission = 1000;
    }

    //TODO::Figure out a way to do this better without duplicating code.
    @SuppressWarnings("Duplicates")
    @Override
    public void onCommandReceived(CommandEvent event) {
        PermissionUtils permissionUtils = new PermissionUtils();
        String[] args = event.getMessage();
        Message message = event.getEvent().getMessage();
        List<Role> roleList = message.getMentionedRoles();
        Settings settings = event.getSettings();
        List<RoleHelper> roleHelperList = settings.getRoleHelper();
        int userPermission = event.getUserPermLevel();
        int permission;

        if (args[1] == null) {
            event.getChannel().sendMessage("Incorrect syntax! Please use " + settings.getPrefix() + "help addperm").queue();
            return;
        }

        for (Role i : roleList)
            args[1] = args[1].replace(i.getAsMention(), "");

        try {
            args[1] = args[1].trim();
            permission = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println(args[1]);
            event.getChannel().sendMessage("Message is improperly formatted.").queue();
            return;
        }

        if (userPermission >= permission || userPermission == roleHelperList.get(0).getPermID()) {
            if (!roleList.isEmpty()) roleList.forEach(e -> {

                RoleHelper roleHelper = new RoleHelper(e, permission);
                Optional<RoleHelper> optionalRoleHelper = roleHelperList.stream().filter(c -> c.getRoleID().equals(roleHelper.getRoleID())).findAny();
                if (optionalRoleHelper.isPresent()) {
                    if (userPermission >= optionalRoleHelper.get().getPermID()) {
                        for (int i = 0; i < roleHelperList.size(); i++) {
                            if (optionalRoleHelper.get().getRoleID().equals(roleHelperList.get(i).getRoleID())) {

                                roleHelperList.set(i, roleHelper);
                                permissionUtils.saveData(roleHelperList, settings, event);

                                event.getChannel().sendMessage("Role: **" + e.getName() + "** with permission level: **" + optionalRoleHelper.get().getPermID() + " **" + " has been updated to permission level: **" + roleHelper.getPermID() + "**").queue();
                                return;
                            }
                        }
                    } else {
                        event.getChannel().sendMessage("You cannot set the permission level of a role that has a higher permission level than you! \nYour highest permission level: **" + userPermission + "**\nRequired Permission: **" + optionalRoleHelper.get().getPermID() + "**").queue();
                        return;
                    }
                }

                roleHelperList.add(roleHelper);
                permissionUtils.saveData(roleHelperList, settings, event);

                event.getChannel().sendMessage("Saved Role: **" + e.getName() + "** with permission level: **" + roleHelper.getPermID() + "**").queue();
            });
            else
                event.getChannel().sendMessage("You did not mention a role. Use !help addperm for more information.").queue();
        } else {
            event.getChannel().sendMessage("You cannot assign the permission level of a role higher than your own! \nYour highest permission level: **" + userPermission + "**\nRequired Permission: **" + permission + "**").queue();
        }
    }
}
