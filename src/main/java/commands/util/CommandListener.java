package commands.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import util.JSONLoader;
import util.PropLoader;
import util.RoleHelper;
import util.Settings;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;

public class CommandListener extends ListenerAdapter
{
    public CommandListener()
    {
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if(event.getAuthor().isBot())
            return;

        String[] commandEventMessage;

        String message = event.getMessage().getContentDisplay();
    }

    @Override
    public void onReady(ReadyEvent event)
    {
        System.out.println("Initializing Settings");

        JDA jda = event.getJDA();
        jda.getGuilds().forEach(guild -> {

            if(!JSONLoader.doesSettingExist(guild.getId()))
                JSONLoader.createGuildJSON(guild.getId());

            Settings settings = JSONLoader.getGuildSettings(guild.getId());
            List<RoleHelper> importantRoles = new ArrayList<>();

            if(settings.isInit()) {
                guild.getRoles().forEach(role -> {
                    if (role.hasPermission(Permission.ADMINISTRATOR)) {
                        RoleHelper roleHelper = new RoleHelper(role.getId(), 1000);
                        importantRoles.add(roleHelper);
                    }
                });
            }
            settings = new Settings(guild.getId(), importantRoles);
    });
    }
}
