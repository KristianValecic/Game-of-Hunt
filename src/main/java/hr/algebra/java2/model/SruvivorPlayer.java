package hr.algebra.java2.model;

import javafx.scene.image.Image;

public class SruvivorPlayer extends Player {
    private int matchesSurvivedCounter = 0;
    public SruvivorPlayer(PlayerRole playerRole, Image playerImage) {
        super(playerRole, playerImage);
    }

    public void SurvivedMatch() {
        matchesSurvivedCounter++;
    }
}
