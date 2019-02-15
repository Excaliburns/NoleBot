import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.hooks.EventListener;

import javax.security.auth.login.LoginException;
import java.io.FileInputStream;
import java.util.Properties;

public class NoleBot implements EventListener
{
    public static void main(String[] args) throws LoginException
    {
        JDA jda = new JDABuilder(new NoleBot().getBotToken())
                .setEventManager(new AnnotatedEventManager())
                .addEventListeners(new NoleBot(), new RegisterCommands())
                .build();
    }

    public void onEvent(GenericEvent event)
    {
        if(event instanceof ReadyEvent)
            System.out.println("API is ready!");
    }

    private String getBotToken()
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

            botToken = properties.getProperty("token");

        } catch (Exception e) {
            System.out.println("Exception: " + e);
            botToken = null;
        }

        return botToken;
    }

}
