package Commands;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.LinkedList;
import java.util.List;

import static Commands.UserHelper.*;

public class AddRole extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        MessageChannel sentChannel = event.getChannel();
        User author = msg.getAuthor();
        Member writer = msg.getMember();
        boolean verified = isVerified(writer);
        boolean namecheck = nameMatch(writer.getEffectiveName());

        List<Role> memberRoleList = new LinkedList<>(writer.getRoles());

        if (!author.isBot()) {
            List<Role> roleList = new LinkedList<>(msg.getMentionedRoles());
            if (msg.getContentRaw().toLowerCase().matches("^.*addrole.*$")) {
                if (verified) {
                    if (namecheck) {
                        for (Role r : roleList) {
                            if (r.getName ().contains ("Player")) {
                                if (memberRoleList.contains (r)) {
                                    sentChannel.sendMessage ("User: " + writer.getAsMention () + " already has that role.").queue ();
                                } else {
                                    msg.getGuild ().getController ().addSingleRoleToMember (writer, r).queue ();
                                    sentChannel.sendMessage ("You have assigned yourself: " + r.getName ()).queue ();
                                }

                            } else {
                                sentChannel.sendMessage ("You cannot add this role.").queue ();
                            }
                        }

                        }
                    } else {
                        sentChannel.sendMessage("User: " + writer.getAsMention() +
                                " does not have a properly formatted name. " +
                                "Please tell them to format their name like this: \"Firstname | Gamertag\" ")
                                .queue();
                    }
                } else {
                    sentChannel.sendMessage("User: " +
                            writer.getAsMention() +
                            " does not have the FSU Student tag. Please contact an officer.")
                            .queue();
                }
            }
        }
    }