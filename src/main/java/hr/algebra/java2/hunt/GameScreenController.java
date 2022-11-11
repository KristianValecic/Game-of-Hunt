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
    private static final String DELIM_MATCH = "/";
    private double spawnPointX = 400.0;
    private double spawnPointY = 200.0;
    private double spawnPointDistance = 140.0;
    private boolean timelineRuningFlag;
    private boolean pauseMatchesRuningFlag;

    private static List<ImageView> playersLightSourceList = new ArrayList<>();
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
    @FXML
    private Label lblMatchOver;
    @FXML
    private Pane paneLightSource;

    //todo refaktoriraj timeline
    private Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1),
                    e -> {
                        timelineRuningFlag = true;
                        if (Game.isGameOver()) {
                            //movementController.stopMovement();
                            EndOfGame();
                        } else if (GameTimer.getTime().equals(GameTimer.matchOver())/* && Game.getCurrentMatch() == Game.getAllMatchesCount()*/) {
                            System.out.println("Timer runout!");
                            Game.matchEndByTimerRunout();
                            if (!Game.isGameOver()) {
                                newMatch();
                            } else {
                                EndOfGame();
                            }
                        } else {
                            //lblTimer.setText(GameTimer.getTime());
                            GameTimer.countDownSecondPassed();
                            lblTimer.setText(GameTimer.getTime());
                        }
                    })
    );

    private Timeline pauseInBetweenMatches = new Timeline(
            new KeyFrame(Duration.seconds(1),
                    e -> {
                pauseMatchesRuningFlag = true;
                        if (GameTimer.isPauseOver()) {
                            lblMatchOver.setVisible(false);
                            GameTimer.resetTimer();
                            cleanupMap();
                            Game.newMatch();
                            spawnPlayers();
                            setMatchCounterLabel();
                            pauseStop();
                            movementController.resumeMovement();
                            //reactivate kill option
                            Game.resumeKilling();
                            timeline.play();
                        } else {
                            GameTimer.countDownPauseSecondPassed();
                        }
                    }));

    private void pauseStop() {
        pauseMatchesRuningFlag = false;
        pauseInBetweenMatches.stop();
    }

    private void timerStop() {
        timelineRuningFlag = false;
        timeline.stop();
    }

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
        lblMatchOver.setVisible(false);
        for (Player player : Game.getAlivePlayersList()) {
            paneGameMap.getChildren().remove(player.getPlayerSprite());
            paneGameMap.getChildren().remove(player.getLightSource());
        }
    }

    private void newMatch() {
        timeline.stop();
        lblMatchOver.setVisible(true);
        //remove kill option
        Game.pauseKilling();
        pauseInBetweenMatches.setCycleCount(Timeline.INDEFINITE);
        pauseInBetweenMatches.play();
        //timeline.pause();
    }

    @FXML
    void onSaveGame(ActionEvent event) throws IOException {
        //List<PlayerState> pStateList = new ArrayList<>();
        GameState gameState = new GameState();
        //1. set player listu sa score-om
        gameState.setPlayersList(Game.getPlayersList());
        gameState.setMatchAllCount(Game.getAllMatchesCount());
        //2. set alive player listu s koordinatama
        gameState.setAlivePlayersPositions(paneGameMap.getChildren());
        //gameState.setAlivePlayersLightSourcePositions(paneGameMap.getChildren());

        //3. set game stanje timer, match
        gameState.setMatchState(Game.getCurrentMatch());
        gameState.setTimerState(GameTimer.getCurrentMinues(), GameTimer.getCurrentSeconds());

        //4. serijaliziraj
        try (ObjectOutputStream serializator = new ObjectOutputStream(new FileOutputStream(Game.SER_FILE)
        )) {
            serializator.writeObject(gameState);
        }
    }

    @FXML
    void onLoadGame(ActionEvent event) throws IOException, ClassNotFoundException {
        try (ObjectInputStream deserializator = new ObjectInputStream(new FileInputStream(Game.SER_FILE))) {
            GameState gameState = (GameState) deserializator.readObject();

            cleanupMap();
            timeline.stop();
            pauseInBetweenMatches.stop();
            // Ocistit trenutnuo stanje gejma i zadat ovo
            //1. satavit igracena stare pozicije
            Game.loadGame(gameState);

            gameState.getAlivePlayersLightSourceList().forEach((player, position) -> {
                //postavljaju se lightSource na mapu
                player.loadPlayerLightSource(position);
                paneGameMap.getChildren().add(player.getLightSource());
            });
            gameState.getAlivePlayersList().forEach((player, position) -> {
                Game.addAlivePlayer(player);
                //postavljaju se spriteovi na mapu
                player.loadPlayerSprite(position);
                paneGameMap.getChildren().add(player.getPlayerSprite());
            });
            //2. zadat match


            setMatchCounterLabel();
            //refreshView();
            //3. namjestit tajmer
            lblTimer.setText(GameTimer.getTime());
            if (timelineRuningFlag){
                timeline.play();
            } else if (pauseMatchesRuningFlag) {
                pauseInBetweenMatches.play();
            }
        }
    }

    private void setMatchCounterLabel() {
        lblMatchCounter.setText(Game.getCurrentMatch() +
                DELIM_MATCH +
                Game.getAllMatchesCount());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameTimer.resetTimer();
        movementController = new MovementController(paneGameMap);
        lblMatchOver.setVisible(false);

        spawnPlayers();

        movementController.makeMovable(paneRootParent);
        //Set timer
        lblTimer.setText(GameTimer.getTime());
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        setMatchCounterLabel();

        MovementController.lblFPS = lblFpsCount;
    }

    private void spawnPlayers() {
        Game.setSpawnPointsOnMap(paneGameMap.getMaxWidth(), paneGameMap.getMaxHeight());
        for (Player player : Game.getAlivePlayersList()) {
            if (player.getPlayerRole() == PlayerRole.Survivor) {
                Coordinate spawnPoint = Game.getRandomSawnPoint();
                ImageView playerLightSource = player.setPlayerLightSourceInitial(spawnPoint);
                paneGameMap.getChildren().add(playerLightSource);
                playerLightSource.toBack();
                player.setLightSource(playerLightSource);
                player.getPlayerSprite().relocate(spawnPoint.getX(), spawnPoint.getY());
            } else {
                Coordinate hunterSpawnPoint = new Coordinate(paneGameMap.getMaxWidth() / 2, paneGameMap.getMaxHeight() / 2);
                ImageView playerLightSource = player.setPlayerLightSourceInitial(hunterSpawnPoint);
                paneGameMap.getChildren().add(playerLightSource);
                player.setLightSource(playerLightSource);
                player.getPlayerSprite().relocate(hunterSpawnPoint.getX(), hunterSpawnPoint.getY());
            }
            paneGameMap.getChildren().add(player.getPlayerSprite());
        }
    }
}
