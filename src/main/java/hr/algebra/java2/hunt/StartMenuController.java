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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StartMenuController implements Initializable{
    private final int MAX_PLAYERS = 5;
    private final String DELIMTER = "/";
    @FXML
    private Button btnAddPlayer;
    private static Button btnAddPlayerFromParent;
    @FXML
    private Button btnStartGame;
    @FXML
    private FlowPane flpnMainMenu;
    private static FlowPane flpnParentToPlayerCard;
    @FXML
    private Label lblPlayerCounter;
    private static Label lblPlayerCounterFormParent;

    //PLAYER CARD VAR
    //Kako napraviti da ima imena koliko je igraca dodano
    @FXML
    private TextField tfPlayerName;
    @FXML
    private Label lblPlayerRole;

    private static List<Player> playersList = new ArrayList<>();

    private static int playerCounter = 0;
    //private int playerCounterSaveState;

//    public StartMenuController() {
//    }

    @FXML
    protected void onClickAddPlayer() {
        if (playerCounter == MAX_PLAYERS){
            return;
        }

        createPLayerCard();

        playerCounter++;
        if(playerCounter == MAX_PLAYERS){
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

    public void startGame(){
        if (!validInputs())

        for (int i = 0; i < playerCounter; i++) {
            String playerName = String.valueOf(
                    ((TextField)((GridPane)((Pane)flpnParentToPlayerCard.getChildren().get(i == MAX_PLAYERS ? i : i+1))
                    .getChildren().get(0)) //0 is index of gidpane in window
                    .getChildren().get(1)).getText() // 1 is index of wanted textfield
            );
            playersList.get(i).setName(playerName);
        }

        try {
            SceneUtils.createScene(StartMenuAplication.getMainStage(), "playingField.fxml", "Game of Hunt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onClickRemovePlayer()  {

        flpnParentToPlayerCard.getChildren().remove(playerCounter == MAX_PLAYERS ? playerCounter-1 : playerCounter);
        playersList.remove(playerCounter-1);

        if(playerCounter == MAX_PLAYERS){
            flpnParentToPlayerCard.getChildren().add(0, btnAddPlayerFromParent);
        }
        playerCounter--;
        lblPlayerCounterFormParent.setText(playerCounter + DELIMTER + MAX_PLAYERS);
    }

    private boolean validInputs() {
        //provjerava jesu sva imena unesena
        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(url.toString().contains("startMenu.fxml")){
            lblPlayerCounterFormParent = lblPlayerCounter;
            flpnParentToPlayerCard = flpnMainMenu;
            btnAddPlayerFromParent = btnAddPlayer;
        } else if(url.toString().contains("playerCard.fxml") && flpnParentToPlayerCard.getChildren().stream().count() == 1){
            lblPlayerRole.setText(PlayerRole.Hunter.toString());
            playersList.add(new Player(PlayerRole.Hunter));
        } else if(url.toString().contains("playerCard.fxml") && flpnParentToPlayerCard.getChildren().stream().count() > 1){
            lblPlayerRole.setText(PlayerRole.Survivor.toString());
            playersList.add(new Player(PlayerRole.Survivor));
        }
    }
}