package util;

import net.dv8tion.jda.api.entities.Guild;

public class Settings
{
    private String guildID;
    private String prefix;
    private int permLevel;


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


}
