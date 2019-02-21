import commands.AssignRank;
import commands.HelloCommand;
import commands.ServerInfoCommand;
import commands.UserInfoCommand;

import util.JSONLoader;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import util.PropLoader;

import javax.security.auth.login.LoginException;
import java.io.FileInputStream;
import java.util.Properties;

public class NoleBot implements EventListener
{
    public static void main(String[] args) throws LoginException
    {
            JDA jda = new JDABuilder(new PropLoader().getProp("token"))
                    .addEventListeners(new NoleBot(),
                            new HelloCommand(),
                            new ServerInfoCommand(),
                            new UserInfoCommand(),
                            new AssignRank(), new JSONLoader())
                    .build();
    }

    public void onEvent(GenericEvent event)
    {
        if(event instanceof ReadyEvent)
            System.out.println("API is ready!");
    }
}
