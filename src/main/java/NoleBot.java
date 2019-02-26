import commands.util.Command;
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
    private static CommandListener commandListener = new CommandListener();

    public static void main(String[] args) throws LoginException
    {
            JDA jda = new JDABuilder(new PropLoader().getProp("token"))
                    .addEventListeners(commandListener)
                    .build();
    }

    public void onEvent(GenericEvent event)
    {
        if(event instanceof ReadyEvent)
            System.out.println("API is ready!");
    }
}
