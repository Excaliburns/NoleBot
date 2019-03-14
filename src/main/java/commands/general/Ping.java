package commands.general;

import commands.util.Command;
import commands.util.CommandEvent;

public class Ping extends Command {

    public Ping() {
        name = "ping";
        description = "Pong!";
        helpDescription = "Sends a ping message to the bot.";
        requiredPermission = 0;
        usages.add("ping");
    }

    @Override
    public void onCommandReceived(CommandEvent event) {
        event.getEvent().getChannel().sendMessage("Pong!").queue();
    }
}
