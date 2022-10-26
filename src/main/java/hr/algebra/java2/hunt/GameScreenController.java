package hr.algebra.java2.hunt;

import hr.algebra.java2.dal.GameState;
import hr.algebra.java2.model.*;
import hr.algebra.java2.utilities.SceneUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;

public class GameScreenController implements Initializable {
    private double spawnPointX = 400.0;
    private double spawnPointY = 200.0;
    private double spawnPointDistance = 140.0;

    private double marginLightSource = 20;

    private static List<ImageView> playersImageViewList = new ArrayList<>();
    private MovementController movementController;
    //private int matchCounter = 1;
    @FXML
    private Pane paneGameMap;
    @FXML
    private ImageView imgGameMap;
    @FXML
    private Pane paneRootParent;
    @FXML
    private Label lblTimer;
    @FXML
    private Label lblMatchCounter;
    @FXML
    private Label lblFpsCount;

    //todo refaktoriraj timeline
    private Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1),
                    e -> {
                        if (Game.isGameOver()) {
                            //movementController.stopMovement();
                            EndOfGame();
                        } else if (GameTimer.getCurrentTime().equals(GameTimer.matchOver())/* && Game.getCurrentMatch() == Game.getAllMatchesCount()*/) {
                            System.out.println("Timer runout!");
                            Game.matchEndByTimerRunout();
                            //SetMoves();
                            //timerStop();
                            if (!Game.isGameOver()) {
                                newMatch();
                            } else {
                                EndOfGame();
                            }
                        } else {
                            GameTimer.countDownSecondPassed();
                            lblTimer.setText(GameTimer.getCurrentTime());
                        }
                    }));


    private void EndOfGame() {
        timerStop();
        movementController.stopMovement();
        cleanupMap();
        try {
            SceneUtils.createScene(StartMenuAplication.getMainStage(), "gameOverWindow.fxml", Game.getWindowTitle());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void cleanupMap() {
        for (Player player : Game.getAlivePlayersList()) {
            paneGameMap.getChildren().remove(player.getPlayerSprite());
        }
    }

    private void timerStop() {
        timeline.stop();
        //timeline.setDelay(new Duration(2000));
        //TODO doradi validaciju za start game (validacija imena)
        //TODO dodati delay za ekran game over i match end itd..
    }

    private void newMatch() {
        timerStop();
        for (Player player : Game.getAlivePlayersList()) {
            player.getPlayerSprite().relocate(spawnPointX -= spawnPointDistance, spawnPointY -= spawnPointDistance);
            lblMatchCounter.setText(Integer.toString(Game.getCurrentMatch()));

            lblMatchCounter.setText(Integer.toString(Game.getCurrentMatch()));
            lblTimer.setText(GameTimer.getCurrentTime());

            GameTimer.resetTimer();
            timeline.pause();
            timeline.setDelay(new Duration(0));
            timeline.play();
            //movementController.resumeMovement();
        }
    }

    @FXML
    void onSaveGame(ActionEvent event) throws IOException {
        //List<PlayerState> pStateList = new ArrayList<>();
        GameState gameState = new GameState();
        //1. set player listu sa score-om
        gameState.setPlayersList(Game.getPlayersList());

        //2. set alive player listu s koordinatama
        gameState.setAlivePlayers( paneGameMap.getChildren());

        //3. set game stanje timer, match
        gameState.setMatchState(Game.getCurrentMatch());
        gameState.setTimerState(GameTimer.getCurrentMinues(), GameTimer.getCurrentSeconds());

        //4. serijaliziraj
        try (ObjectOutputStream serializator = new ObjectOutputStream(new FileOutputStream("saveGame.ser")
        )) {
            serializator.writeObject(gameState);
        }
    }

    @FXML
    void onLoadGame(ActionEvent event) throws IOException, ClassNotFoundException {
        try (ObjectInputStream deserializator = new ObjectInputStream(new FileInputStream("saveGame.ser"))) {
            GameState gameState = (GameState) deserializator.readObject();

            cleanupMap();
            // Ocistit trenutnuo stanje gejma i zadat ovo
            //1. satavit igracena stare pozicije
            Game.loadPlayersList(gameState.getPlayersList());
            Game.getAlivePlayersList().clear();
            gameState.getAlivePlayersList().forEach((player, position) -> {
                Game.addAlivePlayer(player);
                //postavljaju se spriteovi na mapu
                setPlayerSprite(player);
                player.getPlayerSprite().relocate(position.getX(), position.getY());
                //setCharacterSpriteSettings(player);
                paneGameMap.getChildren().add(player.getPlayerSprite());
            });
            //2. zadat match
            Game.setCurrentMatch(gameState.getMatchState());
            lblMatchCounter.setText(Integer.toString(Game.getCurrentMatch()));
            //3. namjestit tajmer
            GameTimer.setMatchTime(gameState.getMinutesState(), gameState.getSecondsState());
            //refreshView();
        }
    }

    private void setCharacterSpriteSettings(Player player) {

    }

    private void setPlayerSprite(Player player) {
        if (player.getPlayerRole().equals(PlayerRole.Hunter)) {
            player.setPlayerSprite(new Image(Game.getHunterImagePath()));
        } else {
            player.setPlayerSprite(new Image(Game.getSurvivorImagePath()));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameTimer.resetTimer();
        movementController = new MovementController(paneGameMap);
        Game.setSpawnPoints(paneGameMap.getMaxWidth(), paneGameMap.getMaxHeight());
        //Set players
        for (Player player : Game.getPlayersList()) {
            //postavljaju se spriteovi na mapu
            if (player.getPlayerRole() == PlayerRole.Survivor){
                Coordinate spawnPoint = Game.getRandomSawnPoint();
                paneGameMap.getChildren().add(setPlayerLightSource(player, spawnPoint));
                player.getPlayerSprite().relocate(spawnPoint.getX(), spawnPoint.getY());
            }else{
                Coordinate hunterSpawnPoint = new Coordinate(paneGameMap.getMaxWidth()/2, paneGameMap.getMaxHeight()/2);
                paneGameMap.getChildren().add(setPlayerLightSource(player, hunterSpawnPoint));
                player.getPlayerSprite().relocate(hunterSpawnPoint.getX(), hunterSpawnPoint.getY());
            }
            //setCharacterSpriteSettings(player);
            paneGameMap.getChildren().add(player.getPlayerSprite());
        }
        movementController.makeMovable(paneRootParent);
        //Set timer
        lblTimer.setText(GameTimer.getMatchStartTime());
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        lblMatchCounter.setText(Integer.toString(Game.getCurrentMatch()));

        MovementController.lblFPS = lblFpsCount;

    }

    private ImageView setPlayerLightSource(Player player, Coordinate spawnPoint) {
        //kriran background image za lightsource
        Image playerImg = player.getPlayerSprite().getImage();
        ImageView lightSource = new ImageView(Game.getGameMapImagePath());
        double spawnX = spawnPoint.getX()-marginLightSource;
        double spawnY = spawnPoint.getY()-marginLightSource;

        //postavke image-a
        lightSource.setSmooth(true);
        lightSource.setPreserveRatio(true);

        //set viewport
        lightSource.setViewport(// provjeri brojeve
                new Rectangle2D
                (spawnX,
                spawnY,
                Game.getPlayerWidth()+(marginLightSource*2),// *2 has to compensate for the subtraction of margin
                Game.getPlayerHeight()+(marginLightSource*2))
                );
        lightSource.relocate(spawnX, spawnY);
        return lightSource;
    }
}
