package hr.algebra.java2.hunt;

import hr.algebra.java2.model.Game;
import hr.algebra.java2.model.GameTimer;
import hr.algebra.java2.model.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.net.URL;
import java.util.*;

public class GameScreenController implements Initializable {
    private double spawnPointX = 400.0;
    private double spawnPointY = 200.0;
    private double spawnPointDistance = 140.0;

    private static List<ImageView> playersImageViewList = new ArrayList<>();
    private MovementController movementController;
    private int matchCounter = 1;
    @FXML
    private Pane pane;
    @FXML
    private Label lblTimer;
    @FXML
    private Label lblMatchCounter;
    Timer timer;
    private Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(1),
                            e -> {
                                if (GameTimer.getCurrentTime().equals(GameTimer.matchOver())) {
                                    System.out.println("Match over!");
                                    timerStop();
                                    Game.matchEnd();
                                }
                                GameTimer.countDownSecondPassed();
                                lblTimer.setText(GameTimer.getCurrentTime());

        }));

    private void timerStop() {
        timeline.stop();
        newMatch();
    }

    private void newMatch() {
        for (Player player : Game.getAlivePlayersList()) {
            player.getPlayerSprite().relocate(spawnPointX -= spawnPointDistance, spawnPointY -= spawnPointDistance);
            lblMatchCounter.setText(Integer.toString(Game.getMatchesCount()));

            lblTimer.setText(GameTimer.getCurrentTime());
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Set players
        movementController = new MovementController(pane);
        for (Player player : Game.getPlayersList()) {
            //postavljaju se spriteovi na mapu
            player.getPlayerSprite().relocate(spawnPointX += spawnPointDistance, spawnPointY += spawnPointDistance); // ovo se moze zamijenit s newMath(),
            player.getPlayerSprite().setFitWidth(50);                                                   // ali kada shvatis kako ce spawnanje radit
            player.getPlayerSprite().setPreserveRatio(true);
            player.getPlayerSprite().setSmooth(true);
            pane.getChildren().add(player.getPlayerSprite());
        }
        Game.getPlayersList().forEach(p -> movementController.makeMovable(p, pane));

        lblMatchCounter.setText(Integer.toString(Game.getMatchesCount()));

        //Set timer
        lblTimer.setText(GameTimer.getCurrentTime());
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }



}
