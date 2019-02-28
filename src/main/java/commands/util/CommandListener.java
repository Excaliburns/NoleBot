package commands.util;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import util.JSONLoader;
import util.RoleHelper;
import util.Settings;

import java.util.*;
/*
CommandListener adapter. Here we both store the commands that we have initialized as well as pass on our parsed CommandMessage to our CommandEvent handler.

This class also helps with loading / creating and sorting Guild JSONs when the bot instance is started.
TODO:: Separate creation / loading of GuildJSON into method called from both onReady and onGuildJoin, currently if bot joins a guild and is not restarted, their JSON is never loaded, and can cause problems.
 */
public class CommandListener extends ListenerAdapter
{
    private final ArrayList<Command> commands;
    private final HashMap<String, Integer> commandIndex;
    private final HashMap<String, Settings> settingsHashMap;

    //Initialize our Command list as well as an index for each command.
    public CommandListener()
    {
        commands = new ArrayList<>();
        commandIndex = new HashMap<>();
        settingsHashMap = new HashMap<>();
    }

    public void addCommand(Command command)
    {
        String name = command.getName();

        //Reading from and adding to the index must be synchronized, as we must execute the correct command.
        //If this was not thread safe, commands could be added / removed from the index at incorrect positions.
        synchronized (commandIndex)
        {

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
        Settings settings = this.settingsHashMap.get(event.getGuild().getId());

        //Currently message is only parsed further if the message stars with the guild's custom prefix
        if(message.startsWith(settings.getPrefix()))
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

            if(!settings.isInit())
            {
                guild.getRoles().forEach(role -> {
                    if (role.hasPermission(Permission.ADMINISTRATOR)) {
                        RoleHelper roleHelper = new RoleHelper(role.getId(), role.getName(), 1000);
                        importantRoles.add(roleHelper);
                    }
                });
                settings = new Settings(guild.getId(), importantRoles);

            }
            settings.getRoleHelper().sort(Comparator.comparing(RoleHelper::getPermID).reversed());

            this.settingsHashMap.put(guild.getId(), settings);
            JSONLoader.saveGuildSettings(settings);
    });
        System.out.println("Settings done initializing.");
    }

    private void messageError(MessageReceivedEvent event, String reason)
    {
        event.getChannel().sendMessage("Command failed for reason: " + reason).queue();
    }
}
