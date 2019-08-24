package commands.inhouse;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.Category;
import util.JSONLoader;

public class InhouseSetupCommand extends Command {
    public InhouseSetupCommand()
    {
        name = "lfgsetup";
        description = "Admin command to setup inhouse channel / voice creation category.";
        helpDescription = "Admin command to setup inhouse channel / voice creation category.";
        requiredPermission = 1000;
    }
    @Override
    public void onCommandReceived(CommandEvent event) {
        InhouseStruct inhouseStruct = JSONLoader.inhouseLoader(event.getSettings());
        Category category = event.getEvent().getMessage().getTextChannel().getParent();


        inhouseStruct.setCategoryID(category.getId());

        event.getChannel().sendMessage("Saved LFG Category " + category.getName() + " with ID " + category.getId()).queue();

        JSONLoader.saveInhouseData(inhouseStruct, event.getGuildID());
    }
}
