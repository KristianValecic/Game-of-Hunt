package hr.algebra.java2.model;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static List<Player> playersList = new ArrayList<>();
    private static List<Player> alivePlayersList = new ArrayList<>();
    private static int matchesCount;

    private static int matchCounter = 1;

    private Game() {
    }

    public static List<Player> getPlayersList() {
        return playersList;
    }

    public static int getMatchesCount() {
        return matchesCount;
    }

    public static List<Player> getAlivePlayersList() {
        return alivePlayersList;
    }

    public static void setPlayersList(List<Player> gamePlayersList) {
        playersList = gamePlayersList;
        alivePlayersList = (gamePlayersList);
    }

    public static void setMatchesCount(int gameMatchesCount) {
        matchesCount = gameMatchesCount;
    }

    public static void matchEnd() {
        matchCounter++;
    }


}
