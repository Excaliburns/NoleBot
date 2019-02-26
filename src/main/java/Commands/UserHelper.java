package Commands;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class UserHelper {
    public static boolean isVerified(Member authorOfMessage) {
        List<Role> roleList = new LinkedList<>(authorOfMessage.getRoles());
        for (Role var : roleList) {
            String name = var.getName();
            if (name.equals("FSU Faculty/Staff") ||
                    name.equals("FSU Alumni") ||
                    name.equals("Guest") ||
                    name.equals("FSU Student")) {
                return true;
            }
        }

        return false;
    }

    public static boolean isGameManager (Member authorOfMessage)
    {
        List<Role> roleList = new LinkedList<>(authorOfMessage.getRoles());
        Iterator<Role> roleIterator = roleList.iterator();

        if(authorOfMessage.hasPermission(Permission.ADMINISTRATOR))
            return true;

        while(roleIterator.hasNext())
        {
            if(roleIterator.next().getName().equals("Game Manager"))
            {
                return true;
            }
            roleIterator.remove();
        }

        return false;
    }
    public static boolean nameMatch(String MemberName) { return true; }
}
