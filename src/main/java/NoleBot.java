import commands.admin.ServerInfoCommand;
import commands.admin.VerifyMe;
import commands.admin.permissions.*;
import commands.games.rps;
import commands.general.*;
import commands.inhouse.GroupCommand;
import commands.inhouse.GroupSetupCommand;
import commands.manager.AddRole;
import commands.manager.DelRole;
import commands.manager.PurgeAll;
import commands.util.CommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import special.FSUVerify;
import special.SpecialListener;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import util.PropLoader;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/*
NoleBot main class. Here the bot is initialized by creating a new JDA instance and adding our own CommandListener.

We implement our own CommandListener because by default, JDA forces each command to use their own ListenerAdapter. See CommandListener for more details.
 */
public class NoleBot {
    private static final CommandListener commandListener = new CommandListener();
    private static final ListenerAdapter specialListener = new SpecialListener();
    private static final ListenerAdapter FSUVerify = new FSUVerify();

    public static void main(String[] args) throws Exception {
        initBot();
    }

    private static void initBot() throws LoginException {


        JDA jda = new JDABuilder(PropLoader.getProp("token")).addEventListeners(commandListener, FSUVerify ,specialListener).build();

        commandListener.addCommand(new Help());
        commandListener.addCommand(new Info());
        commandListener.addCommand(new Ping());
        commandListener.addCommand(new VerifyMe());

        commandListener.addCommand(new rps());

        commandListener.addCommand(new GroupCommand());
        commandListener.addCommand(new GroupSetupCommand());

        commandListener.addCommand(new ServerInfoCommand());
        commandListener.addCommand(new UserInfoCommand());

        commandListener.addCommand(new AddRole());
        commandListener.addCommand(new DelRole());
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
