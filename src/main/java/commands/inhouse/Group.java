package commands.inhouse;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String groupName;
    private int requiredPlayers;
    private int playerCount = 0;
    private int duration = 3;
    private List<String> userList = new ArrayList<>();


    Group(String inhouseName, int requiredPlayers) {
        this.groupName = inhouseName;
        this.requiredPlayers = requiredPlayers;
    }

    String getInhouseName() {
        return groupName;
    }

    int getRequiredPlayers() {
        return requiredPlayers;
    }

    int getPlayerCount() {
        return playerCount;
    }

    void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    List<String> getUserList() {
        return userList;
    }

    void addPlayer(String userID) { userList.add(userID); playerCount++; }

    int getDuration() {
        return duration;
    }

    void setDuration(int duration) {
        this.duration = duration;
    }
}
