package hr.algebra.java2.hunt;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class MovementController {

    private static final String up = "up";
    private static final String down = "down";
    private static final String left = "left";
    private static final String right = "right";

    private BooleanProperty wPressed = new SimpleBooleanProperty();

    private BooleanProperty aPressed = new SimpleBooleanProperty();
    private BooleanProperty sPressed = new SimpleBooleanProperty();
    private BooleanProperty dPressed = new SimpleBooleanProperty();

    private CollisionController collisionController;

    private Pane gameMapPane;

    private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed);

    private double movementVariable = 1.2;

    @FXML
    private ImageView sprite;

    @FXML
    private Pane scene;

    public MovementController(Pane pane) {
        this.gameMapPane = pane;
        collisionController = new CollisionController(gameMapPane);
    }

    public void makeMovable(ImageView sprite, Pane scene) {
        this.sprite = sprite;
        this.scene = scene;

        movementSetup();

        collisionController.setMovementController(this);

        keyPressed.addListener(((observableValue, aBoolean, t1) -> {
            if (!aBoolean) {
                timer.start();
            } else {
                timer.stop();
            }
        }));
    }

    AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long timestamp) {
            if (wPressed.get()) {
                if (!isTest(up) && !collisionController.checkCollisionWithMap(sprite, up) ) {
                    sprite.setLayoutY(sprite.getLayoutY() - movementVariable);
                    System.out.println("w");
                }
            }

            if (sPressed.get()) {

                if (!isTest(down) && !collisionController.checkCollisionWithMap(sprite, down)) {
                    sprite.setLayoutY(sprite.getLayoutY() + movementVariable);
                    System.out.println("s");
                }
            }

            if (aPressed.get()) {

                if (!isTest(left) && !collisionController.checkCollisionWithMap(sprite, left)) {
                    sprite.setLayoutX(sprite.getLayoutX() - movementVariable);
                    System.out.println("a");
                }
            }

            if (dPressed.get()) {

                if (!isTest(right) && !collisionController.checkCollisionWithMap(sprite, right)) {
                    sprite.setLayoutX(sprite.getLayoutX() + movementVariable);
                    System.out.println("d");
                }
            }
        }
    };

    private boolean isTest(String moveDirection) {
        boolean test = false;
        for (Node mapObject : gameMapPane.getChildren()) {
            if (!mapObject.equals(sprite)){
                test = collisionController.checkCollisionWithObject(sprite, mapObject, moveDirection);
                if (test){
                    return test;
                }
            }
        }
        return test;
    }

    private void movementSetup() {
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.W) {
                wPressed.set(true);
            }

            if (e.getCode() == KeyCode.A) {
                aPressed.set(true);
            }

            if (e.getCode() == KeyCode.S) {
                sPressed.set(true);
            }

            if (e.getCode() == KeyCode.D) {
                dPressed.set(true);
            }
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.W) {
                wPressed.set(false);
            }

            if (e.getCode() == KeyCode.A) {
                aPressed.set(false);
            }

            if (e.getCode() == KeyCode.S) {
                sPressed.set(false);
            }

            if (e.getCode() == KeyCode.D) {
                dPressed.set(false);
            }
        });
    }


}
