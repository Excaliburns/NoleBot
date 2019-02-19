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

import javax.security.auth.login.LoginException;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Properties;

public class NoleBot implements EventListener
{
    public static void main(String[] args) throws LoginException
    {
        //try (FileWriter fileWriter = new FileWriter("data/" + ))
            JDA jda = new JDABuilder(getKeyFromProp("token"))
                    .addEventListeners(new NoleBot(),
                            new HelloCommand(),
                            new ServerInfoCommand(),
                            new UserInfoCommand(),
                            new AssignRank())
                    .build();


    }

    public void onEvent(GenericEvent event)
    {
        if(event instanceof ReadyEvent)
            System.out.println("API is ready!");
    }

    private static String getKeyFromProp(String prop)
    {

        String botToken;
        try
        {
            Properties properties = new Properties();
            String propFile = "src/main/java/config.properties";

            FileInputStream botConfig = new FileInputStream(propFile);

            System.out.println("Found config.properties.");
            properties.load(botConfig);
            botConfig.close();

            botToken = properties.getProperty(prop);

        } catch (Exception e) {
            System.out.println("Exception: " + e);
            System.out.println("This is most likely due to you not having a config.properties.");
            botToken = null;
        }

        return botToken;
    }

}
