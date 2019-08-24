package commands.util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import util.Settings;

/*
Build this event to pass to the command. You can implement getters and setters here for almost anything since you have access to the MessageReceivedEvent.
 */
public class CommandEvent {
    private final GuildMessageReceivedEvent event;
    private String[] message;
    private final CommandListener commandListener;
    private final String prefix;
    private final Guild guild;
    private final MessageChannel channel;
    private final String guildID;
    private final Settings settings;
    private final int userPermLevel;

    public CommandEvent(GuildMessageReceivedEvent event, String[] message, CommandListener commandListener, int userPermLevel) {
        this.event = event;
        this.message = message;
        this.commandListener = commandListener;
        this.channel = event.getChannel();
        this.guildID = event.getGuild().getId();
        this.settings = commandListener.getSettingsHashMap().get(guildID);
        this.prefix = getSettings().getPrefix();
        this.guild = event.getGuild();
        this.userPermLevel = userPermLevel;
    }

    public GuildMessageReceivedEvent getEvent() {
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

    public MessageChannel getChannel() {
        return channel;
    }

    public String getGuildID() {
        return guildID;
    }

    public Settings getSettings() {
        return settings;
    }

    public String getPrefix() {
        return prefix;
    }

    public Guild getGuild() {
        return guild;
    }

    public int getUserPermLevel() {
        return userPermLevel;
    }
}
