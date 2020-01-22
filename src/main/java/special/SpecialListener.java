package special;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import util.Settings;
import util.TwitterLoader;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class SpecialListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;

        Message msg = event.getMessage();
        MessageChannel messageChannel = msg.getChannel();
        Random rand = new Random();

        String messageContent = msg.getContentRaw().toLowerCase();

        int n = rand.nextInt(4);

            if ((messageContent.contains("nolebot") && (messageContent.contains("hi") || messageContent.contains("hello") || messageContent.contains("hey") || messageContent.contains("howdy")) || messageContent.contains("<@" + event.getJDA().getSelfUser().getId() + ">"))) {
                switch (n) {
                    case 0:
                        messageChannel.sendMessage("Greetings!").queue();
                        break;
                    case 1:
                        messageChannel.sendMessage("Hey there!").queue();
                        break;
                    case 2:
                        messageChannel.sendMessage("Hello to you too :)").queue();
                        break;
                    case 3:
                        messageChannel.sendMessage("A good day to you " + "<@" + msg.getAuthor().getId() + ">" + "!").queue();
                        break;
                }
            }

        if (msg.getChannel().getId().equals("519243544008065044")) {
            List<Message.Attachment> attachmentList = msg.getAttachments();
            String tweet = msg.getContentRaw();
            RestAction<PrivateChannel> senderChannel = msg.getAuthor().openPrivateChannel();
//            Settings guildSettings = Settings.getSettings(msg.getJDA().getGuildById("519243544008065044"));

            // After getting message to send, check for validation. Send message to user about problems, ex. if tweet cannot be posted.
            /*
            Param 1. Message must not be longer than 280 characters. - result: Tweet cannot be posted
            Param 2. Message cannot contain profanity. Check message against list of profanity. - result: Tweet cannot be posted.
            Param 3. Must hashtag at least once - result: Tweet will not be posted until fixed. User will be given 10 minutes to fix the post, then respond to bot. If it is not fixed, bot will delete post.
            Param 4. Max two tweets every 12 hours. - result: Tweet will not be posted, and bot will DM user about the limit. Either wait, or message admin.
            Param 5. Max 2 images attached. Any more, and the first 2 will be posted. - result: message user about this. (You can only attach up to 2 images.)
             */

            if(tweet.length() > 280)
            {
                //senderChannel.queue(privateChannel -> privateChannel.sendMessage("Your tweet could not be sent as it was over 280 characters long. Edit the post within 10 minutes and respond with \"fixed\" or your tweet will not be posted. ").queue());
                //return;
            }

            try {
                Twitter twitter = TwitterLoader.getTwitter();

                //StatusUpdate status = new StatusUpdate(tweet);
            } catch (TwitterException e) {
                System.out.println("Exception in getting data from twitter: " + e);
                messageChannel.sendMessage("There was a problem in getting a twitter instance. Please contact a guild admin.").queue();
            }

        }
    }
}
