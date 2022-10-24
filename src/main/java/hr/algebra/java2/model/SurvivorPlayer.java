package hr.algebra.java2.model;

import javafx.scene.image.Image;

public class SurvivorPlayer extends Player {
    private int matchesSurvivedCounter = 0;
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
