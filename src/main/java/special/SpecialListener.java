package special;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import util.TwitterLoader;

import java.util.List;
import java.util.Random;

public class SpecialListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        MessageChannel messageChannel = msg.getChannel();
        Random rand = new Random();

        String messageContent = msg.getContentRaw().toLowerCase();

        int n = rand.nextInt(4);

        if (!msg.getAuthor().isBot())
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

        if (msg.getChannel().getId().equals("581894109594386477")) {
            List<Message.Attachment> attachmentList = msg.getAttachments();
            String tweet = msg.getContentRaw();

            try {
                Twitter twitter = TwitterLoader.getTwitter();

                Status status = twitter.updateStatus(tweet);
            } catch (TwitterException e) {
                System.out.println("Exception in getting data from twitter: " + e);
            }

        }
    }
}
