package util;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.entities.Guild;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class JSONLoader
{

    public Settings setGuildSettings(Guild guild)
    {
        Settings settings = new Settings(guild);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("GuildID", guild);

        return settings;
    }

    public Settings getGuildSettings(Guild guild)
    {
        Settings settings = null;
        JsonParser parser = new JsonParser();

        /*
        try
        {
            Object obj = parser.parse(new FileReader("data/") + guild.getId())
        }catch (FileNotFoundException e)
        {

        }*/

        return settings;
    }
}
