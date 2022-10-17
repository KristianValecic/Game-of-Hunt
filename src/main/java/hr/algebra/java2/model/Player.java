package hr.algebra.java2.model;

import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player {
    private String playerName;
    private PlayerRole playerRole;

    private int gamesWon = 0;

    private ImageView playerSprite = new ImageView();

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

    public Player(PlayerRole playerRole, Image playerImage) {
        this.playerRole = playerRole;
        playerSprite.setImage(playerImage);
    }

    public Player(PlayerRole playerRole, String playerName) {
        this.playerRole = playerRole;
        setPlayerName(playerName);
    }
}
