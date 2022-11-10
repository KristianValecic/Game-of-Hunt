package hr.algebra.java2.model;

import javafx.beans.property.StringProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    private String playerName;
    private PlayerRole playerRole;
    public static final double playerWidth = 40;
    public static final double playerHeight = 70;
    private static double marginLightSource = 30;
    private double playerSpeed = 180;
    private double playerHunterSpeed = 250;
    private List<String> moves = new ArrayList<>();
    private Integer gamesWon = 0;
    private transient ImageView playerSprite = new ImageView();
    private transient ImageView lightSource = new ImageView();

    public Player() {}

    public Player(PlayerRole playerRole, Image playerImage) {
        this.playerRole = playerRole;
        this.playerSprite.setImage(playerImage);
    }

    public Player(String playerName, PlayerRole playerRole, Image playerImage) {
        this.playerRole = playerRole;
        setPlayerSprite(playerImage);
        setPlayerName(playerName);
    }

    public void gameWon() {
        gamesWon++;
    }
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
    public void addMove(String moveDesc) {
        moves.add(moveDesc);
    }
    public List<String> getMoves() {
        return moves;
    }
    public void setPlayerSprite(Image image) {
        playerSprite = new ImageView();
        this.playerSprite.setImage(image);
        this.playerSprite.setFitWidth(playerWidth);
        //this.playerSprite.setFitHeight(Game.getPlayerHeight());
        this.playerSprite.setPreserveRatio(true);
        this.playerSprite.setSmooth(true);
        //this.imgPath = image.getUrl();
    }

    public ImageView getLightSource() {
        return lightSource;
    }
    public double getPlayerSpeed() {
        if (PlayerRole.Hunter.equals(playerRole)){
            return playerHunterSpeed;
        }
        return playerSpeed;
    }

    public void setLightSource(ImageView playerLightSource) {
        lightSource = playerLightSource;
    }
    public static double getMarginLightSource() {
        return marginLightSource;
    }
    public static double getPlayerWidth() {
        return playerWidth;
    }
    public static double getPlayerHeight() {
        return playerHeight;
    }

    public static double getLightSourceWidth() {
        return playerWidth + (marginLightSource*2);
    }
    public static double getLightSourceHeight() {
        return playerHeight + (marginLightSource*2);
    }

    public ImageView setPlayerLightSourceInitial(Coordinate spawnPoint) {
        //kriran background image za lightsource
        Image playerImg = getPlayerSprite().getImage();
        ImageView lightSource = new ImageView(Game.getGameMapImagePath());
        double spawnX = spawnPoint.getX()-Player.getMarginLightSource();
        double spawnY = spawnPoint.getY()-Player.getMarginLightSource();

        //postavke image-a
        lightSource.setSmooth(true);
        lightSource.setPreserveRatio(true);

        //set viewport
        lightSource.setViewport(
                new Rectangle2D
                        (spawnX,
                                spawnY,
                                Player.getLightSourceWidth(),// *2 has to compensate for the subtraction of margin
                                Player.getLightSourceHeight())
        );
        lightSource.relocate(spawnX, spawnY);
        return lightSource;
    }

    public void loadPlayerSprite(Coordinate position) {
        if (getPlayerRole().equals(PlayerRole.Hunter)) {
            setPlayerSprite(new Image(Game.getHunterImagePath()));
        } else {
            setPlayerSprite(new Image(Game.getSurvivorImagePath()));
        }
        getPlayerSprite().relocate(position.getX(), position.getY());

    }

    public void loadPlayerLightSource(Coordinate position) {
        lightSource = new ImageView(Game.getGameMapImagePath());
        lightSource.setSmooth(true);
        lightSource.setPreserveRatio(true);

        //set viewport
        lightSource.setViewport(
                new Rectangle2D
                        (position.getX(),
                                position.getY(),
                                Player.getLightSourceWidth(),// *2 has to compensate for the subtraction of margin
                                Player.getLightSourceHeight())
        );
        lightSource.relocate(position.getX(), position.getY());
    }
}
