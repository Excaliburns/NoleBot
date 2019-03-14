package commands.manager;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import util.RoleHelper;
import util.Settings;
import util.UserHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class PurgeAll extends Command {
    public PurgeAll() {
        name = "purgeall";
        description = "Purges all members of a role that is below your permission level.";
        helpDescription = "Purges all members of a role that is below your permission level. This will not delete members from a role that is not in the permission list, is a banned role, or is a verified role.";
        usages.add("purgeall <@Role> [@Role, as many as you want]");
        requiredPermission = 500;
    }

    @Override
    public void onCommandReceived(CommandEvent event) {
        String[] args = event.getMessage();
        Message message = event.getEvent().getMessage();
        MessageChannel messageChannel = event.getChannel();
        Settings settings = event.getSettings();
        List<Role> roleList = message.getMentionedRoles();
        List<RoleHelper> roleHelper = settings.getRoleHelper();
        List<String> verifiedRoles = settings.getVerifiedRoles();
        List<String> bannedRoles = settings.getBannedRoles();
        int userPerm = UserHelper.getHighestUserPermission(message.getMember().getRoles(), roleHelper);

        if (args[1] == null) {
            messageChannel.sendMessage("Incorrect arguments! Please use !help purgeall.").queue();
        } else if (roleList.isEmpty()) {
            messageChannel.sendMessage("You did not mention any roles!").queue();
        } else {
            for (Role r : roleList) {
                String roleID = r.getId();
                if (verifiedRoles.indexOf(roleID) != -1 || bannedRoles.indexOf(roleID) != -1)
                    messageChannel.sendMessage("You cannot assign role: **" + r.getName() + "** to a user.").queue();

                Optional<RoleHelper> optionalRoleHelper = roleHelper.stream().filter(c -> c.getRoleID().equals(roleID)).findAny();

                if (optionalRoleHelper.isPresent()) {
                    if (userPerm > optionalRoleHelper.get().getPermID()) {
                        List<Member> memberList = event.getGuild().getMembersWithRoles(r);
                        for(Member m: memberList)
                            event.getGuild().getController().removeSingleRoleFromMember(m, r).queue();
                    } else {
                        messageChannel.sendMessage("You cannot assign **" + r.getName() + "**, as it has a higher permission level than you.").queue();
                    }
                } else {
                    messageChannel.sendMessage("Role: **" + r.getName() + "** is not in the permission list!").queue();
                }
            }
        }
    }
}