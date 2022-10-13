package hr.algebra.java2.hunt;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class MovementController {

    private BooleanProperty wPressed = new SimpleBooleanProperty();
    private BooleanProperty aPressed = new SimpleBooleanProperty();
    private BooleanProperty sPressed = new SimpleBooleanProperty();
    private BooleanProperty dPressed = new SimpleBooleanProperty();

    private CollisionController collisionController;

    private Pane gameMapPane;

    public String getDirection() {
        if (wPressed.get()){
            return "up";
        }  if (sPressed.get()) {
            return "down";
        }  if (aPressed.get()) {
            return "left";
        }  if (dPressed.get()) {
            return "right";
        }
        return "";
    }

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

    public void makeMovable(ImageView sprite, Pane scene){
            this.sprite = sprite;
            this.scene = scene;

            movementSetup();

            collisionController.setMovementController(this);

            keyPressed.addListener(((observableValue, aBoolean, t1) -> {
                if(!aBoolean){
                    timer.start();
                } else {
                    timer.stop();
                }
            }));
        }

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long timestamp) {
                if (!collisionController.checkCollision(sprite)){
                    if(wPressed.get()) {
                        sprite.setLayoutY(sprite.getLayoutY() - movementVariable);
                    }

                    if(sPressed.get()){
                        sprite.setLayoutY(sprite.getLayoutY() + movementVariable);
                        System.out.println("s");
                    }

                    if(aPressed.get()){
                            sprite.setLayoutX(sprite.getLayoutX() - movementVariable);
                            System.out.println("a");
                    }

                    if(dPressed.get()){
                        sprite.setLayoutX(sprite.getLayoutX() + movementVariable);
                    }
                }
            }
        };

        private void movementSetup(){
            scene.setOnKeyPressed(e -> {
                if(e.getCode() == KeyCode.W) {
                    wPressed.set(true);
                }

                if(e.getCode() == KeyCode.A) {
                    aPressed.set(true);
                }

                if(e.getCode() == KeyCode.S) {
                    sPressed.set(true);
                }

                if(e.getCode() == KeyCode.D) {
                    dPressed.set(true);
                }
            });

            scene.setOnKeyReleased(e ->{
                if(e.getCode() == KeyCode.W) {
                    wPressed.set(false);
                }

                if(e.getCode() == KeyCode.A) {
                    aPressed.set(false);
                }

                if(e.getCode() == KeyCode.S) {
                    sPressed.set(false);
                }

                if(e.getCode() == KeyCode.D) {
                    dPressed.set(false);
                }
            });
        }


}
