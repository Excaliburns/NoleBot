package commands.manager;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import util.RoleHelper;
import util.Settings;

import java.util.List;
import java.util.Optional;

public class DelRole extends Command {
    public DelRole() {
        name = "delrole";
        description = "Deletes a role that is lower than your own permission level to a user - if the Guild has allowed it to be.";
        helpDescription = "This command allows you to remove roles from others that are lower than your own permission level. These roles must be added to the permissions list by your guild admins.";
        requiredPermission = 500;
        usages.add("delrole <@User> <@Role>");
        usages.add("delrole <@Role> [@User, as many as you want] [@Role, as many as you want]");
    }

    @Override
    public void onCommandReceived(CommandEvent event) {
        String[] args = event.getMessage();
        MessageChannel messageChannel = event.getChannel();
        List<Role> sentRoles = event.getEvent().getMessage().getMentionedRoles();
        List<Member> sentMembers = event.getEvent().getMessage().getMentionedMembers();
        Settings settings = event.getSettings();
        int userPerm = event.getUserPermLevel();

        boolean doesGuildBanRoles = !(settings.getBannedRoles().isEmpty());
        boolean doesGuildHaveVerifiedRoles = !(settings.getVerifiedRoles().isEmpty());

        if (sentRoles.isEmpty()) {
            messageChannel.sendMessage("You did not send any roles.").queue();
        } else if (sentMembers.isEmpty()) {
            messageChannel.sendMessage("You did not mention any members.").queue();
        } else if (args[1] == null) {
            messageChannel.sendMessage("You did not enter any arguments! Please use !help addrole for more information").queue();
        } else {
            List<String> bannedRoles = null;
            List<String> verifiedRoles = null;
            if (doesGuildBanRoles) bannedRoles = settings.getBannedRoles();
            if (doesGuildHaveVerifiedRoles) verifiedRoles = settings.getVerifiedRoles();

            for (Role r : sentRoles) {
                Optional<RoleHelper> roleHelpers = settings.getRoleHelper().stream().filter(h -> r.getId().matches(h.getRoleID())).findFirst();
                if (doesGuildBanRoles) if (bannedRoles.indexOf(r.getId()) != -1) {
                    messageChannel.sendMessage("You cannot remove role **" + r.getName() + "**, as designated by the Guild admins. Please contact them if you think this is incorrect.").queue();
                    continue;
                }
                if(doesGuildHaveVerifiedRoles) if(verifiedRoles.indexOf(r.getId()) != -1){
                    messageChannel.sendMessage("You cannot remove role **" + r.getName() + "**, as designated by the Guild admins. Please contact them if you think this is incorrect.").queue();
                    continue;
                }
                if (!roleHelpers.isPresent()) {
                    messageChannel.sendMessage("Your Guild has not set role: **" + r.getName() + "** as a role that can be removed. Please contact an administrator if you think this is incorrect.").queue();
                } else {
                    RoleHelper roleHelper = roleHelpers.get();
                    if (userPerm < roleHelper.getPermID()) {
                        messageChannel.sendMessage("You cannot remove role: **" + r.getName() + "** as it has a higher permission level than yours.").queue();
                        continue;
                    }
                    for (Member m : sentMembers) {
                            if (m.getRoles().contains(r)) {
                                messageChannel.sendMessage("Removed Role: **" + r.getName() + " from User: **" + m.getEffectiveName() + "**").queue();
                                event.getGuild().removeRoleFromMember(m, r).queue();
                            } else {
                                messageChannel.sendMessage("User **" + m.getEffectiveName() + "** did not have role: **" + r.getName() + "**.").queue();
                            }

                    }
                }
            }
        }
    }
}
