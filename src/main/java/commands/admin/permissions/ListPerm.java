package commands.admin.permissions;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Role;
import util.RoleHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListPerm extends Command {
    public ListPerm() {
        name = "listperm";
        description = "Lists permission levels for your server.";
        helpDescription = "Sends out a list of roles and their corresponding permission levels. Can also be used to check the permission level of a specific role.";
        usages.add("listperm");
        usages.add("listperm [@Role]");
        usages.add("listperm commands");
        requiredPermission = 500;
    }

    @Override
    public void onCommandReceived(CommandEvent event) {
        String[] args = event.getMessage();

        List<RoleHelper> roleHelpers = event.getSettings().getRoleHelper();
        List<Role> roleList = event.getEvent().getMessage().getMentionedRoles();

        if (!roleList.isEmpty()) {

            roleList.forEach(role -> {
                Optional<RoleHelper> optionalRoleHelper = roleHelpers.stream().filter(f -> f.getRoleID().equals(role.getId())).findFirst();
                if (optionalRoleHelper.isPresent()) {
                    event.getChannel().sendMessage("Role: **" + role.getName() + "** has permission level: **" + optionalRoleHelper.get().getPermID() + "**.").queue();
                } else {
                    event.getChannel().sendMessage("Did not find role: **" + role.getName() + "** in the permission list.").queue();
                }
            });
        } else if (args[1] == null) {
            MessageBuilder messageBuilder = new MessageBuilder();
            for (RoleHelper role : roleHelpers) {
                messageBuilder.append(role.getRoleName(), MessageBuilder.Formatting.BOLD);
                messageBuilder.append(" has permission level: ");
                messageBuilder.append(Integer.toString(role.getPermID()), MessageBuilder.Formatting.BOLD);
                messageBuilder.append("\n");
            }

            event.getChannel().sendMessage(messageBuilder.build()).queue();
        } else if (args[1].trim().equalsIgnoreCase("commands")) {
            ArrayList<Command> commandList = event.getCommandListener().getCommands();
            MessageBuilder messageBuilder = new MessageBuilder();

            for (Command c : commandList) {
                messageBuilder.append(c.getName(), MessageBuilder.Formatting.BOLD);
                messageBuilder.append(" requires permission level: ");
                messageBuilder.append(Integer.toString(c.getRequiredPermission()), MessageBuilder.Formatting.BOLD);
                messageBuilder.append("\n");
            }

            event.getChannel().sendMessage(messageBuilder.build()).queue();
        } else {
            event.getChannel().sendMessage("Incorrect syntax! Use !help listperm for more help.").queue();
        }
    }
}
