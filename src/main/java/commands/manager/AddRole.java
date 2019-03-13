package commands.manager;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import util.RoleHelper;
import util.Settings;
import util.UserHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static util.UserHelper.isVerified;
import static util.UserHelper.nameMatch;

public class AddRole extends Command {

    public AddRole() {
        name = "addrole";
        description = "Adds a role that is lower than your own permission level to a user - if the Guild has allowed it to be.";
        helpDescription = "This command allows you to assign roles to others that are lower than your own permission level. These roles must be added to the permissions list by your guild admins. The users you are trying to assign roles to must abide by server rules. Use !rules to see rules.";
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
        int userPerm = UserHelper.getHighestUserPermission(event.getEvent().getMember().getRoles(), settings.getRoleHelper());

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
            if (doesGuildBanRoles)
                bannedRoles = settings.getBannedRoles();

            for (Role r : sentRoles) {
                Optional<RoleHelper> roleHelper = settings.getRoleHelper().stream().filter(h -> r.getName().equals(h.getRoleName())).findFirst();
                if (doesGuildBanRoles)
                    if (bannedRoles.indexOf(r.getName()) != -1) {
                        messageChannel.sendMessage("You cannot assign role **" + r.getName() + "**, as designated by the Guild admins. Please contact them if you think this is incorrect.").queue();
                        continue;
                    }
                if (!roleHelper.isPresent()) {
                    messageChannel.sendMessage("Your Guild has not set role: **" + r.getName() + "** as a role that can be assigned. Please contact an administrator if you think this is incorrect.").queue();
                } else {
                    if (userPerm > roleHelper.get().getPermID()) {
                        messageChannel.sendMessage("You cannot assign role: **" + r.getName() + "** as it has a higher permission level than yours. Please use !listperm.").queue();
                        continue;
                    }
                    for (Member m : sentMembers) {
                        List<String> verifiedRoles = null;
                        if(doesGuildHaveVerifiedRoles)
                            verifiedRoles = settings.getVerifiedRoles();

                        if(isMemberVerified(m, verifiedRoles))
                        if (m.getEffectiveName().contains(settings.getNameChar())) {
                            if (m.getRoles().contains(r)) {
                                messageChannel.sendMessage("User: **" + m.getEffectiveName() + "** already has role: **" + r.getName() + "**.").queue();
                                return;
                            } else {
                                messageChannel.sendMessage("User **" + m.getEffectiveName() + "** was assigned role: **" + r.getName() + "**.").queue();
                                event.getEvent().getGuild().getController().addSingleRoleToMember(m, r).queue();
                            }
                        } else {
                            messageChannel.sendMessage("Your guild has designated that your name must be formatted as such: \n\"Firstname " + settings.getNameChar() + " Gamertag\"" + "\n Please format your name as such.").queue();
                        }
                    }
                }
            }
        }
    }

    private boolean isMemberVerified(Member m, List<String> verifiedRoles)
    {
        return true;
    }
}