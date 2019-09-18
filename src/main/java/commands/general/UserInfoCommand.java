package commands.general;

import commands.util.Command;
import commands.util.CommandEvent;
import commands.util.CommandListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import util.BotEmbed;

import java.util.Date;
import java.util.List;

public class UserInfoCommand extends Command {
    public UserInfoCommand() {
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
        int userPermLevel = event.getUserPermLevel();

        if (!memberList.isEmpty()) {
            for (Member m : memberList) {
                userPermLevel = event.getCommandListener().getHighestUserPermission(m.getRoles(), event.getSettings().getRoleHelper());
                channel.sendMessage(buildMessage(m, event, userPermLevel).build()).queue();
            }
        } else if (args[1] == null) {
            channel.sendMessage(buildMessage(user, event, userPermLevel).build()).queue();
        } else {
            channel.sendMessage("Incorrect command arguments. Use "+ event.getSettings().getPrefix() +"help userinfo for help!").queue();
        }
    }

    private EmbedBuilder buildMessage(Member user, CommandEvent event, int userPermLevel) {
        EmbedBuilder embedBuilder = BotEmbed.getBotEmbed(event);
        Date date = new Date(user.getTimeJoined().toInstant().toEpochMilli());

        embedBuilder.setThumbnail(user.getUser().getAvatarUrl());
        embedBuilder.addField("Nickname: ", user.getEffectiveName(), true);
        embedBuilder.addField("Discord Name: ", user.getUser().getAsTag(), true);
        embedBuilder.addField("UserID: ", user.getId(), true);
        embedBuilder.addField("Total Roles: ", Integer.toString(user.getRoles().size()), true);
        embedBuilder.addField("Highest permission level in this Guild: ", Integer.toString(userPermLevel), false);
        embedBuilder.addField("User status: ", user.getOnlineStatus().getKey(), true);
        embedBuilder.addField("Date joined: ", date.toString(), true);
        date = new Date(user.getTimeCreated().toInstant().toEpochMilli());
        embedBuilder.addField("Account created: ", date.toString(), false);

        return embedBuilder;
    }
}
