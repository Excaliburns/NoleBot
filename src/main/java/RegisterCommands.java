import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.internal.requests.Route;

import java.util.List;
import java.util.Random;

public class RegisterCommands
{
    @SubscribeEvent
    public void SayHello(MessageReceivedEvent event)
    {
        Message msg = event.getMessage();
        MessageChannel messageChannel = msg.getChannel();
        Random rand = new Random();

        String messageContent = msg.getContentRaw().toLowerCase();

        int n = rand.nextInt(4);

        if(!msg.getAuthor().isBot())
            if( (messageContent.contains("hi") || messageContent.contains("hello")) && ( messageContent.contains("nolebot") || messageContent.contains("<@" + event.getJDA().getSelfUser().getId() + ">")))
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

    @SubscribeEvent
    public void getServerInfo(MessageReceivedEvent event)
    {
        Message msg = event.getMessage();
        MessageChannel messageChannel = event.getChannel();

        List<Role> RoleList = event.getGuild().getRoles();

        //Command is used to get relevant server information, such as ServerID, channelIDs, RoleIDs, etc.
        if(!msg.getAuthor().isBot())
            if(msg.getContentRaw().toLowerCase().startsWith("!serverinfo"))
            {
                messageChannel.sendMessage("Sending relevant server information...").queue();

                MessageBuilder serverRoleMessage = new MessageBuilder(), largeServerRoleMessage = new MessageBuilder(), serverInfoMessage = new MessageBuilder(), largeServerInfoMessage = new MessageBuilder();
                boolean isLargeMessage = false;

                for(int i = 0; i < RoleList.size() - 1; i++)
                {
                    String roleID = RoleList.get(i).getId();

                    if(serverInfoMessage.length() > 1700)
                    {
                        largeServerRoleMessage.appendFormat(RoleList.get(i).getAsMention() + " ID: " + roleID);
                        largeServerRoleMessage.append("\n");
                        isLargeMessage = true;
                    }
                    else
                    {
                        serverRoleMessage.appendFormat(RoleList.get(i).getAsMention() + " ID: " + roleID);
                        serverRoleMessage.append("\n");
                    }
                }

                messageChannel.sendMessage(serverRoleMessage.build()).queue();

                if(isLargeMessage)
                    messageChannel.sendMessage(largeServerRoleMessage.build()).queue();

                Guild sentGuild = msg.getGuild();
                messageChannel.sendMessage("Other relevant information:").queue();
                serverInfoMessage.appendFormat("Guild: " + sentGuild + " has ID: " + sentGuild.getId() + ", with Owner: " + "<@" + sentGuild.getOwnerId() + ">");
                serverInfoMessage.appendFormat("\nTotal Members: " + sentGuild.getMembers().size());
                serverInfoMessage.appendFormat("\nTotal Role count: " + (sentGuild.getRoles().size()-1));

                messageChannel.sendMessage(serverInfoMessage.build()).queue();
                messageChannel.sendMessage(msg.getMember().getRoles().toString()).queue();
            }
    }
}
