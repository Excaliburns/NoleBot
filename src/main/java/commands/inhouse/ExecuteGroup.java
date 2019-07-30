package commands.inhouse;

import commands.util.CommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.restaction.PermissionOverrideAction;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

class ExecuteGroup {
    ExecuteGroup(CommandEvent event, Inhouse inhouse, InhouseStruct struct)
    {
        boolean categoryNotSet = struct.getCategoryID().isEmpty();

        if(categoryNotSet)
            event.getChannel().sendMessage("You / Your server admins have not set up the automatic LFG voice/text creation Category. Please contact them for assistance.").queue();

        ArrayList<Member> groupMembers = new ArrayList<>();

        for(String s : inhouse.getUserList())
        {
            try{ groupMembers.add(event.getGuild().getMemberById(s)); }
            catch(NullPointerException e) { event.getChannel().sendMessage("Could not find Member **" + s + "**").queue();}
        }

        if(categoryNotSet)
        {
            for(Member member : groupMembers)
                member.getUser().openPrivateChannel().queue( (privateChannel -> privateChannel.sendMessage("Your group has been filled! Please contact **" + event.getGuild().getMemberById(inhouse.getUserList().get(0)).getNickname() + "** to play!" ).queue()));
        }
        else {
            Category creationCategory = event.getGuild().getCategoryById(struct.getCategoryID());

            creationCategory.createTextChannel(inhouse.getInhouseName()).complete();
            creationCategory.createVoiceChannel(inhouse.getInhouseName()).complete();

            TextChannel textChannel = event.getGuild().getTextChannelsByName(inhouse.getInhouseName(), false).get(0);
            VoiceChannel voiceChannel = event.getGuild().getVoiceChannelsByName(inhouse.getInhouseName(), false).get(0);

            for (Member member : groupMembers) {
                member.getUser().openPrivateChannel().queue((privateChannel -> privateChannel.sendMessage("Your group has been created! Please find your group voice and chat channel in **" + event.getGuild().getName() + "** under the **" + creationCategory.getName() + "** Category.").queue()));

                PermissionOverrideAction voicePermissionOverride = voiceChannel.createPermissionOverride(member);

                voicePermissionOverride.setAllow(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK, Permission.VIEW_CHANNEL).queue();

                PermissionOverrideAction textPermissionOverride = textChannel.createPermissionOverride(member);

                textPermissionOverride.setAllow(Permission.VIEW_CHANNEL, Permission.MESSAGE_READ, Permission.MESSAGE_WRITE).queue();
            }

            textChannel.sendMessage("This channel and its corresponding voice channel will be deleted in: **" + inhouse.getDuration() + "** hour(s).").queue();

            voiceChannel.delete().queueAfter(inhouse.getDuration(), TimeUnit.HOURS);
            textChannel.delete().queueAfter(inhouse.getDuration(), TimeUnit.HOURS);
        }
    }
}
