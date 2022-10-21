package hr.algebra.java2.dal;

import hr.algebra.java2.model.Player;
import hr.algebra.java2.model.PlayerRole;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;

public class PlayerState extends Player implements Serializable {

    private double playerPositionMinX;
    private double playerPositionMinY;
    private double playerPositionMaxX;
    private double playerPositionMaxY;
    private String playerName;
    private PlayerRole playerRole;


    //public PlayerState() {}

    public PlayerState(PlayerRole playerRole, String playerName, Image playerImage) {
        super(playerRole, playerImage);
        this.playerName = playerName;
        this.playerRole = playerRole;
    }

    public PlayerState(PlayerRole playerRole, String playerName, double playerPositionMinX, double playerPositionMinY, double playerPositionMaxX, double playerPositionMaxY) {
        this.playerRole = playerRole;
        this.playerName = playerName;
        this.playerPositionMinX = playerPositionMinX;
        this.playerPositionMinY = playerPositionMinY;
        this.playerPositionMaxX = playerPositionMaxX;
        this.playerPositionMaxY = playerPositionMaxY;
    }

    public void setPlayerPosOnMap(Bounds position) {
        playerPositionMinX = position.getMinX();
        playerPositionMinY = position.getMinY();
        playerPositionMaxX = position.getMaxX();
        playerPositionMaxY = position.getMaxY();
    }
    public double getminX() {
        return playerPositionMinX;
    }
    public double getminY() {
        return playerPositionMinY;
    }

    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public PlayerRole getPlayerRole() {
        return playerRole;
    }
}
