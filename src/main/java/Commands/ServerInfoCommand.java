package Commands;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class ServerInfoCommand extends ListenerAdapter
{
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
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

                    if(serverRoleMessage.length() > 1700)
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
                serverInfoMessage.appendFormat("Guild: " + sentGuild.getName() + " has ID: " + sentGuild.getId() + ", with Owner: " + "<@" + sentGuild.getOwnerId() + ">");
                serverInfoMessage.appendFormat("\nTotal Members: " + sentGuild.getMembers().size());
                serverInfoMessage.appendFormat("\nTotal Role count: " + (sentGuild.getRoles().size()-1));

                messageChannel.sendMessage(serverInfoMessage.build()).queue();
                messageChannel.sendMessage(msg.getMember().getRoles().toString()).queue();
            }
    }
}
