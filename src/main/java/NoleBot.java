import commands.general.Help;
import commands.general.Ping;
import commands.admin.ServerInfoCommand;
import commands.general.Prefix;
import commands.util.CommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import util.PropLoader;

import javax.security.auth.login.LoginException;
/*
NoleBot main class. Here the bot is initialized by creating a new JDA instance and adding our own CommandListener.

We implement our own CommandListener because by default, JDA forces each command to use their own ListenerAdapter. See CommandListener for more details.
 */
public class NoleBot
{
    private static final CommandListener commandListener = new CommandListener();

    public static void main(String[] args) throws LoginException
    {
        initBot();
    }

    private static void initBot() throws LoginException
    {
        JDA jda = new JDABuilder(new PropLoader().getProp("token"))
                .addEventListeners(commandListener)
                .build();

        commandListener.addCommand(new Ping());
        commandListener.addCommand(new ServerInfoCommand());
        commandListener.addCommand(new Prefix());
        commandListener.addCommand(new Help());
    }
}
