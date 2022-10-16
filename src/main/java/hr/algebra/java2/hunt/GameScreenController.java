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
    private double spawnPointX = 100.0;
    private double spawnPointy = 340.0;
    //private GameTimer timer = new GameTimer(Game.getMatchMinutes(), Game.getMatchSeconds());
    private static List<ImageView> playersImageViewList = new ArrayList<>();
    @FXML
    private Pane pane;
    @FXML
    private Label lblTimer;

    private int second;
    private int minute;
    private MovementController movementController;
    Timer timer;
    private Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(1),
                            e -> {
                                if (GameTimer.getCurrentTime().equals(GameTimer.matchOver())) {
                                    System.out.println("Match over!");
                                    timelinestop();
                                }
                                GameTimer.countDownSecondPassed();
                                lblTimer.setText(GameTimer.getCurrentTime());

        }));

    private void timelinestop() {
        timeline.stop();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Set players
        movementController = new MovementController(pane);
        for (Player player : Game.getPlayersList()) {
            //postavljaju se spriteovi na mapu
            player.getPlayerSprite().relocate(spawnPointX += pane.getWidth() + spawnPointX, spawnPointy += pane.getHeight());
            player.getPlayerSprite().setFitWidth(50);
            player.getPlayerSprite().setPreserveRatio(true);
            player.getPlayerSprite().setSmooth(true);
            pane.getChildren().add(player.getPlayerSprite());
        }
        Game.getPlayersList().forEach(p -> movementController.makeMovable(p, pane));

        //Set timer
        lblTimer.setText(GameTimer.getCurrentTime());
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }



}
