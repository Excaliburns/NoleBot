package commands.admin.attendance;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.MessageChannel;
import util.DBUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

public class AttendanceList extends Command {
    public AttendanceList() {
        name = "attendancelist";
        description = "Used for listing attendance.";
        helpDescription = "Used for listing attendance.";
        requiredPermission = 1000;
        usages.add("attendancelist all");
        usages.add("attendancelist <Since Date> <Percentage>");
    }

    @Override
    public void onCommandReceived(CommandEvent event) {
        String[] args = event.getMessage();
        MessageChannel messageChannel = event.getChannel();

        if (args[1] == null) {
            messageChannel.sendMessage("Please enter an argument").queue();
        } else if (!args[1].isEmpty()){
            ArrayList<String> message = new ArrayList<>(Arrays.asList(args[1].split("\\s")));

            if (message.get(0).toLowerCase().equals("all")) {
            }
        }
    }
}
