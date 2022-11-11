package hr.algebra.java2.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HunterPlayer extends Player implements Serializable {
    private int scoreOfKilledPlayers = 0;
    private List<Player> lsitOfVictims = new ArrayList<>();//todo: use dodaj zrtve u listVictims
    private boolean canKill = true;
    private transient ImageView victimPlayerSprite;
    public HunterPlayer(PlayerRole playerRole, Image playerImage) {
        super(playerRole, playerImage);
    }
    public HunterPlayer(String playerName, PlayerRole playerRole, Image playerImage) {
        super(playerName, playerRole, playerImage);
    }
    public ImageView getVictimPlayerSprite() {
        return victimPlayerSprite;
    }
    public void setVictimPlayerSprite(ImageView victimPlayerSprite) {
        this.victimPlayerSprite = victimPlayerSprite;
    }
    public void killedPlayer(Player player){
        //lsitOfVictims.add(player);
        scoreOfKilledPlayers++;
    }

    public boolean canKill() {
        return canKill;
    }
    public void deactivateKillOption() {
        canKill = false;
    }
    public void activateKillOption() {
        canKill = true;
    }
}
