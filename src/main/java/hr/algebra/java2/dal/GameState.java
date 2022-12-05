package hr.algebra.java2.dal;

import hr.algebra.java2.model.Coordinate;
import hr.algebra.java2.model.GameTimer;
import hr.algebra.java2.model.Player;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameState implements Serializable {
    //private String timerState;
    private int minutesState;
    private int secondsState;
    private int matchAllCount;
    private int match;
    private int scoreState;
    private GameTimer gameTimer;
    private List<Player> playersList = new ArrayList<>();
    private List<Coordinate> trapsPositionsList = new ArrayList<>();
    private Map<Player, Coordinate> alivePlayersPositionsList = new HashMap<>();
    private Map<Player, Coordinate> alivePlayersLightSourcePositionsList = new HashMap<>();
    private int trapCount;
    private int playersAddedCounter;

    public  List<Player> getPlayersList() {
        return playersList;
    }

    public void addPlayer(Player player) {
        this.playersList.add(player);
    }

    public void setPlayersList(List<Player> playersList) {
        this.playersList.addAll(playersList);
    }

    public void setAlivePlayersPositions(ObservableList<Node> children) {
        children.forEach(child -> {
            playersList.forEach(player -> {

                if (player.getPlayerSprite().equals(child)) {
                    alivePlayersPositionsList.put(player,
                            new Coordinate(
                                    child.getBoundsInParent().getMinX(),
                                    child.getBoundsInParent().getMinY())
                    );
                }
                if (player.getLightSource().equals(child)) {
                    alivePlayersLightSourcePositionsList.put(player,
                            new Coordinate(
                                    child.getBoundsInParent().getMinX(),
                                    child.getBoundsInParent().getMinY())
                    );
                }
            });
        });
    }

    public void setMatchState(int currentMatch) {
        match = currentMatch;
    }

    public void setTimerState(GameTimer timer) {
        gameTimer = timer;
        //secondsState = seconds;
        //minutesState = minutes;
    }
    public int getMatchAllCount() {
        return matchAllCount;
    }

    public void setMatchAllCount(int matchAllCount) {
        this.matchAllCount = matchAllCount;
    }
    public Map<Player, Coordinate> getAlivePlayersList() {
        return alivePlayersPositionsList;
    }
    public int getMatchState() {
        return match;
    }
    public int getSecondsState() {
        return secondsState;
    }
    public int getMinutesState() {
        return minutesState;
    }
    public Map<Player, Coordinate> getAlivePlayersLightSourceList() {
        return alivePlayersLightSourcePositionsList;
    }

    public void setTrapCount(int trapCount) {
        this.trapCount = trapCount;
    }
    public int getTrapCount() {
        return trapCount;
    }

    public void addTrapPosition(Coordinate coordinate) {
        trapsPositionsList.add(coordinate);
    }

    public List<Coordinate> getTrapPositions() {
        return trapsPositionsList;
    }

    public GameTimer getGameTimer() {
        return gameTimer;
    }

    public void savePlayerAddedCounter(int playerCounter) {
        playersAddedCounter = playerCounter;
    }
}
