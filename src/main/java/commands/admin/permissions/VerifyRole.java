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

public class VerifyRole extends Command {
    public VerifyRole() {
        name = "verifyrole";
        description = "Sets the verified roles for your server.";
        helpDescription = "Sets the verified roles for your server. These roles are used in Addrole, where users need to be \"Verified\" before they can be assigned a role." +
                "\nIf the role is already present in the verified role list, this command removes it." +
                "\nUse with the argument list to see the banned roles.";
        usages.add("verifyrole <@Role> [@Role, as many as you want]");
        requiredPermission = 1000;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void onCommandReceived(CommandEvent event) {
        String[] args = event.getMessage();
        Message message = event.getEvent().getMessage();
        MessageChannel messageChannel = event.getChannel();
        List<Role> roleList = message.getMentionedRoles();

        if (args[1] == null) {
            messageChannel.sendMessage("Incorrect arguments! Please use " + event.getSettings().getPrefix() + "verifyrole.").queue();
        } else if (roleList.isEmpty() && !args[1].trim().equals("list")) {
            messageChannel.sendMessage("You did not mention any roles!").queue();
        } else if (args[1].trim().equals("list")) {
            Settings settings = event.getSettings();
            if (settings.getVerifiedRoles().isEmpty())
                messageChannel.sendMessage("This Guild does not have any verified roles.").queue();
            else {
                List<String> verifiedRoles = settings.getVerifiedRoles();
                MessageBuilder messageBuilder = new MessageBuilder();

                messageBuilder.append("Verified Roles:", MessageBuilder.Formatting.BOLD, MessageBuilder.Formatting.UNDERLINE);

                for (String v : verifiedRoles) {
                    Role role = event.getGuild().getRoleById(v);
                    messageBuilder.appendFormat("\n" + role.getName());
                }

                messageChannel.sendMessage(messageBuilder.build()).queue();
            }
        } else {
            Settings settings = event.getSettings();
            MessageBuilder messageBuilder = new MessageBuilder();
            roleList.forEach(role -> {
                if (settings.getVerifiedRoles().indexOf(role.getId()) != -1) {
                    settings.getVerifiedRoles().remove(role.getId());
                    messageBuilder.appendFormat("Removed **" + role.getName() + "** from the verified roles list.\n");
                }
                else
                {
                    settings.getVerifiedRoles().add(role.getId());
                    messageBuilder.appendFormat("Added **" + role.getName() + "** to the verified roles list.\n");
                }
            });
            messageChannel.sendMessage(messageBuilder.build()).queue();
            event.getCommandListener().getSettingsHashMap().put(event.getGuildID(), settings);
            JSONLoader.saveGuildSettings(settings);
        }
    }
}
