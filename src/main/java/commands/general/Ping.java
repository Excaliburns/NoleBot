package commands.general;

import commands.util.Command;
import commands.util.CommandEvent;

public class Ping extends Command {

    public Ping() {
        name = "ping";
        description = "Pong!";
        requiredPermission = 1000;
    }

    @Override
    public void onCommandReceived(CommandEvent event) {
        event.getEvent().getChannel().sendMessage("Pong!").queue();
    }
}
