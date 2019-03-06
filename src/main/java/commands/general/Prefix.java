package commands.general;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.MessageChannel;
import util.JSONLoader;
import util.Settings;

import javax.annotation.Nullable;
import java.util.HashMap;

public class Prefix extends Command {

    public Prefix() {
        name = "prefix";
        description = "Sets a custom prefix for your server";
        requiredPermission = 1000;
    }

    @Override
    public void onCommandReceived(CommandEvent event) {
        String guildID = event.getEvent().getGuild().getId();
        Settings settings = JSONLoader.getGuildSettings(guildID);
        String prefix;
        MessageChannel messageChannel = event.getEvent().getChannel();
        String[] prefixMessage = event.getMessage();

        prefix = prefixMessage[1];

        if (prefix.length() > 1) {
            messageChannel.sendMessage("Your prefix cannot be longer than one character.").queue();
            return;
        }

        messageChannel.sendMessage("Your prefix has been set to: " + prefix).queue();
        settings.setPrefix(prefix);

        JSONLoader.saveGuildSettings(settings);

        HashMap<String, Settings> updatedSettingsHashMap = event.getCommandListener().getSettingsHashMap();
        updatedSettingsHashMap.remove(guildID);
        updatedSettingsHashMap.put(guildID, settings);

        event.getCommandListener().setSettingsHashMap(updatedSettingsHashMap);
    }
}
