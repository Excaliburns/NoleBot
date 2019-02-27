package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.dv8tion.jda.api.entities.Guild;
import org.json.JSONObject;

import java.io.*;

public class JSONLoader
{
    public static boolean doesSettingExist(String guildID)
    {
        File guildFile = new File("data/" + guildID + ".json");

        if(guildFile.exists())
            System.out.println("Found " + guildID + ".json");

        return (guildFile.exists());
    }
    public static void createGuildJSON(String guildID)
    {
            File foundGuildParams = new File("data/" + guildID + ".json");
                try
                {
                    System.out.println("Creating " + guildID + ".json");
                    foundGuildParams.getParentFile().mkdirs();
                    foundGuildParams.createNewFile();

                    Settings settings = new Settings(guildID);

                    try(BufferedWriter writer = new BufferedWriter(new FileWriter(foundGuildParams)))
                    {
                        Gson gson = new GsonBuilder()
                                .serializeNulls()
                                .setPrettyPrinting()
                                .create();
                        writer.write(gson.toJson(settings, settings.getClass()));

                        writer.flush();
                    }

                }catch (IOException e)
                {
                    System.out.println("Exception in creation of: " + guildID + ".json " + e);
                }

            }
    public static Settings getGuildSettings(String guildID)
    {
            File foundGuildParams = new File("data/" + guildID + ".json");

            try
            {
                BufferedReader reader = new BufferedReader(new FileReader(foundGuildParams));

                Gson gson = new Gson();
                return gson.fromJson(reader, Settings.class);
            }
            catch(FileNotFoundException e)
            {
                System.out.println("File Not Found Exception: " + e);
                return null;
            }
    }
    public static void saveGuildSettings(Settings settings)
    {
        File guildSettings = new File("data/" + settings.getGuildID() + ".json");

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(guildSettings)))
        {
            Gson gson = new GsonBuilder()
                    .serializeNulls()
                    .setPrettyPrinting()
                    .create();
            writer.write(gson.toJson(settings, settings.getClass()));

            writer.flush();
        }
        catch(IOException e)
        {
            System.out.println("Exception saving guild settings: " + e);
        }
    }
}
