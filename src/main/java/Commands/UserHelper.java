package Commands;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

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
    public static boolean nameMatch(String MemberName)
    {
        return true;
    }
}