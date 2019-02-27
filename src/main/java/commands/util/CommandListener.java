package commands.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import util.JSONLoader;
import util.PropLoader;
import util.RoleHelper;
import util.Settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CommandListener extends ListenerAdapter
{
    private final ArrayList<Command> commands;
    private final HashMap<String, Integer> commandIndex;

    public CommandListener()
    {
        commands = new ArrayList<>();
        commandIndex = new HashMap<>();
    }

    public void addCommand(Command command)
    {
        String name = command.getName();

        synchronized (commandIndex) {

            if (commandIndex.containsKey(name))
                throw new IllegalArgumentException("Cannot add a command that has already been added.");

            commandIndex.put(name, commands.size());
        }

        commands.add(command);
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if(event.getAuthor().isBot())
            return;

        String[] commandEventMessage = null;

        String message = event.getMessage().getContentRaw();

        if(message.startsWith(Settings.getSettings(event.getGuild()).getPrefix()))
        {
            commandEventMessage = Arrays.copyOf(message.substring(1).trim().split("\\s+", 2), 2);
            String commandTitle = commandEventMessage[0].toLowerCase();
            final Command command;

            synchronized (commandIndex)
            {
                int i = commandIndex.getOrDefault(commandTitle, -1);
                command = i != -1 ? commands.get(i) : null;
            }

            if (command != null)
            {
                Settings settings = JSONLoader.getGuildSettings(event.getGuild().getId());
                HashMap<String, RoleHelper> roleHelperList = new HashMap<>();

                settings.getRoleHelper().forEach(roleHelper ->
                    roleHelperList.put(roleHelper.getRoleID(), roleHelper)
                );

                List<Role> roleList = event.getMember().getRoles();
                boolean commandExecuted = false;
                for(Role role: roleList)
                {
                    if(roleHelperList.containsKey(role.getId()))

                        if(roleHelperList.get(role.getId()).getPermID() >= command.getRequiredPermission())
                        {
                            CommandEvent commandEvent = new CommandEvent(event, commandEventMessage, this);
                            command.execute(commandEvent);
                            commandExecuted = true;
                            break;
                        }
                }
                if(!commandExecuted)
                    messageError(event, "Not a high enough permission level");
            }
        }
    }

    @Override
    public void onReady(ReadyEvent event)
    {
        System.out.println("Initializing Settings");

        JDA jda = event.getJDA();
        jda.getGuilds().forEach(guild -> {

            if(!JSONLoader.doesSettingExist(guild.getId()))
                JSONLoader.createGuildJSON(guild.getId());

            Settings settings = JSONLoader.getGuildSettings(guild.getId());
            List<RoleHelper> importantRoles = new ArrayList<>();

            if(!settings.isInit()) {
                guild.getRoles().forEach(role -> {
                    if (role.hasPermission(Permission.ADMINISTRATOR)) {
                        RoleHelper roleHelper = new RoleHelper(role.getId(), role.getName(), 1000);
                        importantRoles.add(roleHelper);
                    }
                });
                settings = new Settings(guild.getId(), importantRoles);
                JSONLoader.saveGuildSettings(settings);
            }
    });
        System.out.println("Settings done initializing.");
    }

    private void messageError(MessageReceivedEvent event, String reason)
    {
        event.getChannel().sendMessage("Command failed for reason: " + reason).queue();
    }
}
