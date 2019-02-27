package commands;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;

/**
 * This command gets all of the roles from a server and outputs their IDs. It also displays the Guild owner, and the total amount of members that the Guild has.
 *
 * Potentially useful for hardcoding RoleIDs into things, however in the future an outside way of registering permissions to users will be used.
 */
public class ServerInfoCommand extends Command
{
    public ServerInfoCommand()
    {
    }

    @Override
    public void onCommandReceived(CommandEvent event)
    {

    }
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Message msg = event.getMessage();
        MessageChannel messageChannel = event.getChannel();

        //Command is used to get relevant server information, such as ServerID, channelIDs, RoleIDs, etc.
        //Command only operates if user is not a bot.
        if(!msg.getAuthor().isBot()) {

            //Command is just serverinfo at the beginning of the string
            if (msg.getContentRaw().toLowerCase().matches("^.*serverinfo.*$") && msg.getMember().hasPermission(Permission.ADMINISTRATOR)) {

                messageChannel.sendMessage("Sending relevant server information...").queue();

                MessageBuilder serverRoleMessage = new MessageBuilder(), serverInfoMessage = new MessageBuilder();

                //Builds Message for displaying Server Role : RoleID.
                List<Role> roleList = new LinkedList<>(event.getGuild().getRoles());
                Iterator<Role> roleIterator = roleList.iterator();

                while (roleIterator.hasNext()) {
                    Role currentRole = roleIterator.next();

                    String roleID = currentRole.getId();

                    //Removes the @everyone tag / mention. Possibly change / do this in a different way this later?
                    if (currentRole.getName().equals("@everyone")) {
                        roleIterator.remove();
                        break;
                    }

                    //Adds the current role and it's ID to the MessageBuilder.
                    serverRoleMessage.appendFormat(currentRole.getAsMention() + " ID: " + roleID);
                    serverRoleMessage.append("\n");
                    roleIterator.remove();

                    //Messages can only be 2000 characters long, and each line takes up a maximum of 108 characters. This should suffice to keep the message within a reasonable boundary. You may change this if you want to make separate messages smaller, for some reason, I guess.
                    if (serverRoleMessage.length() > 1800) {
                        //This sends the current message and then clears the MessageBuilder.
                        messageChannel.sendMessage(serverRoleMessage.build()).queue();
                        serverRoleMessage.clear();
                    }
                }

                //Sends that shit.
                messageChannel.sendMessage(serverRoleMessage.build()).queue();


                //Other information about the Guild is built and queued here.
                Guild sentGuild = msg.getGuild();
                messageChannel.sendMessage("Other relevant information:").queue();
                serverInfoMessage.appendFormat("Guild: " + sentGuild.getName() + " has ID: " + sentGuild.getId() + ", with Owner: " + "<@" + sentGuild.getOwnerId() + ">");
                serverInfoMessage.appendFormat("\nTotal Members: " + sentGuild.getMembers().size());
                serverInfoMessage.appendFormat("\nTotal Role count: " + (sentGuild.getRoles().size() - 1));

                messageChannel.sendMessage(serverInfoMessage.build()).queue();
            }
        }
    }
}
