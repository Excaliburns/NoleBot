package commands.inhouse;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import util.JSONLoader;
import util.Settings;

import java.util.ArrayList;
import java.util.List;

public class InhouseCommand extends Command {
    public InhouseCommand() {
        name = "inhouse";
        description = "Inhouse command to find and create inhouses within your guild!";
        helpDescription = "This command is used to create inhouses for players in your guild. ";
        requiredPermission = 100;
    }

    @Override
    public void onCommandReceived(CommandEvent event) {
        Settings guildSettings = event.getSettings();
        String[] eventMessage = event.getMessage().clone();
        InhouseStruct inhouseStruct = JSONLoader.inhouseLoader(guildSettings);
        List<Inhouse> currentInhouses = inhouseStruct.getInhouses();
        MessageChannel messageChannel = event.getChannel();

        if (eventMessage[1] == null) {
            if (currentInhouses != null) {
                messageChannel.sendMessage("Here are the inhouses currently being formed: ").queue();
                for (Inhouse l : currentInhouses) {
                    StringBuilder listofUsers = new StringBuilder();
                    for (String u : l.getUserList()) {
                        listofUsers.append(event.getGuild().getMemberById(u).getNickname());
                        listofUsers.append("\n");
                    }
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.addField("Name: ", l.getInhouseName(), true);
                    embedBuilder.addField("No. of Players Needed:", Integer.toString(l.getRequiredPlayers()), true);
                    embedBuilder.addField("No. of Players in Queue:", Integer.toString(l.getPlayerCount()), true);
                    embedBuilder.addField("Creator: ", event.getGuild().getMemberById(l.getUserList().get(0)).getNickname(), true);
                    embedBuilder.addField("Players in Queue: ", listofUsers.toString(), false);
                    messageChannel.sendMessage(embedBuilder.build()).queue();
                }
            } else {
                messageChannel.sendMessage("There are no inhouses currently being formed. Use !help inhouse to learn how to start one!").queue();
            }
        }
        else
        {
            String message = eventMessage[1];
            String[] args = message.split("\\s");

            if (args[0].trim().equals("create")) {

                if (args.length == 1) {
                    messageChannel.sendMessage("You did not specific a name. Please use !help inhouse for usage.").queue();
                    return;
                }
                if (args.length == 2) {
                    messageChannel.sendMessage("You did not specify the required amount of players. Please use !help inhouse for usage.").queue();
                    return;
                }

                int requiredPlayers;
                try {
                    requiredPlayers = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    messageChannel.sendMessage("You need to use a number for your required players. Please use !help inhouse for usage.").queue();
                    return;
                }

                if(currentInhouses == null)
                    currentInhouses = new ArrayList<>();

                String name = args[1];

                if(name.length() > 50)
                {
                    messageChannel.sendMessage("Your inhouse name must be shorter than 50 characters.").queue();
                    return;
                }

                if (currentInhouses.stream().map(Inhouse::getInhouseName).anyMatch(name::equals)) {
                    messageChannel.sendMessage("You cannot create an inhouse with the same name as another inhouse. Please choose a different name.").queue();
                    return;
                }

                Inhouse inhouse = new Inhouse(name, requiredPlayers);
                inhouse.addPlayer(event.getEvent().getAuthor().getId());

                currentInhouses.add(inhouse);

                inhouseStruct.setInhouses(currentInhouses);

                JSONLoader.saveInhouseData(inhouseStruct, guildSettings.getGuildID());
            } else if (args[0].trim().equals("join")) {
                if (args[1] == null) {
                    messageChannel.sendMessage("You did not specify an inhouse to join. Use !inhouse to see all available inhouses.").queue();
                    return;
                }

                Inhouse foundInhouse = currentInhouses.stream().filter( inhouse -> args[2].equals(inhouse.getInhouseName())).findAny().orElse(null);

                if(foundInhouse == null)
                {
                    messageChannel.sendMessage("There is no inhouse with that name. Use !inhouse to list all available inhouses.").queue();
                    return;
                }
            }
        }
    }
}
