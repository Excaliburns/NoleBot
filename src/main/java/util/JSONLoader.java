package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import commands.inhouse.GroupStruct;

import java.io.*;

public class JSONLoader {
    public static boolean doesSettingExist(String guildID) {
        File guildFile = new File("data/" + guildID + "/" + "main.json");

        if (guildFile.exists()) System.out.println("Found " + guildID + " json");

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


            genericSave(foundGuildParams, settings);

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
        File guildSettings = new File("data/" + settings.getGuildID() + "/" + "main.json");


        genericSave(guildSettings, settings);
    }

    public static GroupStruct inhouseLoader(Settings settings) {
        File JSONFile = new File("data/" + settings.getGuildID() + "/" + "inhouse.json");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(JSONFile));

            Gson gson = new Gson();
            return gson.fromJson(reader, GroupStruct.class);
        } catch (IOException e) {
            try {
                System.out.println("Did not find inhouse json for guild " + settings.getGuildID() + "; creating now.");

                if( !(JSONFile.getParentFile().mkdirs() & JSONFile.createNewFile()) )
                    System.out.println("Something is wrong. Could not make directory or create new file " + settings.getGuildID() + " inhouse json.");

                GroupStruct groupStruct = new GroupStruct();

                genericSave(JSONFile, groupStruct);

                return groupStruct;
            } catch (IOException x) {
                System.out.println("Something is terribly wrong. Could not create inhouse file for guild " + settings.getGuildID() + "\n :" + x);
                return null;
            }

        }
    }

    public static void saveInhouseData(GroupStruct groupStruct, String GuildID) {
        File JSONFile = new File("data/" + GuildID + "/" + "inhouse.json");

        genericSave(JSONFile, groupStruct);
    }

    private static void genericSave(File fileName, Object gsonClass) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
            writer.write(gson.toJson(gsonClass, gsonClass.getClass()));

            writer.flush();
        } catch (IOException e) {
            System.out.println("Exception saving guild settings: " + e);
        }
    }
}
