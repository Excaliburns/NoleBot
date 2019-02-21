package commands.util;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import util.JSONLoader;
import util.PropLoader;
import util.Settings;

class CommandListener extends ListenerAdapter
{
    private String prefix = "!";

    public CommandListener()
    {
        Settings settings = Settings.getSettings();
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if(event.getAuthor().isBot())
            return;

        String[] commandEventMessage;

        String message = event.getMessage().getContentDisplay();
    }

    @Override
    public void onReady(ReadyEvent event)
    {
        System.out.println("Initializing Settings");

        JDA jda = event.getJDA();
        jda.getGuilds().forEach(guild -> {
            Settings settings = new Settings(guild);

            settings.setPrefix(new PropLoader().getProp("prefix"));
    });
    }
}
