package util;

import com.google.gson.Gson;
import net.dv8tion.jda.api.entities.Guild;

public class Settings
{
    private String prefix, guildID;
    private int permLevel;
    private RoleHelper roleHelper;

    private Settings()
    {
        this.guildID = null;
        this.roleHelper = null;
        this.prefix = null;
        this.permLevel = 0;
    }

    public Settings(String guildID)
    {
        this.guildID = guildID;
        this.roleHelper = null;
        this.prefix = null;
        this.permLevel = 0;
    }

    public Settings(Guild guild)
    {
        this.guildID = guild.getId();
    }

    public Settings(String prefix, String guildID, int permLevel, RoleHelper roleHelper) {
        this.prefix = prefix;
        this.guildID = guildID;
        this.permLevel = permLevel;
        this.roleHelper = roleHelper;
    }

    public static Settings getSettings(Guild guild)
    {
        return new Settings(guild.getId());
    }
}
