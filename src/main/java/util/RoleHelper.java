package util;

public class RoleHelper {
    private String roleID;
    private int permID;

    public RoleHelper(String roleID, int permID) {
        this.roleID = roleID;
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

    public void setPermID(int permID) {
        this.permID = permID;
    }
}
