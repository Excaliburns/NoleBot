package commands.general;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Help extends Command {

    public Help() {
        name = "help";
        description = "Sends the help message. Also used to ask for help on other commands.";
        helpDescription = "Why would you use this? It sends you this message. Use !help to display all commands and a short description, use !help [command] to display more information about other commands.";
        requiredPermission = 0;
    }

    @Override
    public void onCommandReceived(CommandEvent event) {
        String[] message = event.getMessage();

        message = message[1] == null ? new String[0] : message[1].split("\\s+");

        if (message.length == 0)
            sendGenericHelp(event);
        else
            sendCommandHelp(event, message[0]);
    }

    private void sendGenericHelp(CommandEvent event) {
        List<Command> commandList = new ArrayList<>(event.getCommandListener().getCommands());

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.append("Commands:", MessageBuilder.Formatting.UNDERLINE, MessageBuilder.Formatting.BOLD);

        for (Command command : commandList) {
            messageBuilder.append("\n");
            messageBuilder.appendFormat(event.getSettings().getPrefix() + command.getName() + " - " + command.getDescription());
        }

        messageBuilder.append("Use !help [command] to get more information on a specific command. For example, \n !help prefix");
        System.out.println(messageBuilder.build());
        event.getChannel().sendMessage(messageBuilder.build()).queue();
    }

    private void sendCommandHelp(CommandEvent event, String command) {

        Optional<Command> optionalCommand = event.getCommandListener().getCommands().stream().filter(c -> c.getName().equals(command)).findAny();
        if (optionalCommand.isPresent() && !optionalCommand.get().getName().equals("")) {
            Command calledCommand = optionalCommand.get();

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(new Color(198, 77, 105));
            embedBuilder.addField("Command Name: ", calledCommand.getName(), true);
            embedBuilder.addField("Description: ", calledCommand.getHelpDescription(), false);
        }
    }
}
