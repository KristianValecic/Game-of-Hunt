package hr.algebra.java2.hunt;

import hr.algebra.java2.dal.GameState;
import hr.algebra.java2.model.*;
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

import java.util.Iterator;

public class MovementController /*implements Runnable*/{
    private static final String up = "up";
    private static final String down = "down";
    private static final String left = "left";
    private static final String right = "right";
    private static final String space = "space";

    private static final int NO_COLLISION = 0;
    private static final int COLLISION = 1;
    private static final int KILL_PLAYER = 2;
    private static final int CAUGHT_IN_TRAP = 3;

    private BooleanProperty wPressed = new SimpleBooleanProperty();
    private BooleanProperty aPressed = new SimpleBooleanProperty();
    private BooleanProperty sPressed = new SimpleBooleanProperty();
    private BooleanProperty dPressed = new SimpleBooleanProperty();
    private BooleanProperty spacePressed = new SimpleBooleanProperty();

    private boolean spaceClickFlag = false;
    private CollisionController collisionController;

    private Pane gameMapPane;

    private Pane scene;

    private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed).or(spacePressed);
    private GameTimer gameTimer = GameTimer.getInstance();


    //private static int screenRefreshRate = 60;
//    private List<Player> players = new ArrayList<>();
    public static Label lblFPS;

    //@FXML

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

        keyPressed.addListener((observableValue, aBoolean, t1) -> {
            if (!aBoolean) {
                timer.start();
            } else timer.stop();
        });
    }

    private double elapsedSeconds;
    private AnimationTimer timer = new AnimationTimer() {
        private long lastRun = 0;

        @Override
        public void start() {
            lastRun = System.nanoTime();
            super.start();
        }

        @Override
        public void handle(long timestamp) {

            //if () {
            if (spacePressed.get() && !spaceClickFlag && Game.getTrapCount() != Game.MIN_TRAPS) {
                ImageView trapSprite = new ImageView(Game.TRAP_PATH);
                ImageView hunteSprite = Game.getHunterPlayer().getPlayerSprite();
                Bounds hunterBounds = hunteSprite.getBoundsInParent();
                Coordinate trapSpawnCoordinate = getBottomCenterCoordinates(trapSprite, hunterBounds);
                trapSprite.relocate(trapSpawnCoordinate.getX(), trapSpawnCoordinate.getY());
                gameMapPane.getChildren().add(trapSprite);
                //trapSprite.toBack();
                System.out.println("trap set");
                Game.trapSet();
                spaceClickFlag = true;
            }
            //return;
            // }
//          For FPS
            long delta = timestamp - lastRun;
            lblFPS.setText(Double.toString(Math.round(getFPS(delta))));

            long elapsedNanoSeconds = timestamp - lastRun;

            // 1 second = 1,000,000,000 (1 billion) nanoseconds
            elapsedSeconds = elapsedNanoSeconds / 1_000_000_000.0;

            movePlayers();
            lastRun = timestamp;
        }
    };

    private /*synchronized*/ void movePlayers() {
        for (Player p : Game.getAlivePlayersList()) {//players+
            ImageView sprite = p.getPlayerSprite();
            ImageView lightSource = p.getLightSource();
            Bounds lightSourceBounds = lightSource.getBoundsInParent();
            if (SurvivorPlayer.class.equals(p.getClass()) && ((SurvivorPlayer) p).isCaughtInTrap(gameTimer.getTime())) {
                System.out.println(p.getPlayerName() + "still in trap");
                //return;
            }
            int collision;
            if (wPressed.get()) {
                collision = collisionWithObj(p, up, new Coordinate(sprite.getLayoutX(), sprite.getLayoutY()));
                if (collision == 0 && !collisionController.checkCollisionWithMap(p, sprite, up)) {
                    lightSource.setLayoutY(lightSource.getLayoutY() - (p.getPlayerSpeed() * elapsedSeconds));
                    lightSource.setViewport(new Rectangle2D(
                            lightSourceBounds.getMinX(),
                            lightSourceBounds.getMinY() - (p.getPlayerSpeed() * elapsedSeconds),
                            Player.getLightSourceWidth(),
                            Player.getLightSourceHeight()));
                    sprite.setLayoutY(sprite.getLayoutY() - (p.getPlayerSpeed() * elapsedSeconds));
                    System.out.println("w");
                } else if (collision == 2) {
                    killPlayer(((HunterPlayer) p).getVictimPlayerSprite(), new Coordinate(sprite.getLayoutX(), sprite.getLayoutY()));
                } else if (collision == 3) {
                    caugthInTrap(p);
                }
            }

            if (sPressed.get()) {
                collision = collisionWithObj(p, down, new Coordinate(sprite.getLayoutX(), sprite.getLayoutY()));
                if (collision == 0 && !collisionController.checkCollisionWithMap(p, sprite, down)) {
                    lightSource.setLayoutY(lightSource.getLayoutY() + (p.getPlayerSpeed() * elapsedSeconds));
                    lightSource.setViewport(new Rectangle2D(
                            lightSourceBounds.getMinX(),
                            lightSourceBounds.getMinY() + (p.getPlayerSpeed() * elapsedSeconds),
                            Player.getLightSourceWidth(),
                            Player.getLightSourceHeight()));
                    sprite.setLayoutY(sprite.getLayoutY() + (p.getPlayerSpeed() * elapsedSeconds));
                    System.out.println("s");
                } else if (collision == 2) {
                    killPlayer(((HunterPlayer) p).getVictimPlayerSprite(), new Coordinate(sprite.getLayoutX(), sprite.getLayoutY()));
                }
            }

            if (aPressed.get()) {
                collision = collisionWithObj(p, left, new Coordinate(sprite.getLayoutX(), sprite.getLayoutY()));
                if (collision == 0 && !collisionController.checkCollisionWithMap(p, sprite, left)) {
                    lightSource.setLayoutX(lightSource.getLayoutX() - (p.getPlayerSpeed() * elapsedSeconds));
                    lightSource.setViewport(new Rectangle2D(
                            lightSourceBounds.getMinX() - (p.getPlayerSpeed() * elapsedSeconds),
                            lightSourceBounds.getMinY(),
                            Player.getLightSourceWidth(),
                            Player.getLightSourceHeight()));
                    sprite.setLayoutX(sprite.getLayoutX() - (p.getPlayerSpeed() * elapsedSeconds));
                    System.out.println("a");
                } else if (collision == 2) {
                    //((HunterPlayer)p).setVictimPlayer();
                    double layoutX = sprite.getLayoutX();
                    double layoutY = sprite.getLayoutY();
                    killPlayer(((HunterPlayer) p).getVictimPlayerSprite(),new Coordinate(layoutX, layoutY));
                }
            }

            if (dPressed.get()) {
                collision = collisionWithObj(p, right, new Coordinate(sprite.getLayoutX(), sprite.getLayoutY()));
                if (collision == 0 && !collisionController.checkCollisionWithMap(p, sprite, right)) {
                    lightSource.setLayoutX(lightSource.getLayoutX() + (p.getPlayerSpeed() * elapsedSeconds));
                    lightSource.setViewport(new Rectangle2D(
                            lightSourceBounds.getMinX() + (p.getPlayerSpeed() * elapsedSeconds),
                            lightSourceBounds.getMinY(),
                            Player.getLightSourceWidth(),
                            Player.getLightSourceHeight()));
                    sprite.setLayoutX(sprite.getLayoutX() + (p.getPlayerSpeed() * elapsedSeconds));
                    System.out.println("d");
                } else if (collision == 2) {
                    killPlayer(((HunterPlayer) p).getVictimPlayerSprite(),new Coordinate(sprite.getLayoutX(), sprite.getLayoutY()));
                }
            }
        }
    }

    private void caugthInTrap(Player p) {
        System.out.println(p.getPlayerName() + " caught in trap");
        //gameTimer.
        if (SurvivorPlayer.class.equals(p.getClass())) {
            ((SurvivorPlayer) p).caughtInTrap(gameTimer.getTime());
        }
    }

    private Coordinate getBottomCenterCoordinates(ImageView sprite, Bounds playerSpriteBounds) {
        double x = (playerSpriteBounds.getMinX() + (playerSpriteBounds.getMaxX() - playerSpriteBounds.getMinX()) / 2) - (sprite.getBoundsInLocal().getWidth() / 2);
        double y = playerSpriteBounds.getMinY() + playerSpriteBounds.getHeight();
        return new Coordinate(x, y);
    }

    private double getFPS(long delta) {
        double frameRate = 1d / delta;
        return frameRate * 1e9;
    }

    private /*synchronized*/ void killPlayer(ImageView victimPlayerSprite, Coordinate coordinate) {
//        while (GameState.isWritingInGameState) {
//            try {
//                System.out.println("Thread tried to write in gamestate");
//                wait();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        GameState.isWritingInGameState = true;
        for (Player p : Game.getAlivePlayersList()) {
            if (p.getPlayerSprite().equals(victimPlayerSprite)
                    && p.getClass().equals(SurvivorPlayer.class)
                /* && !((SurvivorPlayer) p).isDead()*/) {
                Game.addMove(p, p.getPlayerName() + " got killed", coordinate);
                gameMapPane.getChildren().remove(p.getPlayerSprite());
                gameMapPane.getChildren().remove(p.getLightSource());
                Game.playerKilled((SurvivorPlayer) p);

//               GameState.isWritingInGameState = false;
//                notifyAll();
            }
        }
    }

    private /*synchronized*/ int collisionWithObj(Player player, String moveDirection, Coordinate coordinate) {
//        while (GameState.isWritingInGameState) {
//            try {
//                System.out.println("Thread tried to move players");
//                wait();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
        boolean collision = false;
        for (Node mapObject : gameMapPane.getChildren()) {
            boolean isLight = checkLight(mapObject);

            if (!mapObject.equals(player.getPlayerSprite())
                    && !isLight) {
                if (player.getClass().equals(HunterPlayer.class) &&
                        ImageView.class.equals(mapObject.getClass()) &&
                        ((ImageView) mapObject).getImage().getUrl().equals(Game.TRAP_PATH)) {
                    return NO_COLLISION;
                }
                collision = collisionController.checkCollisionWithObject(player.getPlayerSprite(), mapObject, moveDirection);

                if (collision) {
                    if (player.getClass().equals(HunterPlayer.class) &&
                            mapObject.getClass().equals(player.getPlayerSprite().getClass()) &&
                            ((HunterPlayer) player).canKill()) {
                        ((HunterPlayer) player).setVictimPlayerSprite((ImageView) mapObject);
                        Game.addMove(player, player.getPlayerName() + " killed a player", coordinate);
                        System.out.println("Player killed");
                        return KILL_PLAYER;
                    }
                    if (ImageView.class.equals(mapObject.getClass()) &&
                            ((ImageView) mapObject).getImage().getUrl().equals(Game.TRAP_PATH)) {
                        return CAUGHT_IN_TRAP;
                    }
                    Game.addMove(player, player.getPlayerName()  + " hit a wall", coordinate);
                    return COLLISION;
                }
            }
        }
        return NO_COLLISION;
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
            if (e.getCode() == KeyCode.SPACE) {
                spacePressed.set(true);
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
            if (e.getCode() == KeyCode.SPACE) {
                spaceClickFlag = false;
                spacePressed.set(false);
            }
        });
    }

//    @Override
//    public void run() {
//        while (true) {
//            movePlayers();
//        }
//    }
}
