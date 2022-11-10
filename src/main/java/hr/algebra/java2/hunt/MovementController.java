package hr.algebra.java2.hunt;

import hr.algebra.java2.model.Game;
import hr.algebra.java2.model.HunterPlayer;
import hr.algebra.java2.model.Player;
import hr.algebra.java2.model.SurvivorPlayer;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
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
    private static final String space = "space";

    private BooleanProperty wPressed = new SimpleBooleanProperty();
    private BooleanProperty aPressed = new SimpleBooleanProperty();
    private BooleanProperty sPressed = new SimpleBooleanProperty();
    private BooleanProperty dPressed = new SimpleBooleanProperty();
    private BooleanProperty spacePressed = new SimpleBooleanProperty();

    private CollisionController collisionController;

    private Pane gameMapPane;

    private Pane scene;

    private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed);

    //private static int screenRefreshRate = 60;
    public static Label lblFPS;

    //@FXML
    private List<Player> players = new ArrayList<>();

    public MovementController(Pane pane) {
        this.gameMapPane = pane;
        collisionController = new CollisionController(gameMapPane);
    }

    public void stopMovement() {
        timer.stop();
    }

    public void resumeMovement() {
        timer.start();
    }

    public void makeMovable(Pane scene) {
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
        private long lastRun = 0;

        @Override
        public void start() {
            lastRun = System.nanoTime();
            super.start();
        }

        @Override
        public void handle(long timestamp) {
//          For FPS
            long delta = timestamp - lastRun;
            lblFPS.setText(Double.toString(Math.round(getFPS(delta))));

            long elapsedNanoSeconds = timestamp - lastRun;

            // 1 second = 1,000,000,000 (1 billion) nanoseconds
            double elapsedSeconds = elapsedNanoSeconds / 1_000_000_000.0;

            for (Player p : Game.getAlivePlayersList()) {//players+
                ImageView sprite = p.getPlayerSprite();
                ImageView lightSource = p.getLightSource();
                Bounds lightSourceBounds = lightSource.getBoundsInParent();
                int collision;
                if (wPressed.get()) {
                    collision = collisionWithObj(p, up);
                    if (collision == 0 && !collisionController.checkCollisionWithMap(sprite, up)) {
                        lightSource.setLayoutY(lightSource.getLayoutY() - (p.getPlayerSpeed() * elapsedSeconds));
                        lightSource.setViewport(new Rectangle2D(
                                lightSourceBounds.getMinX(),
                                lightSourceBounds.getMinY() - (p.getPlayerSpeed() * elapsedSeconds),
                                Player.getLightSourceWidth(),
                                Player.getLightSourceHeight()));
                        sprite.setLayoutY(sprite.getLayoutY() - (p.getPlayerSpeed() * elapsedSeconds));
                        System.out.println("w");
                    } else if (collision == 2) {
                        System.out.println("Player killed");
                        killPlayer(((HunterPlayer) p).getVictimPlayerSprite());
                    }
                }

                if (sPressed.get()) {
                    collision = collisionWithObj(p, down);
                    if (collision == 0 && !collisionController.checkCollisionWithMap(sprite, down)) {
                        lightSource.setLayoutY(lightSource.getLayoutY() + (p.getPlayerSpeed() * elapsedSeconds));
                        lightSource.setViewport(new Rectangle2D(
                                lightSourceBounds.getMinX(),
                                lightSourceBounds.getMinY() + (p.getPlayerSpeed() * elapsedSeconds),
                                Player.getLightSourceWidth(),
                                Player.getLightSourceHeight()));
                        sprite.setLayoutY(sprite.getLayoutY() + (p.getPlayerSpeed() * elapsedSeconds));
                        System.out.println("s");
                    } else if (collision == 2) {
                        System.out.println("Player killed");
                        killPlayer(((HunterPlayer) p).getVictimPlayerSprite());
                    }
                }

                if (aPressed.get()) {
                    collision = collisionWithObj(p, left);
                    if (collision == 0 && !collisionController.checkCollisionWithMap(sprite, left)) {
                        lightSource.setLayoutX(lightSource.getLayoutX() - (p.getPlayerSpeed() * elapsedSeconds));
                        lightSource.setViewport(new Rectangle2D(
                                lightSourceBounds.getMinX() - (p.getPlayerSpeed() * elapsedSeconds),
                                lightSourceBounds.getMinY(),
                                Player.getLightSourceWidth(),
                                Player.getLightSourceHeight()));
                        sprite.setLayoutX(sprite.getLayoutX() - (p.getPlayerSpeed() * elapsedSeconds));
                        System.out.println("a");
                    } else if (collision == 2) {
                        System.out.println("Player killed");
                        //((HunterPlayer)p).setVictimPlayer();
                        killPlayer(((HunterPlayer) p).getVictimPlayerSprite());
                    }
                }

                if (dPressed.get()) {
                    collision = collisionWithObj(p, right);
                    if (collision == 0 && !collisionController.checkCollisionWithMap(sprite, right)) {
                        lightSource.setLayoutX(lightSource.getLayoutX() + (p.getPlayerSpeed() * elapsedSeconds));
                        lightSource.setViewport(new Rectangle2D(
                                lightSourceBounds.getMinX() + (p.getPlayerSpeed() * elapsedSeconds),
                                lightSourceBounds.getMinY(),
                                Player.getLightSourceWidth(),
                                Player.getLightSourceHeight()));
                        sprite.setLayoutX(sprite.getLayoutX() + (p.getPlayerSpeed() * elapsedSeconds));
                        System.out.println("d");
                    } else if (collision == 2) {
                        System.out.println("Player killed");
                        killPlayer(((HunterPlayer) p).getVictimPlayerSprite());
                    }
                }
            }
            lastRun = timestamp;
        }
    };

    private double getFPS(long delta) {
        double frameRate = 1d / delta;
        return frameRate * 1e9;
    }

    private void killPlayer(ImageView victimPlayerSprite) {
        for (Player p : Game.getAlivePlayersList()) {
            if (p.getPlayerSprite().equals(victimPlayerSprite)
                    && p.getClass().equals(SurvivorPlayer.class)
                   /* && !((SurvivorPlayer) p).isDead()*/) {
                Game.addMove(p, "Player killed");
                gameMapPane.getChildren().remove(p.getPlayerSprite());
                gameMapPane.getChildren().remove(p.getLightSource());
                Game.playerKilled((SurvivorPlayer) p);
            }
        }
    }

    private int collisionWithObj(Player player, String moveDirection) {
        boolean test = false;
        for (Node mapObject : gameMapPane.getChildren()) {
            boolean isLight = checkLight(mapObject);
            if (!mapObject.equals(player.getPlayerSprite())
                    && !isLight) {

                test = collisionController.checkCollisionWithObject(player.getPlayerSprite(), mapObject, moveDirection);

                if (test) {
                    if (player.getClass().equals(HunterPlayer.class) && mapObject.getClass().equals(player.getPlayerSprite().getClass())) {
                        ((HunterPlayer) player).setVictimPlayerSprite((ImageView) mapObject);
                        Game.addMove(player, "killed a player");
                        return 2;
                    }
                    return 1;
                }
            }
        }
        return 0;
    }

    private boolean checkLight(Node mapObject) {
        for (Player p : Game.getAlivePlayersList()) {
            if (mapObject.equals(p.getLightSource())) {
                return true;
            }
        }
        return false;
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
