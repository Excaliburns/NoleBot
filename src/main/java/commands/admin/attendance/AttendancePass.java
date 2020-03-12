package commands.admin.attendance;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.MessageChannel;
import util.JSONLoader;
import util.Settings;

import java.sql.Time;
import java.util.*;

public class AttendancePass extends Command {
    public static HashMap<String, String> attendanceMap;

    public AttendancePass() {
        name = "attendancepass";
        description = "Used for setting the password for attendance at club meetings.";
        helpDescription = "Used to set the password for attendance";
        requiredPermission = 1000;
        usages.add("attendancepass <Password>");
    }

    @Override
    public void onCommandReceived(CommandEvent event) {
        String[] args = event.getMessage();
        MessageChannel messageChannel = event.getChannel();
        Settings settings = event.getSettings();

        if (args.length == 1) {
            messageChannel.sendMessage("No arguments entered. Please try again.").queue();
        } else if (!args[1].isEmpty()) {
            ArrayList<String> message = new ArrayList<>(Arrays.asList(args[1].split("\\s")));
            String password = message.get(0);
            Timer timer = new Timer();

            if (password.toLowerCase().equals("start")) {
                attendanceMap = new HashMap<>();

                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        for(Map.Entry<String, String> settingsEntry : attendanceMap.entrySet()) {
                            messageChannel.sendMessage(settingsEntry.getKey() + " - " + settingsEntry.getValue()).queue();
                        }
                        attendanceMap = new HashMap<>();
                    }
                };

                timer.schedule(task,300000);

            } else if(password.toLowerCase().equals("stop")) {

                timer.cancel();
                for(Map.Entry<String, String> settingsEntry : attendanceMap.entrySet()) {
                    messageChannel.sendMessage(settingsEntry.getKey() + " - " + settingsEntry.getValue()).queue();
                }
                attendanceMap = new HashMap<>();

            } else {
                settings.setAttendancePassword(password);
                HashMap<String, Settings> updatedSettingsHashMap = event.getCommandListener().getSettingsHashMap();
                updatedSettingsHashMap.remove(event.getGuildID());
                updatedSettingsHashMap.put(event.getGuildID(), settings);
                event.getCommandListener().setSettingsHashMap(updatedSettingsHashMap);
                JSONLoader.saveGuildSettings(updatedSettingsHashMap);
                messageChannel.sendMessage("Set attendance password to: " + password + ".").queue();
            }
        } else {
            messageChannel.sendMessage("Command was not properly formatted").queue();
        }
    }
}
