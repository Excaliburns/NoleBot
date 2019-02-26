package util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.dv8tion.jda.api.entities.Guild;

import java.io.*;

public class JSONLoader
{
    public static void createGuildJSON(Guild guild)
    {
        JsonParser parser = new JsonParser();

            File foundGuildParams = new File("data/" + guild.getId() + ".json");
            if(!foundGuildParams.exists())
            {
                try
                {
                    foundGuildParams.getParentFile().mkdirs();
                    foundGuildParams.createNewFile();

                    BufferedWriter writer = new BufferedWriter(new FileWriter(foundGuildParams));
/*
                    JsonObject jsonObject = (JsonObject) parser.parse( new FileReader("data/" + guild.getId() + ".json"));

                    jsonObject.addProperty("token", "!");
                    System.out.println(jsonObject);
                    */
                    writer.close();
/*
                    guild.getRoles().forEach( role -> {
                        if(role.hasPermission(Permission.ADMINISTRATOR))
                        {
                            jsonObject.addProperty(role.getId(), "");
                            try
                            {
                                writer.write(jsonObject.toString());
                            }
                            catch (IOException e)
                            {
                                System.out.println("Exception writing JSON Object to file: " + e);
                            }
                        }
                    });
                    */
                }catch (IOException e)
                {
                    System.out.println("Exception: " + e);
                }

            }
    }
}
