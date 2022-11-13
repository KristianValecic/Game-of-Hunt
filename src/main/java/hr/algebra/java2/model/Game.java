package hr.algebra.java2.model;

import hr.algebra.java2.dal.GameState;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Game {
    public static final String TRAP_PATH = "file:src/main/resources/hr/algebra/java2/hunt/images/Trap.png";
    public static final String GAME_MAP_IMAGE_PATH = "file:src/main/resources/hr/algebra/java2/hunt/images/GameMap.png";
    public static final String HUNTER_IMAGE_PATH = "file:src/main/resources/hr/algebra/java2/hunt/images/hunterSprite.png";
    public static final String SURVIVOR_IMAGE_PATH = "file:src/main/resources/hr/algebra/java2/hunt/images/survivorSprite.png";
    private static List<Player> playersList = new ArrayList<>();
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 5;
    public static final int MAX_MATCHES = 7;
    public static final int MIN_MATCHES = 1;
    public static final int MIN_TRAPS = 5;
    public static final double spawnMargin = 20;
    private static int trapCounter = 0;
    public static boolean spawnFlagLeftTop = true;
    public static boolean spawnFlagLeftBottom = true;
    public static boolean spawnFlagRightTop = true;
    public static boolean spawnFlagRightBottom = true;
    private static Coordinate playerSpawnLeftTop;
    private static Coordinate playerSpawnLeftBottom;
    private static Coordinate playerSpawnRightBottom;
    private static Coordinate playerSpawnRightTop;
    public static final String SER_FILE = "saveGame.ser";
    private static List<Player> alivePlayersList = new ArrayList<>();
    private static List<Move> moves = new ArrayList<>();
    private static int allMatchesCount;
    private static int matchCounter = 1;
    private static boolean gameOver = false;
    private static String WindowTitle = "Game of Hunt";
    private static HunterPlayer hunterPlayer;
    public static GameTimer gameTimer = GameTimer.getInstance();


    public static int getAllMatchesCount() {
        return allMatchesCount;
    }
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

  //  public static String getHunterImagePath() {
    //    return hunterImagePath;
   // }

   // public static String getSurvivorImagePath() {
   //     return survivorImagePath;
   // }
   //   public static String getGameMapImagePath() {
//        return gameMapImagePath;
//    }
    public static void setInitialPlayersLists(List<Player> gamePlayersList) {
        playersList.clear();
        for (Player player:gamePlayersList) {
            playersList.add(player);
            if (HunterPlayer.class.equals(player.getClass())){
                hunterPlayer = (HunterPlayer) player;
            }
        }
        alivePlayersList.clear();
        alivePlayersList.addAll(gamePlayersList);
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

    private static void loadPlayersList(List<Player> playersList) {
        Game.playersList.clear();
        for (Player player:playersList) {
            Game.playersList.add(player);
            if (HunterPlayer.class.equals(player.getClass())){
                hunterPlayer = (HunterPlayer) player;
            }
        }
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


    public static void newMatch() {
        ///matchCounter++;
        trapCounter = 0;
        getPlayersList().forEach(p -> {
            if (HunterPlayer.class.equals(p.getClass())   /*PlayerRole.Hunter.equals(p.getPlayerRole())*/) {
                ((HunterPlayer)p).setVictimPlayerSprite(null);
            }
        });
    }


    public static void pauseKilling() {
        hunterPlayer.deactivateKillOption();
    }

    public static void resumeKilling() {
        hunterPlayer.activateKillOption();
    }

    public static void loadGame(GameState gameState) {
        alivePlayersList.clear();
        loadPlayersList(gameState.getPlayersList());
        setAllMatchesCount(gameState.getMatchAllCount());
        setCurrentMatch(gameState.getMatchState());
        loadTrapCounter(gameState.getTrapCount());
        gameTimer = gameState.getGameTimer();
    }

    private static void loadTrapCounter(int trapCount) {
        Game.trapCounter = trapCount;
    }

    public static void setAllMatchesCount(int matches) {
        allMatchesCount = matches;
    }

    public static Player getHunterPlayer() {
        return hunterPlayer;
    }
    public static void trapSet() {
        trapCounter++;
    }

    public static int getTrapCount() {
        return trapCounter;
    }
}
