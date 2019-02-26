package commands.util;

import java.util.Arrays;

public abstract class Command
{
    protected String name = "";
    protected String description = "No data available.";
    protected int requiredPermission = 1000;
    protected Command[] totalCommands = new Command[0];

    public abstract void onCommandRecieved(CommandEvent event);

    public final void execute(CommandEvent event)
    {
        if(event.getMessage().length > 0)
        {
            String[] message = event.getMessage();

            for(Command commands : totalCommands){

                event.setMessage(message.length > 1 ? Arrays.copyOfRange(message, 1, message.length) : new String[0]);
                commands.execute(event);
                return;
            }
        }
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
