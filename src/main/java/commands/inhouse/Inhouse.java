package commands.inhouse;

import java.util.ArrayList;
import java.util.List;

public class Inhouse {
    private String inhouseName;
    private int requiredPlayers;
    private int playerCount = 0;
    private int duration = 3;
    private List<String> userList = new ArrayList<>();

    public Inhouse() {
        this.inhouseName = null;
        this.userList = null;
    }

    Inhouse(String inhouseName, int requiredPlayers) {
        this.inhouseName = inhouseName;
        this.requiredPlayers = requiredPlayers;
    }

    String getInhouseName() {
        return inhouseName;
    }

    public void setInhouseName(String inhouseName) {
        this.inhouseName = inhouseName;
    }

    int getRequiredPlayers() {
        return requiredPlayers;
    }

    public void setRequiredPlayers(int requiredPlayers) {
        this.requiredPlayers = requiredPlayers;
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

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }

    void addPlayer(String userID) { userList.add(userID); playerCount++; }

    int getDuration() {
        return duration;
    }

    void setDuration(int duration) {
        this.duration = duration;
    }
}
