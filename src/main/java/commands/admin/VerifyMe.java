package commands.admin;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.MessageChannel;

public class VerifyMe extends Command {
    public VerifyMe()
    {
        name = "verifyme";
        description = "Get information about student verification.";
        helpDescription = "Get information about student verification.";
        requiredPermission = 0;
        usages.add("verifyme");
    }

    @Override
    public void onCommandReceived(CommandEvent event) {
        event.getEvent().getAuthor().openPrivateChannel().queue(privateChannel -> {
            String message = "Hi there! To get the FSU Student role on Esports at FSU, please first register on NoleCentral:\n" +
                    "https://nolecentral.dsa.fsu.edu/organization/floridastateesports \n\n" +
                    "From there, you will get access to a google form where you can enter your .fsu email address. You ***must*** use your FSU email for this, otherwise it will not work.\n" +
                    "After receiving your password, please DM me the phrase you receive.";
            privateChannel.sendMessage(message).queue();
            });
    }

}
