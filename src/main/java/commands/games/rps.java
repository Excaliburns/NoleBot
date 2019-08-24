package commands.games;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.entities.MessageChannel;

public class rps extends Command {

    public rps() {
        name = "rps";
        description = "Rock Paper Scissors!";
        helpDescription = "Play Rock Paper Scissors with the bot!";
        requiredPermission = 0;
        usages.add("rps rock");
        usages.add("rps paper");
        usages.add("rps scissors");
    }

    @Override
    public void onCommandReceived(CommandEvent event) {
        String[] eventMessage = event.getMessage().clone();
        MessageChannel messageChannel = event.getChannel();
        int randPick = (int) (Math.random() * 3);
        String userSelection = "";
        String botSelection;

        System.out.println(randPick);

        switch(randPick){
            case 0:
                botSelection = "rock";
                break;
            case 1:
                botSelection = "paper";
                break;
            case 2:
                botSelection = "scissors";
                break;
            default:
                botSelection = "";
                break;
        }

        if (eventMessage[1] == null) {
            messageChannel.sendMessage("You did not pick rock, paper, or scissors.").queue();
            return;
        }
        else {
            userSelection = eventMessage[1].toLowerCase();

            if (!(userSelection.equals("paper") | userSelection.equals("scissors") | userSelection.equals("rock"))) {
                messageChannel.sendMessage("You must pick rock, paper, or scissors. Please try again.").queue();
                return;
            }
        }

        if (botSelection.equals(userSelection)) {
            messageChannel.sendMessage("It's a tie! I also picked " + userSelection + ".").queue();
        }
        else if(botSelection.equals("rock")) {
            if (userSelection.equals("paper"))
                messageChannel.sendMessage("You win... I chose rock.").queue();
            else
                messageChannel.sendMessage("I win! I chose rock.").queue();
        }
        else if(botSelection.equals("paper")){
            if (userSelection.equals("scissors"))
                messageChannel.sendMessage("You win... I chose paper.").queue();
            else
                messageChannel.sendMessage("I win! I chose paper.").queue();
        }
        else if(botSelection.equals("scissors")){
            if(userSelection.equals("rock"))
                messageChannel.sendMessage("You win... I chose scissors.").queue();
            else
                messageChannel.sendMessage("I win! I chose scissors.").queue();
        }


    }
}
