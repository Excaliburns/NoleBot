package commands.inhouse;

import java.util.ArrayList;
import java.util.List;

public class Inhouse {
    private String inhouseName;
    private int requiredPlayers;
    private int playerCount = 0;
    private List<String> userList = new ArrayList<>();

    public Inhouse() {
        this.inhouseName = null;
        this.userList = null;
    }

    public Inhouse(String inhouseName, int requiredPlayers) {
        this.inhouseName = inhouseName;
        this.requiredPlayers = requiredPlayers;
    }

    public String getInhouseName() {
        return inhouseName;
    }

    public void setInhouseName(String inhouseName) {
        this.inhouseName = inhouseName;
    }

    public int getRequiredPlayers() {
        return requiredPlayers;
    }

    public void setRequiredPlayers(int requiredPlayers) {
        this.requiredPlayers = requiredPlayers;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }

    public void addPlayer(String userID) { userList.add(userID); playerCount++; }
}
