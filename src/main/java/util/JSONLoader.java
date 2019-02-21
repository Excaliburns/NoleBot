package util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

public class JSONLoader extends ListenerAdapter
{
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if(event.getMessage().getContentRaw().startsWith("!json")) {
            getGuildJSON(event.getGuild());
        }
    }

    private JsonObject getGuildJSON(Guild guild)
    {
        JsonParser parser = new JsonParser();
        try
        {
            File foundGuildParams = new File("data/" + guild.getId() + ".json");
            if(!foundGuildParams.exists())
            {
                try
                {
                    foundGuildParams.getParentFile().mkdirs();
                    foundGuildParams.createNewFile();
                    System.out.println(foundGuildParams.getAbsolutePath());
                }catch (IOException e)
                {
                    System.out.println("Exception: " + e);
                }

            }
            System.out.println("here");
            Object obj = parser.parse(new FileReader("data/" + guild.getId() + ".json"));
            System.out.println("here2");
            return (JsonObject) obj;

        }catch (FileNotFoundException e)
        {
            System.out.println("Exception :" + e);
            return null;
        }
    }
}
