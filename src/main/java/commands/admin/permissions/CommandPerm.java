package commands.admin.permissions;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import util.JSONLoader;
import util.Settings;

import java.util.HashMap;

public class CommandPerm extends Command {
    public CommandPerm() {
        name = "commandperm";
        description = "Sets the required permission level to use a command";
        helpDescription = "Sets the permission level for a command. This value is saved between restarts. You must type the name of the command exactly, without your prefix. Use !help for a list of commands. It is recommended that this command's permission level stays equal to your server's highest. Use !listperm for these values.";
        usages.add("commandperm <Command Name> <permission level>");
        requiredPermission = 1000;
    }

    @Override
    public void onCommandReceived(CommandEvent event)
    {
        String[] args = event.getMessage();
        MessageChannel messageChannel = event.getEvent().getChannel();
        String commandName;
        int permission;

        if (args[1] != null) {
            String[] message = args[1].split("\\s", 2);
            try{
                permission = Integer.parseInt(message[1]);
            }catch(NumberFormatException e)
            {
                messageChannel.sendMessage("You did not input a valid permission level. Use !help commandperm for instructions.").queue();
                return;
            }

            if (message.length > 2) {
                messageChannel.sendMessage("Too many arguments! Use !help commandperm for instructions.").queue();
                return;
            }

            HashMap<String, Integer> commandIndex = event.getCommandListener().getCommandIndex();
            commandName = message[0].toLowerCase().trim();

            int i = commandIndex.getOrDefault(commandName, -1);
            if (i != -1) {
                Settings guildSettings = event.getCommandListener().getSettingsHashMap().get(event.getGuildID());
                HashMap<String, Integer> commandHelper = guildSettings.getCommandHelper();

                if(commandHelper == null)
                {
                    messageChannel.sendMessage("Did not find custom command permissions. Creating now.").queue();
                    commandHelper = new HashMap<>();
                    guildSettings.setCommandHelper(commandHelper);
                }

                messageChannel.sendMessage("Setting: **" + commandName + "** to required permission level: **" + permission + "**.").queue();
                guildSettings.getCommandHelper().put(commandName, permission);

                event.getCommandListener().getSettingsHashMap().put(event.getGuildID(), guildSettings);
                JSONLoader.saveGuildSettings(guildSettings);
            } else {
                messageChannel.sendMessage("Command" + message[0] + "not found!").queue();
            }
        } else {
            messageChannel.sendMessage("You did not input a command.").queue();
        }

    }
}
