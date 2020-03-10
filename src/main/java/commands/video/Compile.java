package commands.video;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Compile extends Command {

    public Compile() {
        name = "compile";
        description = "Compiles all the videos that you have posted in the specific channel with flags";
        helpDescription = "This command allows you to create compilation videos for yourself. You must specify a Time Filter e.g. Week, Month, Year";
        requiredPermission = 400;
        usages.add ("compile <@User> <Time Filter>");
    }

    @Override
    public void onCommandReceived(CommandEvent event) {
        Map<String, Integer> timeFilters = new HashMap<String, Integer> () {{
            put ("Week", 7);
            put ("Month", 31);
            put ("Year", 365);
        }};

        String[] args = event.getMessage ();
        MessageChannel messageChannel = event.getChannel ();
        List<Member> mentionedMembers = event.getEvent ().getMessage ().getMentionedMembers ();
        String videoPath = "/videos/" + mentionedMembers + "/";
        String outFilePath = videoPath + OffsetDateTime.now () + ".webm";

        if (mentionedMembers.size () != 1) {
            messageChannel.sendMessage ("Please specify a single member, please note that this command is costly").queue ();
            return;
        }
        Member mentionedMember = mentionedMembers.get (0);

        if (args[1] == null) {
            messageChannel.sendMessage ("You did not specify a time filter").queue ();
            return;
        } else if (!timeFilters.keySet ().contains (args[1])) {
            messageChannel.sendMessage ("Invalid time filter, please specify Week, Month or Year").queue ();
            return;
        }

        String timeFilter = args[1];

        OffsetDateTime messagesAfterThisDate = OffsetDateTime.now ().minusDays (timeFilters.get (timeFilter));

        List<Message> allMessagesInChannel = new ArrayList<> ();
        List<Message> videoMessagesByAuthor = new ArrayList<> ();

        messageChannel.getHistoryFromBeginning (Integer.MAX_VALUE).queue (messageHistory -> allMessagesInChannel.addAll (messageHistory.getRetrievedHistory ()));
        for (Message message : allMessagesInChannel) {
            if (message.getAuthor () == mentionedMember.getUser ()) {
                if (message.getTimeCreated ().isAfter (messagesAfterThisDate))
                    if (message.getAttachments ().size () > 0)
                        videoMessagesByAuthor.add (message);
            }
        }

        if (videoMessagesByAuthor.size () == 0) {
            messageChannel.sendMessage ("You do not have any videos posted after your time filter").queue ();
            return;
        }

        try {
            PrintWriter printWriter = new PrintWriter (videoPath + "videoNames.txt");
            for (Message message : videoMessagesByAuthor) {
                for (Message.Attachment attachment : message.getAttachments ()) {
                    if (getFileExtension (attachment.getFileName ()) == "webm") {
                        String attachmentFilenamePath = videoPath + attachment.getFileName ();
                        attachment.downloadToFile (attachmentFilenamePath);
                        printWriter.println (videoPath + attachmentFilenamePath);
                    }
                }
            }
            String[] ffmpegCommand = {"ffmpeg", "-f", "concat", "-safe", "0", "-i", videoPath + "videoNames.txt", "-c", "copy", outFilePath};
            ProcessBuilder pb = new ProcessBuilder (ffmpegCommand);
            Process p = pb.start ();
            p.wait ();
            messageChannel.sendMessage ("Here is " + mentionedMember + "'s compiled video").addFile (new File (outFilePath));
            return;
        } catch (IOException e) {
            messageChannel.sendMessage ("Error please contact admin " + e).queue ();
            return;
        } catch (InterruptedException e) {
            messageChannel.sendMessage ("Error please contact admin " + e).queue ();
            return;

        }
    }

    private String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf (".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return fileName.substring (lastIndexOf);
    }
}
