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

public class AddRole extends Command {

    public AddRole() {
        name = "addrole";
        description = "Adds a role that is lower than your own permission level to a user - if the Guild has allowed it to be.";
        helpDescription = "This command allows you to assign roles to others that are lower than your own permission level. These roles must be added to the permissions list by your guild admins.";
        requiredPermission = 500;
        usages.add("addrole <@User> <@Role>");
        usages.add("addrole <@Role> [@User, as many as you want] [@Role, as many as you want]");
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
            if (doesGuildBanRoles) bannedRoles = settings.getBannedRoles();

            for (Role r : sentRoles) {
                Optional<RoleHelper> roleHelpers = settings.getRoleHelper().stream().filter(h -> r.getId().matches(h.getRoleID())).findFirst();
                if (doesGuildBanRoles) if (bannedRoles.indexOf(r.getId()) != -1) {
                    messageChannel.sendMessage("You cannot assign role **" + r.getName() + "**, as designated by the Guild admins. Please contact them if you think this is incorrect.").queue();
                    continue;
                }
                if (!roleHelpers.isPresent()) {
                    messageChannel.sendMessage("Your Guild has not set role: **" + r.getName() + "** as a role that can be assigned. Please contact an administrator if you think this is incorrect.").queue();
                } else {
                    RoleHelper roleHelper = roleHelpers.get();
                    if (userPerm < roleHelper.getPermID()) {
                        messageChannel.sendMessage("You cannot assign role: **" + r.getName() + "** as it has a higher permission level than yours. Please use !listperm.").queue();
                        continue;
                    }
                    for (Member m : sentMembers) {
                        assignLoop:
                        if (doesGuildHaveVerifiedRoles) {
                            List<String> verifiedRoles = settings.getVerifiedRoles();

                            for (String s : verifiedRoles) {
                                Role role = event.getGuild().getRoleById(s);
                                if (!m.getRoles().contains(role)) {
                                    messageChannel.sendMessage("User: **" + m.getEffectiveName() + "** does not have role: **" + role.getName() + "**, which is required to have in order for them to be assigned roles. Please contact an administrator.").queue();
                                    break assignLoop;
                                }
                            }
                        }

                        if (m.getEffectiveName().contains(settings.getNameChar())) {
                            if (m.getRoles().contains(r)) {
                                messageChannel.sendMessage("User: **" + m.getEffectiveName() + "** already has role: **" + r.getName() + "**.").queue();
                            } else {
                                messageChannel.sendMessage("User **" + m.getEffectiveName() + "** was assigned role: **" + r.getName() + "**.").queue();
                                event.getEvent().getGuild().addRoleToMember(m, r).queue();
                            }
                        } else {
                            messageChannel.sendMessage("Your guild has designated that users' names must be formatted in this way: \n\n\"Firstname " + settings.getNameChar() + " Gamertag\"" + "\n\n Please tell: **" + m.getEffectiveName() + "** to format their name as such.").queue();
                        }
                    }
                }
            }
        }
    }
}