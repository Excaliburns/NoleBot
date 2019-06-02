package commands.inhouse;

import commands.util.Command;
import commands.util.CommandEvent;
import util.JSONLoader;
import util.Settings;

public class InhouseCommand extends Command
{
    public InhouseCommand()
    {
        name = "inhouse";
        description = "Inhouse command to find and create inhouses within your guild!";
    }

    @Override
    public void onCommandReceived(CommandEvent event) {
        Settings guildSettings = event.getSettings();
        String[] args = event.getMessage();
        InhouseStruct inhouseStruct = JSONLoader.inhouseLoader(guildSettings);

        if(args[1] == null)
        {
            for(Inhouse l : inhouseStruct.getInhouses())
            {

            }
        }
    }
}
