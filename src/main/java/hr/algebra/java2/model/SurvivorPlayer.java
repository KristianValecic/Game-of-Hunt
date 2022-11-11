package hr.algebra.java2.model;

import javafx.scene.image.Image;

import java.io.Serializable;

public class SurvivorPlayer extends Player implements Serializable {
    private int matchesSurvivedCounter = 0;
    private boolean dead = false;

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public SurvivorPlayer(PlayerRole playerRole, Image playerImage) {
        super(playerRole, playerImage);
    }

    public SurvivorPlayer(String playerName, PlayerRole playerRole, Image playerImage) {
        super(playerName, playerRole, playerImage);
    }

    public void SurvivedMatch() {
        matchesSurvivedCounter++;

    }
}
