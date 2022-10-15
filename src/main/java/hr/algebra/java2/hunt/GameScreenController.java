package hr.algebra.java2.hunt;

import hr.algebra.java2.model.Player;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GameScreenController implements Initializable {

    private double spawnPointX = 100.0;
    private double spawnPointy = 340.0;

    @FXML
    private Pane pane;

    //@FXML
   // private ImageView playerOne;

    private static List<Player> playersList = new ArrayList<>();

    public static void setPlayersList(List<Player> playersList) {
        GameScreenController.playersList = playersList;
    }

    private static List<ImageView> playersImageViewList = new ArrayList<>();
    private MovementController movementController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movementController = new MovementController(pane);
        fillPlayerImageViewList();
        for (ImageView player:playersImageViewList) {
            //movementController.makeMovable(player, pane);
            player.relocate(spawnPointX += pane.getWidth()+spawnPointX, spawnPointy+=pane.getHeight());
            player.setFitWidth(50);
            player.setPreserveRatio(true);
            player.setSmooth(true);
            pane.getChildren().add(player);
        }
        playersImageViewList.forEach(p -> movementController.makeMovable(p, pane));
    }

    private void fillPlayerImageViewList() {
        for (Player p : playersList) {
            ImageView iw = new ImageView();
            iw.setImage(p.getPlayerImage());
            playersImageViewList.add(iw);
        }
    }
}
