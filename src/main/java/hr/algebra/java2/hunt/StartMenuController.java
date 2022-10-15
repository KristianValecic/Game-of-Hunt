package hr.algebra.java2.hunt;

import hr.algebra.java2.model.Player;
import hr.algebra.java2.model.PlayerRole;
import hr.algebra.java2.utilities.SceneUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StartMenuController implements Initializable {
    private static final String DELIMTER = "/";
    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 2;
    private static final int MAX_MATCHES = 7;
    private static final int MIN_MATCHES = 1;
    private static final String MAX_MATCHES_ERROR_MSG = "Maximum amount of matches is "+MAX_MATCHES;
    private static final String MIN_MATCHES_ERROR_MSG = "Minimum amount matches is "+MIN_MATCHES;
    private static int matchCounter = 1;
    private static int playerCounter = 0;
    private static Button btnAddPlayerFromParent;
    private static List<Player> playersList = new ArrayList<>();
    @FXML
    private Button btnAddPlayer;
    @FXML
    private Button btnAddMatch;
    @FXML
    private Button btnSubMatch;
    @FXML
    private Label lblMatchCounter;
    @FXML
    private FlowPane flpnMainMenu;
    @FXML
    private Label lblErrorForm;
    private static FlowPane flpnParentToPlayerCard;
    @FXML
    private Label lblPlayerCounter;
    private static Label lblPlayerCounterFormParent;


    //PLAYER CARD VAR
    @FXML
    private TextField tfPlayerName;
    @FXML
    private Label lblPlayerRole;
    @FXML
    private ImageView imgCharacter;

    @FXML
    protected void onClickAddPlayer() {
        if (playerCounter == MAX_PLAYERS) {
            return;
        }

        createPLayerCard();

        playerCounter++;
        if (playerCounter == MAX_PLAYERS) {
            flpnMainMenu.getChildren().remove(btnAddPlayer);
        }
        lblPlayerCounter.setText(playerCounter + DELIMTER + MAX_PLAYERS);
    }

    private void createPLayerCard() {
        FXMLLoader fxmlLoader = new FXMLLoader(StartMenuController.class.getResource("playerCard.fxml"));

        try {
            flpnMainMenu.getChildren().add(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startGame() {
        if (!validInputs()){
            lblErrorForm.setText("Invalid inputs");
            return;
        }

        for (int i = 0; i < playerCounter; i++) {
            String playerName = String.valueOf(
                    ((TextField) ((GridPane) ((Pane) flpnParentToPlayerCard.getChildren().get(i == MAX_PLAYERS-1 ? i : i + 1))
                            .getChildren().get(0)) //0 is index of gidpane in window
                            .getChildren().get(1)).getText() // 1 is index of wanted textfield
            );
            playersList.get(i).setName(playerName);
        }

        try {
            GameScreenController.setPlayersList(playersList);
            SceneUtils.createScene(StartMenuAplication.getMainStage(), "playingField.fxml", "Game of Hunt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onClickRemovePlayer() {

        flpnParentToPlayerCard.getChildren().remove(playerCounter == MAX_PLAYERS ? playerCounter - 1 : playerCounter);
        playersList.remove(playerCounter - 1);

        if (playerCounter == MAX_PLAYERS) {
            flpnParentToPlayerCard.getChildren().add(0, btnAddPlayerFromParent);
        }
        playerCounter--;
        lblPlayerCounterFormParent.setText(playerCounter + DELIMTER + MAX_PLAYERS);
    }

    @FXML
    protected void onClickAddMatch() {
        if (matchCounter >= MAX_MATCHES){
            lblErrorForm.setText(MAX_MATCHES_ERROR_MSG);
            return;
        }
        matchCounter++;
        if (matchCounter == MAX_MATCHES) {btnAddMatch.setOpacity(0.5);}
        else {
            btnSubMatch.setOpacity(1);
            lblErrorForm.setText("");
        }

        lblMatchCounter.setText(Integer.toString(matchCounter));
    }
    @FXML
    protected void onClickSubtractMatch() {
        if (matchCounter <= MIN_MATCHES){
            lblErrorForm.setText(MIN_MATCHES_ERROR_MSG);
            btnSubMatch.setOpacity(0.5);
            return;
        }
        matchCounter--;
        if (matchCounter == MIN_MATCHES) {btnSubMatch.setOpacity(0.5);}
        else {
            btnAddMatch.setOpacity(1);
            lblErrorForm.setText("");
        }

        lblMatchCounter.setText(Integer.toString(matchCounter));
    }

    private boolean validInputs() {
        //provjerava jesu sva imena unesena
        if (playerCounter < MIN_PLAYERS)
        {
            return false;
        }
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (url.toString().contains("startMenu.fxml")) {
            lblPlayerCounterFormParent = lblPlayerCounter;
            flpnParentToPlayerCard = flpnMainMenu;
            btnAddPlayerFromParent = btnAddPlayer;

            lblMatchCounter.setText(Integer.toString(matchCounter));
            lblPlayerCounter.setText(playerCounter + DELIMTER + MAX_PLAYERS);

        } else if (url.toString().contains("playerCard.fxml") && flpnParentToPlayerCard.getChildren().stream().count() == 1) {
            lblPlayerRole.setText(PlayerRole.Hunter.toString());
            imgCharacter.setImage(new Image("file:src/main/resources/hr/algebra/java2/hunt/hunterSprite.png"));
            playersList.add(new Player(PlayerRole.Hunter, imgCharacter.getImage()));
        } else if (url.toString().contains("playerCard.fxml") && flpnParentToPlayerCard.getChildren().stream().count() > 1) {
            lblPlayerRole.setText(PlayerRole.Survivor.toString());
            imgCharacter.setImage(new Image("file:src/main/resources/hr/algebra/java2/hunt/"+PlayerRole.Survivor.toString().toLowerCase()+"Sprite.png"));
            playersList.add(new Player(PlayerRole.Survivor, imgCharacter.getImage()));
        }
    }
}