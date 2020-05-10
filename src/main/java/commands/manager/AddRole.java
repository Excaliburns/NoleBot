package commands.manager;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.MessageBuilder;
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
        String prefix = event.getPrefix();
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
            messageChannel.sendMessage("You did not enter any arguments! Please use " + prefix + "help addrole for more information").queue();
        } else {
            List<String> bannedRoles = null;
            if (doesGuildBanRoles) bannedRoles = settings.getBannedRoles();

            for (Role r : sentRoles) {
                MessageBuilder builder = new MessageBuilder();
                Optional<RoleHelper> roleHelpers = settings.getRoleHelper().stream().filter(h -> r.getId().matches(h.getRoleID())).findFirst();
                if (doesGuildBanRoles) if (bannedRoles.contains(r.getId())) {
                    messageChannel.sendMessage("You cannot assign role **" + r.getName() + "**, as designated by the Guild admins. Please contact them if you think this is incorrect.").queue();
                    continue;
                }
                if (!roleHelpers.isPresent()) {
                    messageChannel.sendMessage("Your Guild has not set role: **" + r.getName() + "** as a role that can be assigned. Please contact an administrator if you think this is incorrect.").queue();
                } else {
                    RoleHelper roleHelper = roleHelpers.get();
                    if (userPerm < roleHelper.getPermID()) {
                        messageChannel.sendMessage("You cannot assign role: **" + r.getName() + "** as it has a higher permission level than yours. Please use " + prefix + "listperm.").queue();
                        continue;
                    }
                    for (Member m : sentMembers) {
                        if (doesGuildHaveVerifiedRoles) {
                            List<String> verifiedRoles = settings.getVerifiedRoles();
                            if(!hasVerifiedRole(m, verifiedRoles, event))
                                continue;
                        }

                        if (m.getEffectiveName().contains(settings.getNameChar())) {
                            if (m.getRoles().contains(r)) {
                                messageChannel.sendMessage("User: **" + m.getEffectiveName() + "** already has role: **" + r.getName() + "**.").queue();
                            } else {
                                event.getEvent().getGuild().addRoleToMember(m, r).queue();

                                if (builder.length() > 1500) {
                                    builder.append("User **")
                                            .append(m.getEffectiveName())
                                            .append("** was assigned role: **")
                                            .append(r.getName())
                                            .append("**.");
                                } else {
                                    messageChannel.sendMessage(builder.build()).queue();
                                    builder.clear();
                                }

                                messageChannel.sendMessage(builder.build()).queue();
                            }
                        } else {
                            messageChannel.sendMessage("Your guild has designated that users' names must be formatted in this way: \n\n\"Firstname " + settings.getNameChar() + " Gamertag\"" + "\n\n Please tell: **" + m.getEffectiveName() + "** to format their name as such.").queue();
                        }
                    }
                }
            }
        }
    }

    private boolean hasVerifiedRole(Member member, List<String> verifiedRoles, CommandEvent event)
    {
        for (String s : verifiedRoles) {
            Role role = event.getGuild().getRoleById(s);

            if(role == null) {
                event.getChannel().sendMessage("Could not find verified role. Assigning role.").queue();
                return true;
            }

            if (member.getRoles().contains(role)) {
                return true;
            }

            if (!member.getRoles().contains(role)) {
                event.getChannel().sendMessage("User: **" + member.getEffectiveName() + "** does not have role: **" + role.getName() + "**, which is required to have in order for them to be assigned roles. Please contact an administrator.").queue();
                return false;
            }
        }

        return true;
    }
}