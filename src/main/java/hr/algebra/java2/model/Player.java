package hr.algebra.java2.model;

import javafx.scene.image.Image;

public class Player {
    private String playerName;
    private PlayerRole playerRole;

    private Image playerImage;

    public Image getPlayerImage() {
        return playerImage;
    }

    public Player(PlayerRole playerRole, Image playerImage) {
        this.playerRole = playerRole;
        this.playerImage = playerImage;
    }

    public void setName(String name) {
        this.playerName = name;
    }
}
