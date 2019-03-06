package util;

import net.dv8tion.jda.api.entities.Guild;

import java.util.List;

/*
JSON/Gson Helper class.
 */
public class Settings {
    private String prefix = "!";
    private String guildID;
    private boolean init;
    private List<RoleHelper> roleHelper;

    private Settings() {
        this.guildID = null;
        this.roleHelper = null;
        this.init = false;
    }

    public Settings(String guildID) {
        this.guildID = guildID;
        this.roleHelper = null;
        this.init = false;
    }

    public Settings(String guildID, List<RoleHelper> roleHelper) {
        this.guildID = guildID;
        this.roleHelper = roleHelper;
        this.init = true;
    }

    public Settings(String prefix, String guildID, List<RoleHelper> roleHelper) {
        this.prefix = prefix;
        this.guildID = guildID;
        this.roleHelper = roleHelper;
        this.init = true;
    }

    public boolean isInit() {
        return init;
    }

    public List<RoleHelper> getRoleHelper() {
        return roleHelper;
    }

    public void setRoleHelper(List<RoleHelper> roleHelper) {
        this.roleHelper = roleHelper;
    }

    public static Settings getSettings(Guild guild) {
        return new Settings(guild.getId());
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getGuildID() {
        return guildID;
    }

    public void setGuildID(String guildID) {
        this.guildID = guildID;
    }
}
