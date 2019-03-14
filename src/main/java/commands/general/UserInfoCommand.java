package commands.general;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import util.BotEmbed;
import util.UserHelper;

import java.util.Date;
import java.util.List;

public class UserInfoCommand extends Command {
    public UserInfoCommand()
    {
        name = "userinfo";
        description = "Lists information about the user.";
        helpDescription = "This command tells you information about yourself, or anyone you mention, such as their ClientID, any roles that give them a permission level, and other information about them.";
        requiredPermission = 0;
        usages.add("userinfo");
        usages.add("userinfo [@User, as many as you want]");
    }
    @Override
    public void onCommandReceived(CommandEvent event) {
        String[] args = event.getMessage();
        MessageChannel channel = event.getChannel();
        Member user = event.getEvent().getMember();
        List<Member> memberList = event.getEvent().getMessage().getMentionedMembers();

        if(!memberList.isEmpty())
        {
            for(Member m : memberList)
            {
                channel.sendMessage(buildMessage(m, event).build()).queue();
            }
        }
        else if(args[1] == null)
        {
            channel.sendMessage(buildMessage(user, event).build()).queue();
        }
        else
        {
            channel.sendMessage("Incorrect command arguments. Use  !help userinfo for help!").queue();
        }
    }

    private EmbedBuilder buildMessage(Member user, CommandEvent event)
    {
        EmbedBuilder embedBuilder = BotEmbed.getBotEmbed(event);
        Date date = new Date(user.getTimeJoined().toInstant().toEpochMilli());

        embedBuilder.setThumbnail(user.getUser().getAvatarUrl());
        embedBuilder.addField("Nickname: ", user.getEffectiveName(), true);
        embedBuilder.addField("Discord Name: ", user.getUser().getAsTag(), true);
        embedBuilder.addField("UserID: ", user.getId(), true);
        embedBuilder.addField("Total Roles: " , Integer.toString(user.getRoles().size()), true);
        embedBuilder.addField("Highest permission level in this Guild: ", Integer.toString(UserHelper.getHighestUserPermission(user.getRoles(), event.getSettings().getRoleHelper())), false);
        embedBuilder.addField("User status: ", user.getOnlineStatus().getKey(), true);
        embedBuilder.addField("Date joined: ", date.toString(), true);
        date = new Date(user.getTimeCreated().toInstant().toEpochMilli());
        embedBuilder.addField("Account created: ", date.toString(), false);

        return embedBuilder;
    }
}
