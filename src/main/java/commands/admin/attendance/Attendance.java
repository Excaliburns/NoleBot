package commands.admin.attendance;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.MessageChannel;
import util.Settings;

import java.util.ArrayList;
import java.util.Arrays;

public class Attendance extends Command {

    public Attendance() {
        name = "attendance";
        description = "Used for taking attendance.";
        helpDescription = "Used to take attendance.";
        requiredPermission = 1;
        usages.add("attendance <Password> <Name>");
    }


    @Override
    public void onCommandReceived(CommandEvent event) {
        String[] args = event.getMessage();
        MessageChannel messageChannel = event.getChannel();
        Settings settings = event.getSettings();
        if (args.length == 1) {
            messageChannel.sendMessage("Please enter the password!").queue();
        } else if (!args[1].isEmpty()) {
            ArrayList<String> message = new ArrayList<>(Arrays.asList(args[1].split("\\s")));
            if (message.size() >= 2) {
                if (message.get(0).equals(settings.getAttendancePassword())) {
                    if (!message.get(1).isEmpty()) {
                        String name;

                        if (event.getEvent().getMember() != null) {
                            name = event.getEvent().getMember().getEffectiveName();
                        } else {
                            name = event.getEvent().getMember().getUser().getName();
                        }

                        AttendancePass.attendanceMap.put(name, message.get(1));
                        event.getEvent().getMessage().delete().queue();

                        messageChannel.sendMessage("Took attendance for " + name + ".").queue();

                    } else {
                        messageChannel.sendMessage("Please enter your name after the password.").queue();
                    }
                }
            }
        }
    }
}
