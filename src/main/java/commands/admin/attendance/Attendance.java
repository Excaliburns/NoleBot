package commands.admin.attendance;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.MessageChannel;
import util.DBUtils;
import util.DateTimeUtils;
import util.Settings;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

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

        if (AttendancePass.getAttendanceMap(event.getGuildID()) != null) {
            if (args[1] == null) {
                messageChannel.sendMessage("Please enter the password!").queue();
            } else if (!args[1].isEmpty()) {
                ArrayList<String> message = new ArrayList<>(Arrays.asList(args[1].split("\\s")));
                System.out.println(message.size());
                if (message.get(0).equals(settings.getAttendancePassword())) {
                    System.out.println(message);
                    if (message.size() > 1) {
                        String name;
                        String inputName;

                        if (message.size() == 2 && event.getEvent().getMember() != null) {
                            inputName = event.getEvent().getMember().getEffectiveName();
                        }
                        else if (message.size() == 2 && event.getEvent().getMember() == null) {
                            inputName = event.getEvent().getMember().getUser().getName();
                        }
                        else {
                            inputName = message.get(1);
                        }

                        if (event.getEvent().getMember() != null) {
                            name = event.getEvent().getMember().getEffectiveName();
                        } else {
                            name = event.getEvent().getMember().getUser().getName();
                        }
                        event.getEvent().getMessage().delete().queue();

                        AttendancePass.insertAttendanceEntry(event.getGuildID(), name, inputName);

                        String date = DateTimeUtils.getCurrentDateString();
                        String id = event.getEvent().getMember().getId();
                        String discordTag = event.getEvent().getMember().getUser().getAsTag();

                        if ( insertAttendanceData(date, id, inputName, discordTag) == -1) {
                            messageChannel.sendMessage("Was not able to update database for " + name + ". Check logs!").queue();
                        } else {
                            messageChannel.sendMessage("Took attendance for " + name + ".").queue();
                        }
                    } else {
                        messageChannel.sendMessage("Please enter your name after the password.").queue();
                    }
                } else {
                    messageChannel.sendMessage("The password was incorrect.").queue();
                }
            }
        } else {
            messageChannel.sendMessage("Attendance is not currently being taken.").queue();
        }
    }

    private int insertAttendanceData(String date, String userID, String nickName, String discordTag) {

        try {
            PreparedStatement statement = DBUtils.getPreparedAttendanceStatement();

                statement.setTimestamp(1, Timestamp.valueOf(date));
                statement.setString(2, userID);
                statement.setString(3, nickName);
                statement.setString(4, discordTag);

                return statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
