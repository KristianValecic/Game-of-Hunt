package hr.algebra.java2.hunt;

import javafx.geometry.Bounds;
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

    public boolean checkCollision(ImageView playerSprite){
        Bounds playerSpriteHitBox = playerSprite.getBoundsInLocal();

        //1. get sprite position in world
        Bounds playerMapPosition = playerSprite.getBoundsInParent();
        Bounds mapBounds = mainMap.getLayoutBounds();

//        double playerShoulderLeft = playerMapPosition.getMinY();
//        double playerShoulderRight = playerMapPosition.getMinY();
//        double playerLegLeft = playerMapPosition.getMinY();
//        double playerLegRight = playerMapPosition.getMinY();

        //2. compare with map bounds
        String s = movementController.getDirection();
        switch (s){
            case "up": //  provjerava je li je dotakao vrh mape
                if (playerMapPosition.intersects(0.0, 0.0, mapBounds.getWidth(), 0.0)
                        /*playerMapPosition.getMinY() < 0.0 || playerMapPosition.getMinY()+playerMapPosition.getWidth() < 0.0*/){
                    System.out.println("Hit map Bounds");
                    return  true;
                }
                else {
                    return false;
                }
            case "down": //  provjerava je li je dotakao dno mape
                if (playerMapPosition.getMaxY() > mapBounds.getMaxY() || playerMapPosition.getMaxY()-playerMapPosition.getWidth() > mapBounds.getMaxY()){
                    System.out.println("Down Bound");
                    return  true;
                }
                else {
                    return false;
                }
            case "left": // treba provjeriti gornju i donju lijevu stranu igraca akoi prolazi lijevo | provjerava je li je dotakao lijevi kraj mape
                if (playerMapPosition.intersects(0.0, 0.0, mapBounds.getMaxX(), mapBounds.getMinY())
                    /*playerMapPosition.getMinX() < 0.0 || playerMapPosition.getMinX()+playerMapPosition.getHeight() < 0.0*/){
                    System.out.println("left Bounds");
                    return  true;
                }
                else {
                    return false;
                }
            case "right": // obrunto od lijevog | provjerava je li je dotakao desni kraj mape
                if (playerMapPosition.getMaxX() > mapBounds.getWidth() || playerMapPosition.getMaxX()-playerMapPosition.getHeight() > mapBounds.getMaxX()){
                    System.out.println("Hit map Bounds");
                    return  true;
                }
                else {
                    return false;
                }
        }
        return false;
    }
}
