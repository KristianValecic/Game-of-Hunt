package hr.algebra.java2.model;

import javafx.util.Pair;

import java.awt.font.LineBreakMeasurer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game {
    private static List<Player> playersList = new ArrayList<>();
    private static List<Player> alivePlayersList = new ArrayList<>();
    //private static HashMap<Player, String> moves = new HashMap<>();
    private static List<Move> moves = new ArrayList<>();
    private static int allMatchesCount;
    //private static int playersCount = playersList.stream().count();
    private static int matchCounter = 1;
    private static boolean gameOver=false;
    private static String WindowTitle = "Game of Hunt";

    public static int getAllMatchesCount() {
        return matchCounter;
    }
    public static int getMatchesCount() {
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

    public static void setPlayersList(List<Player> gamePlayersList) {
        playersList.addAll(gamePlayersList);
        alivePlayersList.addAll(gamePlayersList);
//        playersList.forEach(p ->
//        {
//            if (p.getClass().equals(SruvivorPlayer.class)) {
//                alivePlayersList.add(p);
//            }
//        });
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
            gameEnd(SruvivorPlayer.class.toString());
        }
        matchEnd();
        alivePlayersList.forEach(p -> {
            if (p.getClass().equals(SruvivorPlayer.class)) {
                ((SruvivorPlayer)p).SurvivedMatch();
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
}
