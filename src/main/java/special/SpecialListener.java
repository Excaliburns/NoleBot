package special;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import util.CacheMessage;

import javax.annotation.Nonnull;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class SpecialListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        MessageChannel messageChannel = msg.getChannel();
        Random rand = new Random();
        String messageContent = msg.getContentRaw().toLowerCase();
        OffsetDateTime offsetDateTimeMessage = msg.getTimeCreated();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

        if(event.getAuthor().isBot()) return;

        MessageCacheAccess.insertMessage(event.getMessageIdLong(), messageContent, event.getAuthor().getAsTag(), format.format(offsetDateTimeMessage.toLocalDateTime()));

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
    }


    @Override
    public void onMessageDelete(@Nonnull MessageDeleteEvent event) {
        CacheMessage message = MessageCacheAccess.getDeletedMessage(event.getMessageIdLong());

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.append("MESSAGE WAS DELETED:\n");
        messageBuilder.append("**MESSAGE CONTENT:** " + message.getMessageContent() + "\n");
        messageBuilder.append("**ORIGINAL MESSAGE SENDER:** " + message.getMessageSender() + "\n");
        messageBuilder.append("**ORIGINAL MESSAGE SENT DATE:** " + message.getDateSent() + "\n");
        messageBuilder.append("**MESSAGE DELETED IN:** #" + event.getChannel().getName());

        event.getJDA().getTextChannelById(735679360589365349L).sendMessage(messageBuilder.build()).queue();
    }

    @Override
    public void onMessageUpdate(@Nonnull MessageUpdateEvent event) {
        CacheMessage message = MessageCacheAccess.getDeletedMessage(event.getMessageIdLong());

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.append("MESSAGE WAS EDITED:\n");
        messageBuilder.append("**MESSAGE CONTENT:** " + message.getMessageContent() + "\n");
        messageBuilder.append("**ORIGINAL MESSAGE SENDER:** " + message.getMessageSender() + "\n");
        messageBuilder.append("**ORIGINAL MESSAGE SENT DATE:** " + message.getDateSent() + "\n");
        messageBuilder.append("**EDITED TO:** " + event.getMessage().getContentRaw() + "\n");
        messageBuilder.append("**MESSAGE EDITED IN:** #" + event.getChannel().getName());

        event.getJDA().getTextChannelById(735679360589365349L).sendMessage(messageBuilder.build()).queue();
    }
}
