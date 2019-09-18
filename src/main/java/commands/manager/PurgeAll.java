package commands.manager;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import util.RoleHelper;
import util.Settings;

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
        String prefix = event.getPrefix();
        String[] args = event.getMessage();
        Message message = event.getEvent().getMessage();
        MessageChannel messageChannel = event.getChannel();
        Settings settings = event.getSettings();
        List<Role> roleList = message.getMentionedRoles();
        List<RoleHelper> roleHelper = settings.getRoleHelper();
        List<String> verifiedRoles = settings.getVerifiedRoles();
        List<String> bannedRoles = settings.getBannedRoles();
        int userPerm = event.getUserPermLevel();

        if (args[1] == null) {
            messageChannel.sendMessage("Incorrect arguments! Please use " + prefix + "help purgeall.").queue();
        } else if (roleList.isEmpty()) {
            messageChannel.sendMessage("You did not mention any roles!").queue();
        } else {
            for (Role r : roleList) {
                String roleID = r.getId();
                if (verifiedRoles.indexOf(roleID) != -1 || bannedRoles.indexOf(roleID) != -1)
                    continue;

                Optional<RoleHelper> optionalRoleHelper = roleHelper.stream().filter(c -> c.getRoleID().equals(roleID)).findAny();

                if (optionalRoleHelper.isPresent()) {
                    if (userPerm > optionalRoleHelper.get().getPermID()) {
                        List<Member> memberList = event.getGuild().getMembersWithRoles(r);
                        for (Member m : memberList) {
                            event.getGuild().removeRoleFromMember(m, r).queue();
                            messageChannel.sendMessage("Removed user: **" + m.getEffectiveName() + "** from role: **" + r.getName() + "**.").queue();
                        }
                    } else {
                        messageChannel.sendMessage("Did not remove **" + r.getName() + "**, as it has a higher permission level than you.").queue();
                    }
                } else {
                    messageChannel.sendMessage("Role: **" + r.getName() + "** is not in the permission list!").queue();
                }
            }
        }
    }
}