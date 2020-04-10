package commands.admin.attendance;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import util.JSONLoader;
import util.Settings;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AttendancePass extends Command {
    private static HashMap<String, ConcurrentHashMap<String, String>> attendanceMap;

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
                attendanceMap.put(event.getGuildID(), new ConcurrentHashMap<>());

                timer.schedule(task(event),300000);
            } else if(password.toLowerCase().equals("stop")) {
                timer.cancel();

                task(event).run();
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

    public static void initAttendanceMap() {
        attendanceMap = new HashMap<>();
    }

    public static ConcurrentHashMap<String, String> getAttendanceMap(String guildId) {
        return attendanceMap.get(guildId);
    }

    public static void insertAttendanceEntry(String guildId, String name, String inputName) {
        attendanceMap.get(guildId).put(name, inputName);
    }

    private TimerTask task(CommandEvent event) {
        ConcurrentHashMap<String, String> guildAttendanceMap = getAttendanceMap(event.getGuildID());

        return new TimerTask() {
            @Override
            public void run() {
                event.getChannel().sendMessage("Attendance has been filled out by: ").queue();

                MessageBuilder messageBuilder = new MessageBuilder();
                for (Map.Entry<String, String> attendanceEntry : guildAttendanceMap.entrySet()) {
                    messageBuilder.append(attendanceEntry.getKey());
                    messageBuilder.append(" - ");
                    messageBuilder.append(attendanceEntry.getKey());

                    if (messageBuilder.length() > 1500) {
                        event.getChannel().sendMessage(messageBuilder.build()).queue();
                        messageBuilder.clear();
                    }
                }

                event.getChannel().sendMessage(messageBuilder.build()).queue();

                attendanceMap.remove(event.getGuildID());
            }
        };
    }
}
