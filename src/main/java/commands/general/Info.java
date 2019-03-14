package commands.general;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import util.BotEmbed;

public class Info extends Command {

    public Info()
    {
        name = "info";
        description = "Gives you some information about the bot.";
        helpDescription = "Tells you something about me! Stuff like my creator, my github, and my purpose.";
        requiredPermission = 0;
        usages.add("info");
    }

    //Big janky, was tired.
    //TODO:: make this better
    @Override
    public void onCommandReceived(CommandEvent event) {
        EmbedBuilder embedBuilder = BotEmbed.getBotEmbed(event);
        JDA selfUser = event.getEvent().getJDA();

        User creator = event.getEvent().getJDA().getUserById("92081050519343104");
        User maintainer = event.getEvent().getJDA().getUserById("140893732525441024");
        String maintainers = ", " + creator.getAsTag();

        embedBuilder.setThumbnail(creator.getAvatarUrl());
        embedBuilder.addField("Creator: ", creator.getAsTag(), true);
        embedBuilder.addField("Maintainers: ", maintainers, true);
        embedBuilder.addField("Version: ", "2.0.1", true);
        embedBuilder.addField("Total Guilds joined: ", Integer.toString(selfUser.getGuilds().size()), true);
        embedBuilder.addField("Purpose: ", "Purposed for use in Esports discords. Was originally written for the Esports at Florida State discord community. Contact my creator if you'd like to join, or if you have questions / suggestions about me! You can also click the title of this message to go to my GitHub page, where you can submit an issue or a feature request.", true);
        embedBuilder.addField("Total love for my users: ", "Infinite", false);

        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
