package util;

import net.dv8tion.jda.api.entities.Guild;

public class Settings
{
    private String guildID;
    private String prefix;

    private int permLevel;

    private Settings()
    {
        this.guildID = null;
        this.prefix = null;
        this.permLevel = 0;
    }

    public Settings(String guildID)
    {
        this.guildID = guildID;
    }

    public Settings(Guild guild)
    {
        this.guildID = guild.getId();
    }

    public Settings setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public static Settings getSettings(Guild guild)
    {
        return getSettings(guild.getId());
    }

    public static Settings getSettings(String guildID)
    {
        //JSONLoader.getGuildJSON()
        return new Settings(guildID);
    }

    public static Settings getSettingsNull(Guild guild)
    {
        return null;
    }
}
