package commands.admin;

import commands.util.Command;
import commands.util.CommandEvent;

public class TweetChannel extends Command {

    @Override
    public void onCommandReceived(CommandEvent event) {
        this.name = "tweetchannel";
        this.description = "This command will provide the setup process in order to have an automatic channel that gets posted to twitter. This requires the twitter client and OAuth keys to be set in twitter.properties.";
        this.helpDescription = "Sets the autotweet channel for your server.";
        this.requiredPermission = 1000;
    }
}
