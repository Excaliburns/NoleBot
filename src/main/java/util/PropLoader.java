package util;

import jdk.internal.util.xml.impl.Input;

import java.io.FileInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Properties;

public class PropLoader
{
    public String getProp(String prop)
    {

        String botToken;
        try {
            Properties properties = new Properties();
            File propFile = new File("data/config/config.properties");

            if (!propFile.exists()) {
                System.out.println("Did not find config.properties. Making now.");

                propFile.getParentFile().mkdirs();
                propFile.createNewFile();

                InputStream fStream = new FileInputStream(propFile);
                properties.load(fStream);

                //Create properties normally used for Bot operation
                properties.setProperty("token", "");
                properties.setProperty("prefix", "!");
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
