package commands.admin.permissions;

import commands.util.CommandEvent;
import util.JSONLoader;
import util.RoleHelper;
import util.Settings;

import java.util.Comparator;
import java.util.List;

class PermissionUtils {
    void saveData(List<RoleHelper> roleHelperList, Settings settings, CommandEvent event) {
        settings.setRoleHelper(roleHelperList);
        settings.getRoleHelper().sort(Comparator.comparing(RoleHelper::getPermID).reversed());

        event.getCommandListener().getSettingsHashMap().put(event.getGuildID(), settings);
        JSONLoader.saveGuildSettings(settings);
    }
}
