package hr.algebra.java2.hunt;

import hr.algebra.java2.model.Game;
import hr.algebra.java2.model.HunterPlayer;
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

    private Pane scene;

    private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed);

    private double movementVariable = 1.2;

    //@FXML
    private List<Player> players = new ArrayList<>();

    public MovementController(Pane pane) {
        this.gameMapPane = pane;
        collisionController = new CollisionController(gameMapPane);
    }

//    public void stopMovement() {
//        timer.stop();
//    }
//
//    public void resumeMovement() {
//        timer.start();
//    }

    public void makeMovable(/*Player player,*/ Pane scene) {
        //this.players.add(player);
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
            for (Player p : Game.getAlivePlayersList()) {//players
                ImageView sprite = p.getPlayerSprite();
                int collision = 0;

                if (wPressed.get()) {
                    collision = collisionWithObj(p, up);
                    if (collision == 0 && !collisionController.checkCollisionWithMap(sprite, up)) {
                        sprite.setLayoutY(sprite.getLayoutY() - movementVariable);
                        System.out.println("w");
                    } else if (collision == 2) {
                        System.out.println("Player killed");
                        killPlayer(((HunterPlayer) p).getVictimPlayerSprite());
                    }
                }

                if (sPressed.get()) {
                    collision = collisionWithObj(p, down);
                    if (collision == 0 && !collisionController.checkCollisionWithMap(sprite, down)) {
                        sprite.setLayoutY(sprite.getLayoutY() + movementVariable);
                        System.out.println("s");
                    } else if (collision == 2) {
                        System.out.println("Player killed");
                        killPlayer(((HunterPlayer) p).getVictimPlayerSprite());
                    }
                }

                if (aPressed.get()) {
                    collision = collisionWithObj(p, left);
                    if (collision == 0 && !collisionController.checkCollisionWithMap(sprite, left)) {
                        sprite.setLayoutX(sprite.getLayoutX() - movementVariable);
                        System.out.println("a");
                    } else if (collision == 2) { // znaci da je nekog ubio, dotako
                        System.out.println("Player killed");
                        //((HunterPlayer)p).setVictimPlayer();
                        killPlayer(((HunterPlayer) p).getVictimPlayerSprite());
                    }
                }

                if (dPressed.get()) {
                    collision = collisionWithObj(p, right);
                    if (collision == 0 && !collisionController.checkCollisionWithMap(sprite, right)) {
                        sprite.setLayoutX(sprite.getLayoutX() + movementVariable);
                        System.out.println("d");
                    } else if (collision == 2) {
                        System.out.println("Player killed");
                        killPlayer(((HunterPlayer) p).getVictimPlayerSprite());
                    }
                }
            }
        }
    };

    private void killPlayer(ImageView victimPlayerSprite) {
        for (Player p : Game.getPlayersList()) {
            if (p.getClass().equals(HunterPlayer.class)) {
                Game.addMove(p, "killed a player"); //TODO dodaj kojeg igraca
            }
            if (p.getPlayerSprite().equals(victimPlayerSprite)) {
                Game.addMove(p, "Player killed");
                gameMapPane.getChildren().remove(p.getPlayerSprite());
                //players.remove(p);
                //Game.getPlayersList().remove(p);
                Game.playerKilled(p);
            }
        }
    }

    private int collisionWithObj(Player player, String moveDirection) {
        boolean test = false;
        for (Node mapObject : gameMapPane.getChildren()) {
            if (!mapObject.equals(player.getPlayerSprite())) {
                test = collisionController.checkCollisionWithObject(player.getPlayerSprite(), mapObject, moveDirection);
                if (test) {
                    if (player.getClass().equals(HunterPlayer.class) && mapObject.getClass().equals(player.getPlayerSprite().getClass())) {
                        ((HunterPlayer) player).setVictimPlayerSprite((ImageView) mapObject);
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
