package util;

import net.dv8tion.jda.api.entities.Role;

public class User {


    private Role role;
    private int permLevel;

    public User() {
    }

    public User(Role role, int permLevel) {
        this.role = role;
        this.permLevel = permLevel;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getPermLevel() {
        return permLevel;
    }

    public void setPermLevel(int permLevel) {
        this.permLevel = permLevel;
    }

}
