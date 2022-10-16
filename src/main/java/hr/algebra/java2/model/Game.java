package hr.algebra.java2.model;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static List<Player> playersList = new ArrayList<>(); //mozda static
    private static int matchesCount;

    private Game() {}

    public static List<Player> getPlayersList() {
        return playersList;
    }

    public static int getMatchesCount() {
        return matchesCount;
    }

    public static void setPlayersList(List<Player> gamePlayersList) {
        playersList = gamePlayersList;
    }

    public static void setMatchesCount(int gameMatchesCount) {
       matchesCount = gameMatchesCount;
    }


}
