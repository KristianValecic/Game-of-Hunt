package hr.algebra.java2.hunt;

import hr.algebra.java2.dal.GameState;
import hr.algebra.java2.dal.PlayerState;
import hr.algebra.java2.model.Game;
import hr.algebra.java2.model.GameTimer;
import hr.algebra.java2.model.Player;
import hr.algebra.java2.model.PlayerRole;
import hr.algebra.java2.utilities.SceneUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;

public class GameScreenController implements Initializable {
    private double spawnPointX = 400.0;
    private double spawnPointY = 200.0;
    private double spawnPointDistance = 140.0;

    private static List<ImageView> playersImageViewList = new ArrayList<>();
    private MovementController movementController;
    private double characterWidth = 40;
    //private int matchCounter = 1;
    @FXML
    private Pane paneGameMap;
    @FXML
    private Pane paneRootParent;
    @FXML
    private Label lblTimer;
    @FXML
    private Label lblMatchCounter;
    @FXML
    private Label lblFpsCount;


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


//    private void SetMoves() {
//        for (Player p:Game.getPlayersList()) {
//            if (p.getClass().equals(SruvivorPlayer.class)){
//                Game.addMove(p, "Sruvived");
//            }
//        }
//    }

    private void EndOfGame() {
        timerStop();
        movementController.stopMovement();
        cleanup();
        try {
            SceneUtils.createScene(StartMenuAplication.getMainStage(), "gameOverWindow.fxml", Game.getWindowTitle());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void cleanup() {
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
        gameState.setAlivePlayers(/*Game.getAlivePlayersList(),*/ paneGameMap.getChildren());

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

            cleanup();
            //1. satavit igracena stare pozicije
            Game.loadPlayersList(gameState.getPlayersList());
            Game.getAlivePlayersList().clear();
            gameState.getAlivePlayersList().forEach((player, position) -> {
                Game.addAlivePlayer(player);
                //postavljaju se spriteovi na mapu
                setPlayerSprite(player);
                player.getPlayerSprite().relocate(position.getX(), position.getY());
                setCharacterSpriteSettings(player);
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
        player.getPlayerSprite().setFitWidth(characterWidth);
        player.getPlayerSprite().setPreserveRatio(true);
        player.getPlayerSprite().setSmooth(true);
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
        //Set players
        movementController = new MovementController(paneGameMap);
        for (Player player : Game.getPlayersList()) {
            //postavljaju se spriteovi na mapu
            player.getPlayerSprite().relocate(spawnPointX += spawnPointDistance, spawnPointY += spawnPointDistance); // ovo se moze zamijenit s newMath(),
            setCharacterSpriteSettings(player);
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
}
