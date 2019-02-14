import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

import java.util.Random;

public class RegisterCommands
{
    @SubscribeEvent
    public void GameRank(MessageReceivedEvent event)
    {
        Message msg = event.getMessage();
        MessageChannel messageChannel = event.getChannel();

        if(!msg.getAuthor().isBot())
            if(msg.getContentRaw().startsWith("!rank"))
            {
            System.out.println(msg.getMember().getRoles());
            messageChannel.sendMessage(msg.getMember().getRoles().toString()).queue();
            }
    }

    @SubscribeEvent
    public void SayHello(MessageReceivedEvent event)
    {
        Message msg = event.getMessage();
        MessageChannel messageChannel = msg.getChannel();
        Random rand = new Random();

        String messageContent = msg.getContentRaw().toLowerCase();

        int n = rand.nextInt(4);

        if(!msg.getAuthor().isBot())
            if(messageContent.contains("hello") && ( messageContent.contains("nolebot") || messageContent.contains("<@" + event.getJDA().getSelfUser().getId() + ">")))
            {
                switch(n)
                {
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
}
