import commands.admin.ServerInfoCommand;
import commands.admin.permissions.*;
import commands.games.rps;
import commands.general.*;
import commands.inhouse.InhouseCommand;
import commands.inhouse.InhouseSetupCommand;
import commands.manager.AddRole;
import commands.manager.PurgeAll;
import commands.util.CommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import special.SpecialListener;
import util.PropLoader;

import javax.security.auth.login.LoginException;

/*
NoleBot main class. Here the bot is initialized by creating a new JDA instance and adding our own CommandListener.

We implement our own CommandListener because by default, JDA forces each command to use their own ListenerAdapter. See CommandListener for more details.
 */
public class NoleBot {
    private static final CommandListener commandListener = new CommandListener();
    private static final ListenerAdapter specialListener = new SpecialListener();

    public static void main(String[] args) throws LoginException {
        initBot();
    }

    private static void initBot() throws LoginException {
        JDA jda = new JDABuilder(new PropLoader().getProp("token")).addEventListeners(commandListener, specialListener).build();

        commandListener.addCommand(new Help());
        commandListener.addCommand(new Info());
        commandListener.addCommand(new Ping());

        commandListener.addCommand(new rps());

        commandListener.addCommand(new InhouseCommand());
        commandListener.addCommand(new InhouseSetupCommand());

        commandListener.addCommand(new ServerInfoCommand());
        commandListener.addCommand(new UserInfoCommand());

        commandListener.addCommand(new AddRole());
        commandListener.addCommand(new PurgeAll());

        commandListener.addCommand(new AddPerm());
        commandListener.addCommand(new DelPerm());
        commandListener.addCommand(new ListPerm());
        commandListener.addCommand(new CommandPerm());
        commandListener.addCommand(new Prefix());
        commandListener.addCommand(new BanRole());
        commandListener.addCommand(new VerifyRole());
    }
}
