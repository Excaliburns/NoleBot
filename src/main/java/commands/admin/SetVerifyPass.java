package commands.admin;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.MessageChannel;

public class SetVerifyPass extends Command {
    public SetVerifyPass()
    {
        name = "verifypassword";
        description = "Admin command to set the verified roles password.";
        helpDescription = "Admin command to set the verified roles password.";
        requiredPermission = 1000;
        usages.add("verifypassword [password]");
    }

    @Override
    public void onCommandReceived(CommandEvent event) {
        String[] eventMessage = event.getMessage().clone();
        MessageChannel messageChannel = event.getChannel();


        if (eventMessage[1] == null)
            messageChannel.sendMessage("You must put the new password after the command.").queue();
        else if(eventMessage[1]){

        }
        else if(eventMessage.length > 1)
            messageChannel.sendMessage("Too many arguments.").queue();
    }
}
