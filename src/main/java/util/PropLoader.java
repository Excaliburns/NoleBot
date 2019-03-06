package util;

import java.io.*;
import java.util.Properties;

public class PropLoader {
    public String getProp(String prop) {

        String botToken;
        try {
            Properties properties = new Properties();
            File propFile = new File("data/config/config.properties");

            if (!propFile.exists()) {
                System.out.println("Did not find config.properties. Making now.");

                if (!propFile.getParentFile().mkdirs())
                    System.out.println("Could not create " + propFile + " is something wrong?");
                if (!propFile.createNewFile())
                    System.out.println("Could not create " + propFile + " is something wrong?");

                OutputStream fStream = new FileOutputStream(propFile);

                //Create properties normally used for Bot operation
                properties.setProperty("token", "");

                properties.store(fStream, null);
            }
            FileInputStream botConfig = new FileInputStream(propFile);

            System.out.println("Found config.properties.");
            properties.load(botConfig);
            botConfig.close();

            botToken = properties.getProperty(prop);

        } catch (Exception e) {
            System.out.println("Exception: " + e);
            botToken = null;
        }

        return botToken;
    }
}
