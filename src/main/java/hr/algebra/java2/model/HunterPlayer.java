package hr.algebra.java2.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HunterPlayer extends Player{

    public HunterPlayer(PlayerRole playerRole, Image playerImage) {
        super(playerRole, playerImage);
    }
    private ImageView victimPlayerSprite;

    public ImageView getVictimPlayerSprite() {
        return victimPlayerSprite;
    }

    public void setVictimPlayer(ImageView victimPlayerSprite) {
        this.victimPlayerSprite = victimPlayerSprite;
    }
}
