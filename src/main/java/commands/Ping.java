package commands;

import commands.util.Command;
import commands.util.CommandEvent;

public class Ping extends Command {

    public Ping()
    {
        name = "Ping";
        description = "Pong!";
        requiredPermission = 0;
    }
    @Override
    public void onCommandRecieved(CommandEvent event) {
        event.getEvent().getChannel().sendMessage("Pong!").queue();
    }


}
