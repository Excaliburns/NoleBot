package commands.util;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.Settings;

/*
Build this event to pass to the command. You can implement getters and setters here for almost anything since you have access to the MessageReceivedEvent.

 */
public class CommandEvent
{
    private final MessageReceivedEvent event;
    private String[] message;
    private final CommandListener commandListener;
    private final MessageChannel channel;
    private final String guildID;
    private final Settings settings;


    public CommandEvent(MessageReceivedEvent event, String[] message, CommandListener commandListener) {
        this.event = event;
        this.message = message;
        this.commandListener = commandListener;
        this.channel = event.getChannel();
        this.guildID = event.getGuild().getId();
        this.settings = commandListener.getSettingsHashMap().get(guildID);
    }

    public MessageReceivedEvent getEvent() {
        return event;
    }

    public String[] getMessage() {
        return message;
    }

    public void setMessage(String[] message) {
        this.message = message;
    }

    public CommandListener getCommandListener() {
        return commandListener;
    }

    public MessageChannel getChannel() { return channel; }

    public String getGuildID() { return guildID; }

    public Settings getSettings() { return settings; }
}
