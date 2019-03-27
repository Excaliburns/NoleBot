package commands.admin.permissions;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import util.JSONLoader;
import util.Settings;

import java.util.List;

public class BanRole extends Command {
    public BanRole() {
        name = "banrole";
        description = "Bans a role from being assigned through addrole.";
        helpDescription = "This command allows admins to ban a role from being assigned by !addrole. Useful for if you need to restrict specific roles from being assigned, outside of the default permission values." +
                "\nIf the role is already present in the banned role list, this command removes it." +
                "\nUse with the argument list to see the banned roles.";
        usages.add("banrole <@Role> [@Role, as many as you want]");
        requiredPermission = 1000;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void onCommandReceived(CommandEvent event) {
        String[] args = event.getMessage();
        Message message = event.getEvent().getMessage();
        MessageChannel messageChannel = event.getEvent().getChannel();
        List<Role> roleList = message.getMentionedRoles();

        if (args[1] == null) {
            messageChannel.sendMessage("Incorrect syntax! Please use !help banrole.").queue();
        } else if (roleList.isEmpty() && !args[1].trim().equals("list")) {
            messageChannel.sendMessage("You did not mention any roles!").queue();
        } else if (args[1].trim().equals("list")) {
            Settings settings = event.getSettings();
            if (settings.getBannedRoles().isEmpty())
                messageChannel.sendMessage("This Guild does not have any banned roles.").queue();
            else {
                List<String> bannedRoles = settings.getBannedRoles();
                MessageBuilder messageBuilder = new MessageBuilder();

                messageBuilder.append("Banned Roles:", MessageBuilder.Formatting.BOLD, MessageBuilder.Formatting.UNDERLINE);

                for (String b : bannedRoles) {
                    Role role = event.getGuild().getRoleById(b);
                    messageBuilder.appendFormat("\n" + role.getName());
                }

                messageChannel.sendMessage(messageBuilder.build()).queue();
            }
        } else {
            Settings settings = event.getSettings();
            MessageBuilder messageBuilder = new MessageBuilder();
            roleList.forEach(role -> {
                if (settings.getBannedRoles().indexOf(role.getId()) == -1) {
                    settings.getBannedRoles().remove(role.getId());
                    messageBuilder.appendFormat("Removed **" + role.getName() + "** from the banned roles list.");
                }

                settings.getBannedRoles().add(role.getId());
                messageBuilder.appendFormat("Added **" + role.getName() + "** to the banned roles list.");
            });
            messageChannel.sendMessage(messageBuilder.build()).queue();
            event.getCommandListener().getSettingsHashMap().put(event.getGuildID(), settings);
            JSONLoader.saveGuildSettings(settings);
        }
    }
}
