package hr.algebra.java2.dal;

import hr.algebra.java2.model.Coordinate;
import hr.algebra.java2.model.GameTimer;
import hr.algebra.java2.model.Player;
import javafx.collections.ObservableList;
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
    private int match;
    private int scoreState;
    private List<Player> playersList = new ArrayList<>();
    private Map<Player, Coordinate> alivePlayersPositionsList = new HashMap<>();

    public  List<Player> getPlayersList() {
        return playersList;
    }

    public void addPlayer(Player player) {
        this.playersList.add(player);
    }

    public void setPlayersList(List<Player> playersList) {
        this.playersList.addAll(playersList);
    }

    public void setAlivePlayers(/*List<Player> alivePlayers, */ObservableList<Node> positions) {
        positions.forEach(playerSpritePos -> {
            playersList.forEach(player -> {
                if (player.getPlayerSprite().equals(playerSpritePos)) {
                    Bounds playerSpriteBounds = playerSpritePos.getBoundsInParent();
                    alivePlayersPositionsList.put(player, new Coordinate(playerSpriteBounds.getMinX(), playerSpriteBounds.getMinY()));
                }
            });
        });
    }

    public void setPlayerPosition(Player player, Bounds bounds) {

    }

    public void setMatchState(int currentMatch) {
        match = currentMatch;
    }

    public void setTimerState(int minutes, int seconds) {
        secondsState = seconds;
        minutesState = minutes;
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
}
