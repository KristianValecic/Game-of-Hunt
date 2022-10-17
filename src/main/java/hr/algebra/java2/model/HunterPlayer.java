package hr.algebra.java2.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class HunterPlayer extends Player{

    private int scoreOfKilledPlayers = 0;
    private List<Player> lsitOfVictims = new ArrayList<>();
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
    public void killedPlayer(Player player){
        lsitOfVictims.add(player);
        scoreOfKilledPlayers++;
    }
}
