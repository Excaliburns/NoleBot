package commands.admin.permissions;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import util.JSONLoader;
import util.Settings;

import java.util.List;

public class VerifiedRoles extends Command {
    public VerifiedRoles()
    {
        name = "verifiedroles";
        description = "Sets the verified roles for your server.";
        helpDescription = "Sets the verified roles for your server. These roles are used in Addrole, where users need to be \"Verified\" before they can be assigned a role.";
        usages.add("verifiedroles <@Role> [@Role, as many as you want]");
        requiredPermission = 1000;
    }

    @Override
    public void onCommandReceived(CommandEvent event) {
        String[] args = event.getMessage();
        Message message = event.getEvent().getMessage();
        MessageChannel messageChannel = event.getChannel();
        List<Role> roleList = message.getMentionedRoles();

        if (args[1] == null) {
            messageChannel.sendMessage("Incorrect arguments! Please use !help verifiedroles.").queue();
        } else if (roleList.isEmpty()) {
            messageChannel.sendMessage("You did not mention any roles!").queue();
        } else {
            Settings settings = event.getSettings();
            roleList.forEach(role -> settings.getVerifiedRoles().add(role.getId()));
            event.getCommandListener().getSettingsHashMap().put(event.getGuildID(), settings);
            JSONLoader.saveGuildSettings(settings);
        }
    }
}
