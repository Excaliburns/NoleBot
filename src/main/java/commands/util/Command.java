package commands.util;

import java.util.ArrayList;
import java.util.Arrays;

/*
Command class. Here the CommandEvent is passed to the actual command.
Otherwise this is just a skeleton class to implement when you make an actual command.
 */
public abstract class Command {
    protected String name = "";
    protected String description = "No data available.";
    protected String helpDescription = "No data available.";
    protected ArrayList<String> usages = new ArrayList<>();
    protected int requiredPermission = 1000;
    protected Command[] totalCommands = new Command[0];

    public abstract void onCommandReceived(CommandEvent event);

    public final void execute(CommandEvent event) {
        if (event.getMessage().length > 0) {
            String[] message = event.getMessage();

            for (Command commands : totalCommands) {

                event.setMessage(message.length > 1 ? Arrays.copyOfRange(message, 1, message.length) : new String[0]);
                commands.execute(event);
                return;
            }
        }

        onCommandReceived(event);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getHelpDescription() {
        return helpDescription;
    }

    public ArrayList<String> getUsages() {
        return usages;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRequiredPermission() {
        return requiredPermission;
    }

    public void setRequiredPermission(int requiredPermission) {
        this.requiredPermission = requiredPermission;
    }

    public Command[] getTotalCommands() {
        return totalCommands;
    }

    public void setTotalCommands(Command[] totalCommands) {
        this.totalCommands = totalCommands;
    }
}
