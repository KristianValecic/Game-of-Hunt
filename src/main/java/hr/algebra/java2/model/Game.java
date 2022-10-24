package hr.algebra.java2.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Game {
    private static List<Player> playersList = new ArrayList<>();

    private static final String hunterImagePath = "file:src/main/resources/hr/algebra/java2/hunt/hunterSprite.png";
    private static final String survivorImagePath = "file:src/main/resources/hr/algebra/java2/hunt/survivorSprite.png";
    private static List<Player> alivePlayersList = new ArrayList<>();
    private static List<Move> moves = new ArrayList<>();
    private static int allMatchesCount;
    private static int matchCounter = 1;
    private static boolean gameOver=false;
    private static String WindowTitle = "Game of Hunt";

    //public static int getAllMatchesCount() {
//        return matchCounter;
//    }
    public static int getCurrentMatch() {
        return matchCounter;
    }

    private Game() {
    }

    public static List<Player> getPlayersList() {
        return playersList;
    }
    public static List<Player> getAlivePlayersList() {
        return alivePlayersList;
    }
    public static String getHunterImagePath() {
        return hunterImagePath;
    }
    public static String getSurvivorImagePath() {
        return survivorImagePath;
    }

    public static void setInitialPlayersList(List<Player> gamePlayersList) {
        playersList.addAll(gamePlayersList);
        alivePlayersList.addAll(gamePlayersList);
    }

    public static void setMatchesCount(int gameMatchesCount) {
        allMatchesCount = gameMatchesCount;
    }

    private static void matchEnd() {
        matchCounter++;
    }

    public static void playerKilled(Player player) {
        alivePlayersList.remove(player);
        alivePlayersList.forEach(p -> {
            if (p.getClass().equals(HunterPlayer.class)) {
                ((HunterPlayer)p).killedPlayer(player);
                //alivePlayersList.remove(player);
            }
        });
        if (alivePlayersList.stream().count()==1 /*alivePlayersList.isEmpty()*/){
            gameEnd(HunterPlayer.class.toString());
        }
    }

    public static void matchEndByTimerRunout() {
        if (matchCounter >= allMatchesCount){
            gameOver=true;
            gameEnd(SurvivorPlayer.class.toString());
        }
        matchEnd();
        alivePlayersList.forEach(p -> {
            if (p.getClass().equals(SurvivorPlayer.class)) {
                ((SurvivorPlayer)p).SurvivedMatch();
                addMove(p, "Sruvived");

            }
        });
    }

    private static void gameEnd(String winnerPlayerClass) {
        gameOver=true;
        System.out.println(winnerPlayerClass.getClass().toString());
        playersList.forEach(p -> {
            if (p.getClass().toString().equals(winnerPlayerClass)) {
                p.gameWon();
            }
        });
    }

    private static void setNewGame() {
        gameOver=false;
        System.out.println("New game started");
    }

    public static boolean isGameOver() {
        return gameOver;
    }

    public static void addMove(Player player, String move){
        moves.add(new Move(player, move));
    }
    public static List<Move> getMoves(){
        return moves;
    }

    public static String getWindowTitle() {
        return WindowTitle;
    }

    public static void Rematch() {
        gameOver=false;
        matchCounter=1;
        alivePlayersList.clear();
        alivePlayersList.addAll(playersList);
        System.out.println("Rematch started");
    }

    public static void addAlivePlayer(Player player) {
        alivePlayersList.add(player);
    }

    public static void loadPlayersList(List<Player> playersList) {
        Game.playersList.clear();
        Game.playersList.addAll(playersList);
    }

    public static void setCurrentMatch(int matchState) {
        matchCounter = matchState;
    }
}
