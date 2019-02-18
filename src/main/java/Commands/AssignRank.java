package Commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/*
Temporary command to allow Game Managers on FSU server to assign ranks while a better MySQL DB solution is implemented.
This is a very one-server solution to the problem. THIS COMMAND WILL BE MUCH DIFFERENT IN THE FUTURE.
Simple functionality for the FSU Esports, checks include looking for a "Game Manager" rank, as well as a properly formatted name.
 */
public class AssignRank extends ListenerAdapter
{
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        MessageChannel sentChannel = event.getChannel();

        boolean isGameManager = isGameManager(msg.getMember());
        if(!msg.getAuthor().isBot()) {

            List<Member> mentioned = msg.getMentionedMembers();
            List<Role> roleList = new LinkedList<>(msg.getMentionedRoles());

            if(msg.getContentRaw().toLowerCase().startsWith("!assignrole"))
            {

                if (isGameManager)
                {
                        for (Member s : mentioned) {

                            if (s.getEffectiveName().contains("|"))
                            {
                                if(s.getRoles().toString().contains("Student")) {


                                    for (Role r : roleList) {
                                        if (r.getName().contains("FSU") && !(r.getName().contains("Alum") || r.getName().contains("Student") || r.getName().contains("Faculty"))) {
                                            if (s.getRoles().toString().contains(r.toString())) {
                                                sentChannel.sendMessage("User: " + s.getAsMention() + " already has that role.").queue();
                                            } else {
                                                msg.getGuild().getController().addSingleRoleToMember(s, r).queue();
                                                sentChannel.sendMessage("User: " + s.getAsMention() + " was assigned: " + "**" + r.getName() + "**").queue();
                                            }
                                        } else {
                                            sentChannel.sendMessage("You cannot assign those roles.").queue();
                                        }
                                    }
                                } else {
                                    sentChannel.sendMessage("User: " + s.getAsMention() + " does not have the FSU Student tag. Please contact an officer.").queue();
                                }

                            } else {
                                sentChannel.sendMessage("User: " + s.getAsMention() + " does not have a properly formatted name. Please tell them to format their name like this: \"Firstname | Gamertag\" ").queue();
                            }

                        }
                }
                else
                {
                    sentChannel.sendMessage("Sorry, you do not have sufficient permissions to assign this role.").queue();
                }

            }
        }
    }

    private boolean isGameManager (Member authorOfMessage)
    {
        List<Role> roleList = new LinkedList<>(authorOfMessage.getRoles());
        Iterator<Role> roleIterator = roleList.iterator();

        if(authorOfMessage.hasPermission(Permission.ADMINISTRATOR))
            return true;

        while(roleIterator.hasNext())
        {
            if(roleIterator.next().getName().equals("Game Manager"))
            {
                return true;
            }
            roleIterator.remove();
        }

        return false;
    }

}
