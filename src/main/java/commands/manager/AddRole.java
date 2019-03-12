package commands.manager;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.LinkedList;
import java.util.List;

import static util.UserHelper.isVerified;
import static util.UserHelper.nameMatch;

public class AddRole extends Command {

    public AddRole()
    {
        name = "addrole";
        description = "Adds a role that is lower than your own permission level to a user - if the Guild has allowed it to be.";
        helpDescription = "This command allows you to assign roles to others that are lower than your own permission level. These roles must be added to the permissions list by your guild admins. Use !listperm to see a list of these commands.";
        requiredPermission = 500;
        usages.add("addrole <@User> <@Role>");
        usages.add("addrole <@Role> [@User, as many as you want] [@Role, as many as you want]");
    }

    @Override
    public void onCommandReceived(CommandEvent event) {

    }

    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        MessageChannel sentChannel = event.getChannel();
        User author = msg.getAuthor();
        Member writer = msg.getMember();
        boolean verified = isVerified(writer);
        boolean nameCheck = nameMatch(writer.getEffectiveName());

        List<Role> memberRoleList = new LinkedList<>(writer.getRoles());

        if (!author.isBot()) {
            List<Role> roleList = new LinkedList<>(msg.getMentionedRoles());
            if (msg.getContentRaw().toLowerCase().matches("^.*addrole.*$")) {
                if (verified) {
                    if (nameCheck) {
                        for (Role r : roleList) {
                            if (r.getName().contains("Player")) {
                                if (memberRoleList.contains(r)) {
                                    sentChannel.sendMessage("User: " + writer.getAsMention() + " already has that role.").queue();
                                } else {
                                    msg.getGuild().getController().addSingleRoleToMember(writer, r).queue();
                                }
                                sentChannel.sendMessage("You cannot assign " + r + ".").queue();
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
}