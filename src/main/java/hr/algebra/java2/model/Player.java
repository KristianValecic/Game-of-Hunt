package hr.algebra.java2.model;

import javafx.beans.property.StringProperty;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    private double playerPositionMinX;
    private double playerPositionMinY;
    private double playerPositionMaxX;
    private double playerPositionMaxY;
    private String playerName;
    private PlayerRole playerRole;
    private String imgPath;
    private List<String> moves = new ArrayList<>();
    private Integer gamesWon = 0;
    private transient ImageView playerSprite = new ImageView();

    public Player() {}

    public Player(PlayerRole playerRole, Image playerImage) {
        this.playerRole = playerRole;
        this.playerSprite.setImage(playerImage);
    }

//    public Player(PlayerRole playerRole, String playerName) {
//        this.playerRole = playerRole;
//        setPlayerName(playerName);
//    }
    public Player(String playerName, PlayerRole playerRole, Image playerImage) {
        this.playerRole = playerRole;
        setPlayerSprite(playerImage);
        setPlayerName(playerName);
    }
    public void gameWon() {
        gamesWon++;
    }
    public ImageView getPlayerSprite() {
        return playerSprite;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public String getPlayerName() {
        return  this.playerName;
    }
    public PlayerRole getPlayerRole() {
        return  this.playerRole;
    }
    public void setPlayerRole(PlayerRole playerRole) {
        this.playerRole = playerRole;
    }
    public int getGamesWon() {
        return this.gamesWon;
    }
    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }
    public void addMove(String moveDesc) {
        moves.add(moveDesc);
    }
    public List<String> getMoves() {
        return moves;
    }
    public void setPlayerSprite(Image image) {
        playerSprite = new ImageView();
        this.playerSprite.setImage(image);
        this.playerSprite.setFitWidth(Game.getPlayerWidth());
        //this.playerSprite.setFitHeight(Game.getPlayerHeight());
        this.playerSprite.setPreserveRatio(true);
        this.playerSprite.setSmooth(true);
        //this.imgPath = image.getUrl();
    }


    public void setPlayerPosition(Bounds playerPositionInParent) {
        this.playerPositionMinX = playerPositionInParent.getMinX();
        this.playerPositionMinY = playerPositionInParent.getMinY();
        this.playerPositionMaxX = playerPositionInParent.getMaxX();
        this.playerPositionMaxY = playerPositionInParent.getMaxY();
    }
}
