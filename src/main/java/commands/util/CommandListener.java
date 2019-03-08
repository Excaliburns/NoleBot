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
import util.UserHelper;

import java.util.*;

/*
CommandListener adapter. Here we both store the commands that we have initialized as well as pass on our parsed CommandMessage to our CommandEvent handler.

This class also helps with loading / creating and sorting Guild JSONs when the bot instance is started.
TODO:: Separate creation / loading of GuildJSON into method called from both onReady and onGuildJoin, currently if bot joins a guild and is not restarted, their JSON is never loaded, and can cause problems.
 */
public class CommandListener extends ListenerAdapter {
    private final ArrayList<Command> commands;
    private final HashMap<String, Integer> commandIndex;
    private HashMap<String, Settings> settingsHashMap;

    //Initialize our Command list as well as an index for each command.
    public CommandListener() {
        commands = new ArrayList<>();
        commandIndex = new HashMap<>();
        settingsHashMap = new HashMap<>();
    }

    public HashMap<String, Settings> getSettingsHashMap() {
        return settingsHashMap;
    }

    public void setSettingsHashMap(HashMap<String, Settings> settingsHashMap) {
        this.settingsHashMap = settingsHashMap;
    }

    public void addCommand(Command command) {
        String name = command.getName();

        //Reading from and adding to the index must be synchronized, as we must execute the correct command.
        //If this was not thread safe, commands could be added / removed from the index at incorrect positions.
        synchronized (commandIndex) {

            if (commandIndex.containsKey(name))
                throw new IllegalArgumentException("Cannot add a command that has already been added.");

            commandIndex.put(name, commands.size());
        }
        System.out.println(commandIndex);
        commands.add(command);
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;

        String[] commandEventMessage;

        String message = event.getMessage().getContentRaw();
        Settings settings = this.settingsHashMap.get(event.getGuild().getId());

        //Currently message is only parsed further if the message stars with the guild's custom prefix
        if (message.startsWith(settings.getPrefix())) {
            commandEventMessage = Arrays.copyOf(message.substring(1).trim().split("\\s+", 2), 2);

            System.out.println(commandEventMessage.length);

            String commandTitle = commandEventMessage[0].toLowerCase();
            final Command command;

            // Get the command that we want to run from the CommandIndex. If the message does not contain a valid command, return null.
            synchronized (commandIndex) {
                int i = commandIndex.getOrDefault(commandTitle, -1);
                command = i != -1 ? commands.get(i) : null;
            }

            //Pass command to CommandEvent if there is one.
            if (command != null) {
                /*
                Currently, this method is how we determine the permissions of the user that sent the command.

                1. For each RoleHelper object that was loaded from the settings, add the RoleID of each role and then the RoleHelper object itself to a HashMap.
                2. Then, get a list of Roles from the User that sent the message.
                3. For each role in that list, check if the RoleHelperHashMap contains that role.
                4. If it does, get the PermissionID from the RoleHelperHashMap and check it against the command that we want to execute.

                Currently, we sort the RoleHelper List in the onReady() event. Possibly we can use this to shorten the search time in the future if we see a decrease in responsiveness across a large amount of servers.
                This search is linear.
                 */
                List<RoleHelper> guildRoles = settings.getRoleHelper();
                List<Role> userRoles = event.getMember().getRoles();

                        if (UserHelper.getHighestUserPermission(userRoles, guildRoles) >= command.getRequiredPermission())
                        {
                            CommandEvent commandEvent = new CommandEvent(event, commandEventMessage, this);
                            command.execute(commandEvent);
                        }
                        else
                            messageError(event, "Not a high enough permission level");
            }
        }
    }

    // This is called after the JDA object called in initBot() is successfully built.
    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("Initializing Settings...");

        JDA jda = event.getJDA();
        //Iterate through every guild that the bot is a part of.
        jda.getGuilds().forEach(guild -> {

            //If any of those guilds don't have a corresponding JSON, create it.
            if (!JSONLoader.doesSettingExist(guild.getId()))
                JSONLoader.createGuildJSON(guild.getId());

            //Load the JSON settings from the loaded guilds. If the guild's JSON had just been created, then this simply loads the defaults.
            Settings settings = JSONLoader.getGuildSettings(guild.getId());

            //This is for initializing a new guild with default permissions.
            List<RoleHelper> importantRoles = new ArrayList<>();

            if (!settings.isInit()) {
                //If there are roles in the guild with the Administrator permission, then they automatically receive a permission level of 1000.
                guild.getRoles().forEach(role -> {
                    if (role.hasPermission(Permission.ADMINISTRATOR)) {
                        RoleHelper roleHelper = new RoleHelper(role.getId(), role.getName(), 1000);
                        importantRoles.add(roleHelper);
                    }
                });
                settings = new Settings(guild.getId(), importantRoles);

            }
            //Sort the RoleHelper objects by PermissionID before we store them.
            settings.getRoleHelper().sort(Comparator.comparing(RoleHelper::getPermID).reversed());

            //Store the HashMap locally to use so we don't have to pull from the disk every time.
            this.settingsHashMap.put(guild.getId(), settings);

            //Save the settings to JSON.
            JSONLoader.saveGuildSettings(settings);
        });
        System.out.println("Settings done initializing.");
    }

    //Method for sending error messages.
    private void messageError(MessageReceivedEvent event, String reason) {
        event.getChannel().sendMessage("Command failed for reason: " + reason).queue();
    }
}
