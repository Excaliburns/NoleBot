import commands.Ping;
import commands.ServerInfoCommand;
import commands.util.CommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import util.PropLoader;

import javax.security.auth.login.LoginException;

public class NoleBot implements EventListener
{
    private static final CommandListener client = new CommandListener();

    public static void main(String[] args) throws LoginException
    {
        initBot();
    }

    private static void initBot() throws LoginException
    {
        JDA jda = new JDABuilder(new PropLoader().getProp("token"))
                .addEventListeners(client)
                .build();

        client.addCommand(new Ping());
        client.addCommand(new ServerInfoCommand());
    }


    public void onEvent(GenericEvent event)
    {
        if(event instanceof ReadyEvent)
            System.out.println("API is ready!");
    }
}
