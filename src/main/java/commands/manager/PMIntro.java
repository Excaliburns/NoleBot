package commands.manager;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class PMIntro extends ListenerAdapter {
    public void onMessageReceived(GuildMemberJoinEvent event) {
        Member newMember = event.getMember();
        User newMemUser = newMember.getUser();
        if (!newMember.getUser().isBot()) {
            newMemUser.openPrivateChannel().queue( (channel) -> channel.sendMessage("hello").queue() );
        }
    }

    private String PersonalMessage(User user) {
        return "Hello " + user.getName() + ", and welcome to the Discord server for Esports at Florida State University.\n" +
                "\n" +
                "Our discord is formatted with the intention of streamlining the user experience for your pleasure, in order to do this we will ask a few things of you first.\n" +
                "So please look at the rules posted in the Welcome tab before you get started. \n" +
                "In order for you to receive roles in our server you must:\n" +
                "First change your nickname to the following format [First Name | Username].\n" +
                "Then message an admin to receive a student, faculty, alumni, or guest role\n." +
                "\n" +
                "Afterwards you can add roles, thus granting you access to certain games channels.\n" +
                "To do so use the addrole command which format is as follows \"addrole (list of mentioned roles)\". \n" +
                "If you are a student interested in joining a team or getting involved with the club feel free to get in contact with one of the Officers listed at the top right of the channel. Many games have a designated game manager (GM) which are listed in the welcome tab.\n" +
                "\n" +
                "Thank you for joining our server!";
    }
}
