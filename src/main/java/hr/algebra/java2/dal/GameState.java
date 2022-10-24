package hr.algebra.java2.dal;

import hr.algebra.java2.model.Coordinate;
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
    private String timerState;
    private int match;
    private int scoreState;
    private List<Player> playersList = new ArrayList<>();
    private Map<Player, Coordinate> alivePlayersPositionsList = new HashMap<>();

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

    public void setTimerState(String currentTime) {
        timerState = currentTime;
    }

    public Map<Player, Coordinate> getAlivePlayersList() {
        return alivePlayersPositionsList;
    }
}
