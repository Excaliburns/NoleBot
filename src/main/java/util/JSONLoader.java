package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import commands.inhouse.InhouseStruct;

import java.io.*;

public class JSONLoader {
    public static boolean doesSettingExist(String guildID) {
        File guildFile = new File("data/" + guildID + "/"  + "main.json");

        if (guildFile.exists())
            System.out.println("Found " + guildID + " json");

        return (guildFile.exists());
    }

    @SuppressWarnings("all")
    public static void createGuildJSON(String guildID) {
        File foundGuildParams = new File("data/" + guildID + "/" + "main.json");
        try {
            System.out.println("Creating " + guildID + " json");

            foundGuildParams.getParentFile().mkdirs();
            foundGuildParams.createNewFile();


            Settings settings = new Settings(guildID);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(foundGuildParams))) {
                Gson gson = new GsonBuilder()
                        .serializeNulls()
                        .setPrettyPrinting()
                        .create();
                writer.write(gson.toJson(settings, settings.getClass()));

                writer.flush();
            }

        } catch (IOException e) {
            System.out.println("Exception in creation of: " + guildID + " json " + e);
        }

    }

    public static Settings getGuildSettings(String guildID) {
        File foundGuildParams = new File("data/" + guildID + "/" + "main.json");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(foundGuildParams));

            Gson gson = new Gson();
            return gson.fromJson(reader, Settings.class);
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found Exception: " + e);
            return null;
        }
    }

    public static void saveGuildSettings(Settings settings) {
        File guildSettings = new File("data/" + settings.getGuildID()  + "/" + "main.json");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(guildSettings))) {
            Gson gson = new GsonBuilder()
                    .serializeNulls()
                    .setPrettyPrinting()
                    .create();
            writer.write(gson.toJson(settings, settings.getClass()));

            writer.flush();
        } catch (IOException e) {
            System.out.println("Exception saving guild settings: " + e);
        }
    }

    public static InhouseStruct inhouseLoader(Settings settings) {
        File JSONFile = new File("data/" + settings.getGuildID() + "/" + "inhouse.json");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(JSONFile));

            Gson gson = new Gson();
            return gson.fromJson(reader, InhouseStruct.class);
        }
        catch (IOException e)
        {
            try
            {
                System.out.println("Did not find inhouse json for guild " + settings.getGuildID() +"; creating now." );
                JSONFile.getParentFile().mkdirs();
                JSONFile.createNewFile();

                InhouseStruct inhouseStruct = new InhouseStruct();

                BufferedWriter writer = new BufferedWriter(new FileWriter(JSONFile));
                Gson gson = new GsonBuilder()
                        .serializeNulls()
                        .setPrettyPrinting()
                        .create();
                writer.write(gson.toJson(inhouseStruct, InhouseStruct.class));
                writer.flush();

                return inhouseStruct;
            }
            catch (IOException x)
            {
                System.out.println("Something is terribly wrong. Could not create inhouse file for guild " + settings.getGuildID() + "\n :" + x);
                return null;
            }

        }
    }
}
