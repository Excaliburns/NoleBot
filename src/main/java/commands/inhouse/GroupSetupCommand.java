package commands.inhouse;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.Category;
import util.JSONLoader;

public class GroupSetupCommand extends Command {
    public GroupSetupCommand()
    {
        name = "lfgsetup";
        description = "Admin command to setup inhouse channel / voice creation category.";
        helpDescription = "Admin command to setup inhouse channel / voice creation category.";
        requiredPermission = 1000;
    }
    @Override
    public void onCommandReceived(CommandEvent event) {
        GroupStruct groupStruct = JSONLoader.inhouseLoader(event.getSettings());
        Category category = event.getEvent().getMessage().getTextChannel().getParent();

        if(category == null)
        {
            event.getChannel().sendMessage("Category returned null. There may be no category this is assigned to, please make a category for lfg groups and try again.").queue();
            return;
        }
        if(groupStruct == null)
        {
            event.getChannel().sendMessage("Something went terribly wrong creating your Guild's inhouse file. Please contact your Guild admins and report this bug.").queue();
            return;
        }

        groupStruct.setCategoryID(category.getId());

        event.getChannel().sendMessage("Saved LFG Category " + category.getName() + " with ID " + category.getId()).queue();

        JSONLoader.saveInhouseData(groupStruct, event.getGuildID());
    }
}
