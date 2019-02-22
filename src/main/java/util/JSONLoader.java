package util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.internal.JDAImpl;

import java.io.*;

public class JSONLoader
{
    public static JsonObject getGuildJSON(Guild guild)
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

                    BufferedWriter writer = new BufferedWriter(new FileWriter(foundGuildParams));
                   writer.write("{}");

                    JsonObject jsonObject = (JsonObject) parser.parse( new FileReader("data/" + guild.getId() + ".json"));

                    jsonObject.addProperty("token", "!");
                    System.out.println(jsonObject);
                    writer.close();

                    guild.getRoles().forEach( role -> {
                        if(role.hasPermission(Permission.ADMINISTRATOR))
                        {
                            jsonObject.addProperty(role.getId(), "");
                        }
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
