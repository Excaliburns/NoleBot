package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

public class PropLoader {
    public static String getProp(String prop) {
        String propValue;
        try {
            Properties properties = new Properties();
            Properties sortedProps = new Properties() {
                @Override
                public synchronized Enumeration<Object> keys() {
                    return Collections.enumeration(new TreeSet<>(super.keySet()));
                }
            };

            File propFile = new File("/home/ec2-user/NoleBot/build/libs/data/config/config.properties");

            if (!propFile.exists()) {
                System.out.println("Did not find config.properties. Making now.");

                if (!propFile.getParentFile().mkdirs())
                    System.out.println("Could not create " + propFile + " is something wrong?");
                if (!propFile.createNewFile())
                    System.out.println("Could not create " + propFile + " is something wrong?");

                OutputStream fStream = new FileOutputStream(propFile);

                //Create properties normally used for Bot operation
                properties.setProperty("token", "");
                properties.setProperty("google_app_name", "NoleBot Verification Service");
                properties.setProperty("google_spreadsheet_id", "");

                properties.setProperty("db_addr", "127.0.0.1");
                properties.setProperty("db_name", "nolebot");
                properties.setProperty("db_user", "nolebot");
                properties.setProperty("db_pass", "");

                sortedProps.putAll(properties);

                sortedProps.store(fStream, null);
                System.out.println("Created and stored blank values. Please open /data/config/config.properties and set the bot's values.");

                fStream.close();
            }
            FileInputStream botConfig = new FileInputStream(propFile);

            properties.load(botConfig);
            botConfig.close();

            propValue = properties.getProperty(prop);

        } catch (Exception e) {
            System.out.println("Exception: " + e);
            propValue = null;
        }

        return propValue;
    }
}
