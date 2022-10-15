package hr.algebra.java2.hunt;

import hr.algebra.java2.model.Player;
import hr.algebra.java2.model.PlayerRole;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

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

    //@FXML
    private List<Player> players = new ArrayList<>();

    //@FXML
    private Pane scene;

    public MovementController(Pane pane) {
        this.gameMapPane = pane;
        collisionController = new CollisionController(gameMapPane);
    }

    public void makeMovable(Player player, Pane scene) {
        this.players.add(player);
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
            for (Player p : players) {
                ImageView sprite = p.getPlayerSprite();

                if (wPressed.get()) {
                    if (collisionWithObj(p, up) == 0 && !collisionController.checkCollisionWithMap(sprite, up)) {
                        sprite.setLayoutY(sprite.getLayoutY() - movementVariable);
                        System.out.println("w");
                    }
                }

                if (sPressed.get()) {
                    if (collisionWithObj(p, down) == 0  && !collisionController.checkCollisionWithMap(sprite, down)) {
                        sprite.setLayoutY(sprite.getLayoutY() + movementVariable);
                        System.out.println("s");
                    }
                }

                if (aPressed.get()) {
                    if (collisionWithObj(p, left) == 0  && !collisionController.checkCollisionWithMap(sprite, left)) {
                        sprite.setLayoutX(sprite.getLayoutX() - movementVariable);
                        System.out.println("a");
                    }
                }

                if (dPressed.get()) {
                    if (collisionWithObj(p, right) == 0  && !collisionController.checkCollisionWithMap(sprite, right)) {
                        sprite.setLayoutX(sprite.getLayoutX() + movementVariable);
                        System.out.println("d");
                    }
                }
            }
        }
    };

    private int collisionWithObj(Player player, String moveDirection) {
        boolean test = false;
        for (Node mapObject : gameMapPane.getChildren()) {
            if (!mapObject.equals(player.getPlayerSprite())) {
                test = collisionController.checkCollisionWithObject(player.getPlayerSprite(), mapObject, moveDirection);
                if (test) {
                    if(player.getPlayerRole() == PlayerRole.Hunter || mapObject.getClass().equals(player.getPlayerSprite().getClass())){
                        System.out.println("Player killed");//TODO: Igrac koji je ulovljen treba nestati (return 2), to se treba brojati kao score za huntera
                        //TODO kada je ulovljen igrac restartaju se poz. tada je gotov 1 match. impl. tajmer za match
                        return 2;
                    }
                    return 1;
                }
            }
        }
        return 0;
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
