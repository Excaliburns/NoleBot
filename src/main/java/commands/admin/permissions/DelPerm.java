package commands.admin.permissions;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.Role;
import util.RoleHelper;
import util.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DelPerm extends Command {
    public DelPerm() {
        name = "delperm";
        description = "Deletes a role from the permission list.";
        helpDescription = "Removes roles from the permission list. You cannot remove the role with the highest permission. Can also be used to purge all permission levels other than the top level permission(s).";
        usages.add("delperm <@Role> [@Role, as many as you want]");
        usages.add("delperm all");
        requiredPermission = 1000;
    }

    //TODO::Figure out a way to do this better without duplicating code.
    @SuppressWarnings("Duplicates")
    @Override
    public void onCommandReceived(CommandEvent event) {
        PermissionUtils permissionUtils = new PermissionUtils();
        String[] args = event.getMessage();
        List<Role> rolesMentioned = event.getEvent().getMessage().getMentionedRoles();
        Settings settings = event.getSettings();
        List<RoleHelper> roleHelperList = settings.getRoleHelper();
        int highestPerm = event.getUserPermLevel();

        if (args[1] != null && args[1].trim().equals("all")) {
            if (highestPerm >= roleHelperList.get(0).getPermID()) {
                ArrayList<RoleHelper> roleHelperArrayList = new ArrayList<>();
                for (RoleHelper role : roleHelperList) {
                    if (roleHelperList.get(0).getPermID() > role.getPermID()) {
                        event.getChannel().sendMessage("Deleted permission level for role: **" + role.getRoleName() + "**").queue();
                        roleHelperArrayList.add(role);
                    }
                }
                roleHelperList.removeAll(roleHelperArrayList);
                permissionUtils.saveData(roleHelperList, settings, event);
                event.getChannel().sendMessage("Your roles have been cleaned. Please use !listperms for a list of all current permissions.").queue();
            } else {
                event.getChannel().sendMessage("You must have the highest permission level on the server to delete all other permissions.\nYour permission level: **" + highestPerm + "**\nRequired permission level: **" + roleHelperList.get(0).getPermID() + "**.").queue();
            }
        } else {
            if (!rolesMentioned.isEmpty()) {
                rolesMentioned.forEach(role -> {
                    Optional<RoleHelper> optionalRoleHelper = roleHelperList.stream().filter(f -> f.getRoleID().equals(role.getId())).findFirst();
                    if (optionalRoleHelper.isPresent()) {
                        if (roleHelperList.get(0).getPermID() > optionalRoleHelper.get().getPermID()) {
                            roleHelperList.remove(optionalRoleHelper.get());
                            event.getChannel().sendMessage("Deleted permission level for role: **" + role.getName() + "**").queue();
                            permissionUtils.saveData(roleHelperList, settings, event);
                        } else {
                            event.getChannel().sendMessage("You cannot delete the permission level for a role that has the highest permission level on the server.").queue();
                        }
                    } else {
                        event.getChannel().sendMessage("Role: **" + role.getName() + "** does not have a permission level.").queue();
                    }
                });
            } else {
                event.getChannel().sendMessage("You did not mention a role. Use !help delperm for more information.").queue();
            }
        }
    }
}
