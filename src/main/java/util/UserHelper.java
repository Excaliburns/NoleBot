package util;


import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.*;


public class UserHelper {

    public static int getHighestUserPermission(List<Role> userRoles, List<RoleHelper> guildRoles) {
        HashMap<String, RoleHelper> roleHelperList = new HashMap<>();
        ArrayList<RoleHelper> presentRoles = new ArrayList<>();

        guildRoles.forEach(roleHelper -> roleHelperList.put(roleHelper.getRoleID(), roleHelper));

        for (Role role : userRoles) {
            String roleID = role.getId();

            if (roleHelperList.containsKey(roleID)) presentRoles.add(roleHelperList.get(roleID));
        }
        presentRoles.sort(Comparator.comparing(RoleHelper::getPermID).reversed());

        if (presentRoles.isEmpty()) return -1;
        else return presentRoles.get(0).getPermID();
    }

    public static boolean isVerified(Member authorOfMessage) {
        List<Role> roleList = new LinkedList<>(authorOfMessage.getRoles());
        for (Role var : roleList) {
            String name = var.getName();
            if (name.equals("FSU Faculty/Staff") || name.equals("FSU Alumni") || name.equals("Guest") || name.equals("FSU Student")) {
                return true;
            }
        }

        return false;
    }
}
