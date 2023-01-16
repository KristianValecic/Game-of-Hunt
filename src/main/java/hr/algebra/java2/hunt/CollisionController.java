package hr.algebra.java2.hunt;

import hr.algebra.java2.model.Coordinate;
import hr.algebra.java2.model.Game;
import hr.algebra.java2.model.HunterPlayer;
import hr.algebra.java2.model.Player;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class CollisionController {
    private final Pane mainMap;
    public boolean collision;

    public boolean isCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    MovementController movementController;

    public void setMovementController(MovementController movementController) {
        this.movementController = movementController;
    }

    public CollisionController(Pane gameMapPane) {
        this.mainMap = gameMapPane;
    }

    public boolean checkCollisionWithMap(Player player, ImageView playerSprite, String keyPressed) {
        //1. get sprite position in world
        Bounds playerMapPosition = playerSprite.getBoundsInParent();
        Bounds mapBounds = mainMap.getLayoutBounds();

        //2. compare with map bounds
        switch (keyPressed) {
            case "up":
                if (playerMapPosition.intersects(0.0, 0.0, mapBounds.getWidth(), 0.0)) {
                    System.out.println("Top map Bounds");
                    Game.addMove(player, player.getPlayerName()  + " hit map bounds", new Coordinate(playerSprite.getLayoutX(), playerSprite.getLayoutY()));
                    return true;
                } else {
                    return false;
                }

            case "down":
                if (playerMapPosition.intersects(0.0, mapBounds.getHeight(), mapBounds.getWidth(), mapBounds.getHeight())) {
                    System.out.println("Bottom map Bound");
                    Game.addMove(player, player.getPlayerName()  + " hit map bounds", new Coordinate(playerSprite.getLayoutX(), playerSprite.getLayoutY()));
                    return true;
                } else {
                    return false;
                }

            case "left":
                if (playerMapPosition.intersects(-1.0, 0.0, mapBounds.getMinX(), mapBounds.getMaxY())) {
                    System.out.println("left map Bounds");
                    Game.addMove(player, player.getPlayerName()  + " hit map bounds", new Coordinate(playerSprite.getLayoutX(), playerSprite.getLayoutY()));
                    return true;
                } else {
                    return false;
                }

            case "right":
                if (playerMapPosition.intersects(mapBounds.getMaxX(), mapBounds.getMinY(), mapBounds.getWidth(), mapBounds.getHeight())) {
                    System.out.println("right map Bounds");
                    Game.addMove(player, player.getPlayerName()  + " hit map bounds", new Coordinate(playerSprite.getLayoutX(), playerSprite.getLayoutY()));
                    return true;
                } else {
                    return false;
                }
        }
        return false;
    }

    public boolean checkCollisionWithObject(ImageView playerSprite, Node mapObject, String keyPressed) {
        //1. get sprite position in world
        Bounds playerMapPosition = playerSprite.getBoundsInParent();
        Bounds objectBounds = mapObject.getBoundsInParent();

        //2. compare with map bounds
        switch (keyPressed) {
            case "up":
                if (objectBounds.intersects(playerMapPosition.getMinX()+5, playerMapPosition.getMinY(), playerMapPosition.getWidth()-9, 0.0))
                {
                    System.out.println("top object hit");
                    return true;
                } else {
                    return false;
                }

            case "down":
                if (objectBounds.intersects(playerMapPosition.getMinX()+5, playerMapPosition.getMaxY(), playerMapPosition.getWidth()-9, 0.0)) {
                    System.out.println("Down object hit");
                    return true;
                } else {
                    return false;
                }

            case "left":
                if (objectBounds.intersects(playerMapPosition.getMinX(), playerMapPosition.getMinY()+5, 0.0, playerMapPosition.getHeight()-9)) {
                    System.out.println("left object hit");
                    return true;
                } else {
                    return false;
                }

            case "right":
                if (objectBounds.intersects(playerMapPosition.getMaxX(), playerMapPosition.getMinY()+5, 0.0, playerMapPosition.getHeight()-9)) {
                    System.out.println("right object hit");
                    return true;
                } else {
                    return false;
                }
        }
        return false;
    }


}
