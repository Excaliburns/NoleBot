import commands.admin.RoleMembers;
import commands.admin.ServerInfoCommand;
import commands.admin.VerifyMe;
import commands.admin.attendance.Attendance;
import commands.admin.attendance.AttendancePass;
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
import net.dv8tion.jda.api.requests.GatewayIntent;
import special.PrivateMessageListener;
import special.SpecialListener;
import util.DBUtils;
import util.PropLoader;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.util.ArrayList;

/*
NoleBot main class. Here the bot is initialized by creating a new JDA instance and adding our own CommandListener.

We implement our own CommandListener because by default, JDA forces each command to use their own ListenerAdapter. See CommandListener for more details.
 */
public class NoleBot {
    private static final CommandListener commandListener = new CommandListener();
    private static final ListenerAdapter specialListener = new SpecialListener();
    private static final ListenerAdapter FSUVerify = new PrivateMessageListener();

    public static void main(String[] args) throws Exception {
        initBot();
    }

    private static void initBot() throws LoginException {

        JDA jda = JDABuilder
                .create(PropLoader.getProp("token"), GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.GUILD_VOICE_STATES)
                .addEventListeners(commandListener, FSUVerify ,specialListener)
                .build();

        commandListener.addCommand(new Help());
        commandListener.addCommand(new Info());
        commandListener.addCommand(new Ping());
        commandListener.addCommand(new VerifyMe());

        commandListener.addCommand(new rps());
        commandListener.addCommand(new Counter());

        commandListener.addCommand(new GroupCommand());
        commandListener.addCommand(new GroupSetupCommand());

        commandListener.addCommand(new ServerInfoCommand());
        commandListener.addCommand(new UserInfoCommand());

        commandListener.addCommand(new AddRole());
        commandListener.addCommand(new DelRole());
        commandListener.addCommand(new PurgeAll());
        commandListener.addCommand(new RoleMembers());

        commandListener.addCommand(new AddPerm());
        commandListener.addCommand(new DelPerm());
        commandListener.addCommand(new ListPerm());
        commandListener.addCommand(new CommandPerm());
        commandListener.addCommand(new Prefix());
        commandListener.addCommand(new BanRole());
        commandListener.addCommand(new VerifyRole());
        commandListener.addCommand(new Attendance());

        commandListener.addCommand(new AttendancePass());
        AttendancePass.initAttendanceMap();

        try {
            DBUtils.initConnection();
        } catch (SQLException e) {
            System.out.println("Could not initialize database connection! Attendance commands will not work. Check properties.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("No Mysql Driver Found!");
            System.out.println("Exception: " + e.getMessage());
        }
    }
}
