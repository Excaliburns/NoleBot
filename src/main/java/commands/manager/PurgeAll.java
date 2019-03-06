package commands.manager;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.LinkedList;
import java.util.List;

public class PurgeAll extends ListenerAdapter {
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        MessageChannel sentChannel = event.getChannel();
        User author = msg.getAuthor();
        Member writer = msg.getMember();

        if (!author.isBot()) {
            if (msg.getContentRaw().toLowerCase().matches("^.*purgeall.*$")) {
                if (writer.hasPermission(Permission.ADMINISTRATOR)) {
                    List<Role> roleList = new LinkedList<>(msg.getMentionedRoles());
                    for (Role r : roleList) {
                        List<Member> memberList = new LinkedList<>(msg.getGuild().getMembersWithRoles(r));
                        for (Member m : memberList) {
                            msg.getGuild().getController().removeSingleRoleFromMember(m, r).queue();
                        }
                        sentChannel.sendMessage(r.getName() + " has been purged of all members");
                    }
                } else {
                    sentChannel.sendMessage("You do not have adequate permissions for this command.").queue();
                }
            }
        }
    }
}