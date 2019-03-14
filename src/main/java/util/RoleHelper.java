package util;

import net.dv8tion.jda.api.entities.Role;

/*
Also JSON/Gson Helper class.
 */
public class RoleHelper {
    private String roleID;
    private String roleName;
    private int permID;

    public RoleHelper(String roleID, String roleName, int permID) {
        this.roleID = roleID;
        this.roleName = roleName;
        this.permID = permID;
    }

    public RoleHelper(Role role, int permID)
    {
        this.roleID = role.getId();
        this.roleName = role.getName();
        this.permID = permID;
    }

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public int getPermID() {
        return permID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setPermID(int permID) {
        this.permID = permID;
    }

    public RoleHelper getRoleHelper(){ return this; }
}
