package commands.util;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/*
Build this event to pass to the command. You can implement getters and setters here for almost anything since you have access to the MessageReceivedEvent.

 */
public class CommandEvent
{
    private final MessageReceivedEvent event;
    private String[] message;
    private final CommandListener commandListener;


    public CommandEvent(MessageReceivedEvent event, String[] message, CommandListener commandListener) {
        this.event = event;
        this.message = message;
        this.commandListener = commandListener;
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
}
