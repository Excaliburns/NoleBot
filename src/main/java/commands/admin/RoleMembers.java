package commands.admin;

import commands.admin.attendance.Attendance;
import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;

public class RoleMembers extends Command {

    public RoleMembers() {
        name = "rolemembers";
        requiredPermission = 1000;
        usages.add("rolemembers <@Role>");
    }

    @Override
    public void onCommandReceived(CommandEvent event) {
        List<Role> roleList = event.getEvent().getMessage().getMentionedRoles();

        for (Role r : roleList) {
            event.getChannel().sendMessage("Members in role " + r.getName() + ": " + event.getGuild().getMembersWithRoles(r).size()).queue();
        }
    }
}
