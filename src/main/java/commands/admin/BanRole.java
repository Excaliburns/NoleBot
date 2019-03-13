package commands.admin;

import commands.util.Command;
import commands.util.CommandEvent;
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
        helpDescription = "This command allows admins to ban a role from being assigned by !addrole. Useful for if you need to restrict specific roles from being assigned, outside of the default permission values.";
        usages.add("banrole <@Role> [@Role, as many as you want]");
        requiredPermission = 1000;
    }

    @Override
    public void onCommandReceived(CommandEvent event) {
        String[] args = event.getMessage();
        Message message = event.getEvent().getMessage();
        MessageChannel messageChannel = event.getEvent().getChannel();
        List<Role> roleList = message.getMentionedRoles();

        if (args[1] == null) {
            messageChannel.sendMessage("Incorrect arguments! Please use !help banrole.").queue();
        } else if (roleList.isEmpty()) {
            messageChannel.sendMessage("You did not mention any roles!").queue();
        } else {
            Settings settings = event.getSettings();
            roleList.forEach(role -> settings.getBannedRoles().add(role.getId()));
            event.getCommandListener().getSettingsHashMap().put(event.getGuildID(), settings);
            JSONLoader.saveGuildSettings(settings);
        }
    }
}
