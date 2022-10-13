package hr.algebra.java2.hunt;

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
import java.util.ResourceBundle;

public class GameScreenController implements Initializable {

    @FXML
    private Pane pane;

    @FXML
    private ImageView playerOne;

    private MovementController movementController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movementController = new MovementController(pane);
        movementController.makeMovable(playerOne, pane);
    }
}
