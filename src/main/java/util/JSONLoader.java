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

                    //JsonObject jsonObject = new JsonParser().parse(new FileReader("data/" + guild.getId() + ".json"));

                    guild.getRoles().forEach( role -> {

                    });
                }catch (IOException e)
                {
                    System.out.println("Exception: " + e);
                }

            }
            Object obj = parser.parse(new FileReader("data/" + guild.getId() + ".json"));
            return (JsonObject) obj;

        }catch (FileNotFoundException e)
        {
            System.out.println("Exception :" + e);
            return null;
        }
    }
}
