package hr.algebra.java2.model;

import javafx.scene.image.Image;

import java.io.Serializable;

public class SurvivorPlayer extends Player implements Serializable {
    private int matchesSurvivedCounter = 0;
    private String timeOfGotCaughtInTrap;
    private String timeOfReleaseInTrap;
    public static final int TIME_IN_TRAP = 3;
    private boolean dead = false;

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public SurvivorPlayer(PlayerRole playerRole, Image playerImage) {
        super(playerRole, playerImage);
        timeOfReleaseInTrap = null;
    }

    public SurvivorPlayer(String playerName, PlayerRole playerRole, Image playerImage) {
        super(playerName, playerRole, playerImage);
        timeOfReleaseInTrap = null;
    }

    public void SurvivedMatch() {
        matchesSurvivedCounter++;

    }
    public void caughtInTrap(String time) {
        timeOfGotCaughtInTrap = time;// provjeri time of varijable
         int minutes = GameTimer.getMinutesFromTime(timeOfGotCaughtInTrap);
         int seconds = GameTimer.getSecondsFromTime(timeOfGotCaughtInTrap);
        timeOfReleaseInTrap = GameTimer.formatTime(minutes, seconds-TIME_IN_TRAP);
    }

    public boolean isCaughtInTrap(String time) {
        if (timeOfReleaseInTrap != null && GameTimer.aEqualsOrMoreThanB(timeOfReleaseInTrap, time)){
            timeOfReleaseInTrap = null;
            return true;
        }
        return false;
    }
}
