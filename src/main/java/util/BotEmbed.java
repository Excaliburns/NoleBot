package util;

import commands.util.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class BotEmbed {

    public static EmbedBuilder getBotEmbed(CommandEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("NoleBot", "https://github.com/Excaliburns/NoleBot", event.getEvent().getJDA().getSelfUser().getAvatarUrl());
        embedBuilder.setFooter("NoleBot, a bot from Esports at Florida State", event.getEvent().getJDA().getSelfUser().getAvatarUrl());
        embedBuilder.setColor(new Color(198, 77, 105));

        return embedBuilder;
    }
}
