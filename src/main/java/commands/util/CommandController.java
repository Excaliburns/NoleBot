package commands.util;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandController extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        JDA jda = event.getJDA();

        jda.getGuilds().forEach(guild -> {

        });
    }
}
