package hr.algebra.java2.model;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Game {
    private static List<Player> playersList = new ArrayList<>();
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 5;
    public static final int MAX_MATCHES = 7;
    public static final int MIN_MATCHES = 1;
    public static final double spawnMargin = 20;
    public static boolean spawnFlagLeftTop = true;
    public static boolean spawnFlagLeftBottom = true;
    public static boolean spawnFlagRightTop = true;
    public static boolean spawnFlagRightBottom = true;
    private static Coordinate playerSpawnLeftTop;
    private static Coordinate playerSpawnLeftBottom;
    private static Coordinate playerSpawnRightBottom;
    private static Coordinate playerSpawnRightTop;
    private static final String gameMapImagePath = "file:src/main/resources/hr/algebra/java2/hunt/images/GameMap.png";
    private static final String hunterImagePath = "file:src/main/resources/hr/algebra/java2/hunt/images/hunterSprite.png";
    private static final String survivorImagePath = "file:src/main/resources/hr/algebra/java2/hunt/images/survivorSprite.png";
    public static final String SER_FILE = "saveGame.ser";
    private static List<Player> alivePlayersList = new ArrayList<>();
    private static List<Move> moves = new ArrayList<>();
    private static int allMatchesCount;
    private static int matchCounter = 1;
    private static boolean gameOver = false;
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

    public static void playerKilled(SurvivorPlayer player) {
        player.setDead(true);
        alivePlayersList.remove(player);
        alivePlayersList.forEach(p -> {
            if (p.getClass().equals(HunterPlayer.class)) {
                ((HunterPlayer) p).killedPlayer(player);
            }
        });
        if (alivePlayersList.stream().count() == 1 /*alivePlayersList.isEmpty()*/) {
            gameEnd(HunterPlayer.class.toString());
        }
    }

    public static void matchEndByTimerRunout() {
        if (matchCounter >= allMatchesCount) {
            gameOver = true;
            gameEnd(SurvivorPlayer.class.toString());
        }
        matchEnd();
        alivePlayersList.forEach(p -> {
            if (p.getClass().equals(SurvivorPlayer.class)) {
                ((SurvivorPlayer) p).SurvivedMatch();
                addMove(p, "Sruvived");

            }
        });
    }

    private static void gameEnd(String winnerPlayerClass) {
        gameOver = true;
        System.out.println(winnerPlayerClass.getClass().toString());
        playersList.forEach(p -> {
            if (p.getClass().toString().equals(winnerPlayerClass)) {
                p.gameWon();
            }
        });
    }

    private static void setNewGame() {
        gameOver = false;
        System.out.println("New game started");
    }

    public static boolean isGameOver() {
        return gameOver;
    }

    public static void addMove(Player player, String move) {
        moves.add(new Move(player, move));
    }

    public static List<Move> getMoves() {
        return moves;
    }

    public static String getWindowTitle() {
        return WindowTitle;
    }

    public static void Rematch() {
        gameOver = false;
        matchCounter = 1;
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

    public static void setSpawnPointsOnMap(double width, double height) {
        playerSpawnLeftTop = new Coordinate(spawnMargin, spawnMargin);
        playerSpawnLeftBottom = new Coordinate(spawnMargin, height - 70);
        playerSpawnRightBottom = new Coordinate(width - Player.getPlayerWidth() - spawnMargin, height - 70 - spawnMargin);
        playerSpawnRightTop = new Coordinate(width - Player.getPlayerWidth() - spawnMargin, spawnMargin);
    }

    public static Coordinate getRandomSawnPoint() {
        Random r = new Random();
        Coordinate spawn = new Coordinate(0, 0);
        int result;
        boolean isSet = false;
        while (!isSet) {
            result = r.nextInt(4);
            if (result == 0 && spawnFlagLeftTop) {
                spawnFlagLeftTop = false;
                isSet = true;
                spawn = playerSpawnLeftTop;
            } else if (result == 1 && spawnFlagLeftBottom) {
                spawnFlagLeftBottom = false;
                isSet = true;
                spawn = playerSpawnLeftBottom;
            } else if (result == 2 && spawnFlagRightTop) {
                spawnFlagRightTop = false;
                isSet = true;
                spawn = playerSpawnRightTop;
            } else if (result == 3 && spawnFlagRightBottom) {
                spawnFlagRightBottom = false;
                isSet = true;
                spawn = playerSpawnRightBottom;
            }
        }

        if (!spawnFlagRightBottom && !spawnFlagRightTop && !spawnFlagLeftTop && !spawnFlagLeftBottom){
            spawnFlagRightBottom = true;
            spawnFlagRightTop = true;
            spawnFlagLeftTop = true;
            spawnFlagLeftBottom = true;
        }
        return spawn;
    }

    public static String getGameMapImagePath() {
        return gameMapImagePath;
    }

    public static void newMatch() {
        ///matchCounter++;
        getPlayersList().forEach(p -> {
            if (HunterPlayer.class.equals(p.getClass())   /*PlayerRole.Hunter.equals(p.getPlayerRole())*/) {
                ((HunterPlayer)p).setVictimPlayerSprite(null);
            }
        });
    }


}
