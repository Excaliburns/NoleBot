package util;

import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
JSON/Gson Helper class.
 */
public class Settings {
    private String prefix = "!";
    private String guildID;
    private List<RoleHelper> roleHelper;
    private HashMap<String, Integer> commandHelper;
    private List<String> verifiedRoles = new ArrayList<>();
    private List<String> bannedRoles = new ArrayList<>();
    private String introMessage = "";
    private String nameChar = "|";
    private String verifyPass = "";
    private boolean init;

    private Settings() {
        this.guildID = null;
        this.roleHelper = null;
        this.commandHelper = null;
        this.init = false;
    }

    Settings(String guildID) {
        this.guildID = guildID;
        this.roleHelper = null;
        this.commandHelper = null;
        this.init = false;
    }

    public Settings(String guildID, List<RoleHelper> roleHelper) {
        this.guildID = guildID;
        this.roleHelper = roleHelper;
        this.commandHelper = null;
        this.init = true;
    }

    public Settings(String guildID, List<RoleHelper> roleHelper, HashMap<String, Integer> commandHelper) {
        this.guildID = guildID;
        this.roleHelper = roleHelper;
        this.commandHelper = commandHelper;
        this.init = true;
    }

    public Settings(String guildID, List<RoleHelper> roleHelper, HashMap<String, Integer> commandHelper, List<String> bannedRoles) {
        this.guildID = guildID;
        this.roleHelper = roleHelper;
        this.commandHelper = commandHelper;
        this.bannedRoles = bannedRoles;
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

    public HashMap<String, Integer> getCommandHelper() {
        return commandHelper;
    }

    public void setCommandHelper(HashMap<String, Integer> commandHelper) {
        this.commandHelper = commandHelper;
    }

    public List<String> getBannedRoles() {
        return bannedRoles;
    }

    public void setBannedRoles(List<String> bannedRoles) {
        this.bannedRoles = bannedRoles;
    }

    public String getIntroMessage() {
        return introMessage;
    }

    public void setIntroMessage(String introMessage) {
        this.introMessage = introMessage;
    }

    public String getNameChar() {
        return nameChar;
    }

    public void setNameChar(String nameChar) {
        this.nameChar = nameChar;
    }

    public List<String> getVerifiedRoles() {
        return verifiedRoles;
    }

    public void setVerifiedRoles(List<String> verifiedRoles) {
        this.verifiedRoles = verifiedRoles;
    }
}
