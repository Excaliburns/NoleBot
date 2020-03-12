package commands.general;

import commands.util.Command;
import commands.util.CommandEvent;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.restaction.pagination.MessagePaginationAction;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Counter extends Command {
    static int totalValue;

    public Counter(){
        name = "counter";
        requiredPermission = 1000;
        usages.add("counter");
    }

    @Override
    public void onCommandReceived(CommandEvent event) {
        MessageChannel channel = event.getChannel();
        MessagePaginationAction messageHistory = channel.getIterableHistory();
        Map<User, Integer> userMessages = new HashMap<>();

        messageHistory.iterator().forEachRemaining(e -> {
            User author = e.getAuthor();
            if(userMessages.getOrDefault(author, -1) == -1){
                userMessages.put(author, 0);
            }
            int iterator = userMessages.get(author);
            iterator++;

            userMessages.put(author, iterator);
        });

        final Map<User, Integer> sortedMap = userMessages.entrySet().stream().sorted(Map.Entry.<User, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        channel.sendMessage("Congrats everyone! Here are some of the stats for our wonderful count to 10000 channel.").queue();
        MessageBuilder builder = new MessageBuilder();

        int otherCounter = 0;
        for(Map.Entry<User, Integer> entry : sortedMap.entrySet()){
            totalValue += entry.getValue();

            if(entry.getValue() < totalValue * .03){
                otherCounter += entry.getValue();
                continue;
            }
            if(builder.length() > 1600){
                channel.sendMessage(builder.build()).queue();
                builder.clear();
            }

            builder.append("User: " + entry.getKey().getName() + " has sent: " + entry.getValue() + " messages.\n");
        }

        channel.sendMessage(builder.build()).queue();
        channel.sendMessage("Everyone else has sent a total of: " + otherCounter + " messages.").queue();

        DefaultPieDataset dataset = createDataset(userMessages);
        JFreeChart chart = ChartFactory.createPieChart(
                "User Message %",
                dataset,
                true,
                true,
                false
        );
        PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator(
                "{0}: ({2})", new DecimalFormat("0"), new DecimalFormat("0%")
        );
        ((PiePlot) chart.getPlot()).setLabelGenerator(labelGenerator);

        File output = new File("outputGraph.png");
        if(output.exists())
            output.delete();

        try {
            ImageIO.write(chart.createBufferedImage(1280, 960), "png", output);
            channel.sendFile(output).queue();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static DefaultPieDataset createDataset(Map<User, Integer> userMap) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        int otherIterator = 0;

        for(Map.Entry<User, Integer> entry: userMap.entrySet()){
            if(entry.getValue() < totalValue * .03){
                otherIterator += entry.getValue();
                continue;
            }
            dataset.setValue(entry.getKey().getName(), entry.getValue());
        }

        dataset.setValue("Other", otherIterator);

        return dataset;
    }
}
