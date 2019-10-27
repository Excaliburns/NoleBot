package util;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class TwitterLoader {

    public static Twitter getTwitter() throws TwitterException {
        Properties properties = new Properties();
        File propFile = new File("data/config/twitter.properties");

        try {
            FileInputStream botConfig = new FileInputStream(propFile);
            properties.load(botConfig);
        } catch (Exception e) {
            System.out.println(e);
        }

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true).setOAuthAccessToken(properties.getProperty("oauth.accessToken")).setOAuthAccessTokenSecret(properties.getProperty("oauth.accessTokenSecret")).setOAuthConsumerKey(properties.getProperty("auth.consumerKey")).
                setOAuthConsumerSecret(properties.getProperty("oauth.consumerSecret"));


        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf.getInstance();
    }
}
