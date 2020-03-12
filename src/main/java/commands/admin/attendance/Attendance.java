package commands.admin.attendance;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.MessageChannel;
import util.PropLoader;
import util.Settings;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        if (AttendancePass.attendanceMap != null) {
            if (args[1] == null) {
                messageChannel.sendMessage("Please enter the password!").queue();
            } else if (!args[1].isEmpty()) {
                ArrayList<String> message = new ArrayList<>(Arrays.asList(args[1].split("\\s")));
                System.out.println(message.size());
                if (message.get(0).equals(settings.getAttendancePassword())) {
                    System.out.println(message);
                    if (message.size() == 2) {
                        String name;
                        String inputName = message.get(1);

                        if (event.getEvent().getMember() != null) {
                            name = event.getEvent().getMember().getEffectiveName();
                        } else {
                            name = event.getEvent().getMember().getUser().getName();
                        }


                        AttendancePass.attendanceMap.put(name, inputName);
                        event.getEvent().getMessage().delete().queue();

                        try {
                            Class.forName("com.mysql.cj.jdbc.Driver");

                            String db_name = PropLoader.getProp("db_name");
                            String db_addr = PropLoader.getProp("db_addr");
                            String db_user = PropLoader.getProp("db_user");
                            String db_pass = PropLoader.getProp("db_pass");

                            LocalDateTime nowTime = LocalDateTime.now();
                            DateTimeFormatter format = DateTimeFormatter.ofPattern("YYYY/MM/dd hh:mm:ss");
                            String time = nowTime.format(format);

                            try {
                                Connection connection = DriverManager.getConnection("jdbc:mysql://" + db_addr + ":3306/" + db_name, db_user, db_pass);
                                Statement statement = connection.createStatement();
                                String sqlStatement = "insert into Attendance " + "(dateTime, userID, nickname, discordID) values (" + "STR_TO_DATE('" + time + "', '%Y/%m/%d %H:%i:%s'), " + "'" + event.getEvent().getMember().getId() + "', " + "'" + inputName + "', " + "'" + event.getEvent().getMember().getUser().getAsTag() + "'" + ");";

                                statement.execute(sqlStatement);

                                System.out.println("Successfully Updated DB!\n" + sqlStatement);

                            } catch (SQLException e) {
                                System.out.println("Exception: " + e.getMessage());
                            }

                        } catch (ClassNotFoundException e) {
                            System.out.println("No Mysql Driver Found!");
                            System.out.println("Exception: " + e.getMessage());
                        }

                        messageChannel.sendMessage("Took attendance for " + name + ".").queue();


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
}
