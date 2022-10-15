package hr.algebra.java2.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player {
    private String playerName;
    private PlayerRole playerRole;
    private Image playerImage;

    private ImageView playerSprite = new ImageView();

    public ImageView getPlayerSprite() {
        return playerSprite;
    }

    public PlayerRole getPlayerRole() {
        return playerRole;
    }

    public Player(PlayerRole playerRole, Image playerImage) {
        this.playerRole = playerRole;
        playerSprite.setImage(playerImage);
    }

    public void setName(String name) {
        this.playerName = name;
    }
}
