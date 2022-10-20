package hr.algebra.java2.hunt;

import hr.algebra.java2.model.Game;
import hr.algebra.java2.model.GameTimer;
import hr.algebra.java2.model.Player;
import hr.algebra.java2.utilities.SceneUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class GameScreenController implements Initializable {
    private double spawnPointX = 400.0;
    private double spawnPointY = 200.0;
    private double spawnPointDistance = 140.0;

    private static List<ImageView> playersImageViewList = new ArrayList<>();
    private MovementController movementController;
    private int matchCounter = 1;
    @FXML
    private Pane paneGameMap;
    @FXML
    private Pane paneRootParent;
    @FXML
    private Label lblTimer;
    @FXML
    private Label lblMatchCounter;
    Timer timer;

    private Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1),
                    e -> {
                        if (Game.isGameOver()) {
                            //movementController.stopMovement();
                            EndOfGame();
                        } else if (GameTimer.getCurrentTime().equals(GameTimer.matchOver())/* && Game.getMatchesCount() == Game.getAllMatchesCount()*/) {
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
            lblMatchCounter.setText(Integer.toString(Game.getMatchesCount()));

            lblMatchCounter.setText(Integer.toString(Game.getMatchesCount()));
            lblTimer.setText(GameTimer.getCurrentTime());

            GameTimer.resetTimer();
            timeline.pause();
            timeline.setDelay(new Duration(0));
            timeline.play();
            //movementController.resumeMovement();
        }
    }

    @FXML
    private Label lblFpsCount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameTimer.resetTimer();
        //Set players
        movementController = new MovementController(paneGameMap);
        for (Player player : Game.getPlayersList()) {
            //postavljaju se spriteovi na mapu
            player.getPlayerSprite().relocate(spawnPointX += spawnPointDistance, spawnPointY += spawnPointDistance); // ovo se moze zamijenit s newMath(),
            player.getPlayerSprite().setFitWidth(50);                                                   // ali kada shvatis kako ce spawnanje radit
            player.getPlayerSprite().setPreserveRatio(true);
            player.getPlayerSprite().setSmooth(true);
            paneGameMap.getChildren().add(player.getPlayerSprite());
        }
        movementController.makeMovable(/*player,*/ paneRootParent);
        //Set timer
        lblTimer.setText(GameTimer.getMatchStartTime());
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        lblMatchCounter.setText(Integer.toString(Game.getMatchesCount()));

        MovementController.lblFPS = lblFpsCount;
    }


}
