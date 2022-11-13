package hr.algebra.java2.hunt;

import hr.algebra.java2.model.*;
import hr.algebra.java2.utilities.SceneUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

import javax.sound.midi.MidiFileFormat;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StartMenuController implements Initializable {
    private static final String DELIMTER = "/";

    private static final String MIN_PLAYER_ERROR_MSG = "Minimum amount of players is "+Game.MIN_PLAYERS;
    private static final String MAX_MATCHES_ERROR_MSG = "Maximum amount of matches is "+Game.MAX_MATCHES;
    private static final String MIN_MATCHES_ERROR_MSG = "Minimum amount matches is "+Game.MIN_MATCHES;
    private static final String MAX_MATCH_TIME_ERROR = "Maximum match time is " + GameTimer.MAX_MATCH_TIME;
    private static final String MIN_MATCH_TIME_ERROR = "Minimum match time is " + GameTimer.MIN_MATCH_TIME;
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
    private Button btnSubMatchTime;
    @FXML
    private Button btnAddMatchTime;
    @FXML
    private Label lblMatchCounter;
    @FXML
    private FlowPane flpnMainMenu;
    @FXML
    private Label lblErrorForm;
    @FXML
    private Label lblMatchTime;
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
    private GameTimer gameTimer;

    @FXML
    protected void onClickAddPlayer() {
        if (playerCounter == Game.MAX_PLAYERS) {
            return;
        }

        createPLayerCard();

        playerCounter++;
        if (playerCounter == Game.MAX_PLAYERS) {
            flpnMainMenu.getChildren().remove(btnAddPlayer);
        }
        lblPlayerCounter.setText(playerCounter + DELIMTER + Game.MAX_PLAYERS);
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
        //provjerava je li playerCount i je li su sva imena unesena
        if (!playersValid()){
            return;
        }

        for (Node node:flpnParentToPlayerCard.getChildren()) {
            if (node.getClass().equals(Pane.class)){
                GridPane playerCard= ((GridPane) ((Pane) node)
                        .getChildren().get(0));//0 is index of gidpane in window

                String playerName = String.valueOf(((TextField)playerCard.getChildren().get(1)).getText()); // 1 is index of wanted textfield
                String playerRole = "";
                for (Node n:playerCard.getChildren()) {
                    if (n.getClass().equals(FlowPane.class)){
                        for (Node lbl:((FlowPane) n).getChildren()) {
                            if (lbl.getClass().equals(Label.class)){
                                playerRole = ((Label) lbl).getText();
                            }
                        }
                    }
                }
                if (playerRole.equals(PlayerRole.Hunter.toString())){
                    playersList.add(new HunterPlayer(playerName, PlayerRole.Hunter, new Image(Game.HUNTER_IMAGE_PATH)));
                }else {
                    playersList.add(new SurvivorPlayer(playerName, PlayerRole.Survivor, new Image(Game.SURVIVOR_IMAGE_PATH)));
                }
            }
//            gameTimer.getTime();
//            gameTimer.getCurrentTime();
        }

        //sets time of match


        try {
            //GameScreenController.setPlayersList(playersList);
            Game.setInitialPlayersLists(playersList);
            Game.setAllMatchesCount(matchCounter);
            SceneUtils.createScene(StartMenuAplication.getMainStage(), "playingField.fxml", Game.getWindowTitle());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onClickRemovePlayer() {
        flpnParentToPlayerCard.getChildren().remove(playerCounter == Game.MAX_PLAYERS ? playerCounter - 1 : playerCounter);
        //playersList.remove(playerCounter - 1);

        if (playerCounter == Game.MAX_PLAYERS) {
            flpnParentToPlayerCard.getChildren().add(0, btnAddPlayerFromParent);
        }
        playerCounter--;
        lblPlayerCounterFormParent.setText(playerCounter + DELIMTER + Game.MAX_PLAYERS);
    }

    @FXML
    void onClickSubtractMatchTime(ActionEvent event) {
        if (gameTimer.validateMinTime()) { /*gameTimer.formatTime(matchMinutes, matchSeconds).equals(gameTimer.minMatchTime())*/
            return;
        }

        gameTimer.subMatchTime();
        lblMatchTime.setText(gameTimer.getTime());/*gameTimer.formatTime(matchMinutes, matchSeconds)*/

        if (gameTimer.validateMinTime()) {
            btnSubMatchTime.setOpacity(0.5);
            lblErrorForm.setText(MIN_MATCH_TIME_ERROR);
        }else {
            btnAddMatchTime.setOpacity(1);
            lblErrorForm.setText("");
        }
    }
    @FXML
    void onClickAddMatchTime(ActionEvent event) {
        if (gameTimer.validateMaxTime()) {/*gameTimer.formatTime(matchMinutes, matchSeconds).equals(gameTimer.maxMatchTime())*/
            return;
        }

        gameTimer.addMatchTime();
        lblMatchTime.setText(gameTimer.getTime());/*formatTime(matchMinutes, matchSeconds)*/

        if (gameTimer.validateMaxTime()) {
            lblErrorForm.setText(MAX_MATCH_TIME_ERROR);
            btnAddMatchTime.setOpacity(0.5);
        }else {
            lblErrorForm.setText("");
            btnSubMatchTime.setOpacity(1);
        }
    }

    @FXML
    protected void onClickAddMatch() {
        if (matchCounter >= Game.MAX_MATCHES){
            lblErrorForm.setText(MAX_MATCHES_ERROR_MSG);
            btnAddMatch.setOpacity(0.5);
            return;
        }else {
            btnSubMatch.setOpacity(1);
            lblErrorForm.setText("");
        }
        matchCounter++;

        lblMatchCounter.setText(Integer.toString(matchCounter));
    }
    @FXML
    protected void onClickSubtractMatch() {
        if (matchCounter <= Game.MIN_MATCHES){
            lblErrorForm.setText(MIN_MATCHES_ERROR_MSG);
            btnSubMatch.setOpacity(0.5);
            btnSubMatch.setOpacity(0.5);
            return;
        } else {
            btnAddMatch.setOpacity(1);
            lblErrorForm.setText("");
            lblErrorForm.setText("");
        }
        matchCounter--;

        lblMatchCounter.setText(Integer.toString(matchCounter));
    }

    private boolean playersValid() {
        //provjerava jesu sva imena unesena
        if (playerCounter < Game.MIN_PLAYERS)
        {
            lblErrorForm.setText(MIN_PLAYER_ERROR_MSG);
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
            lblPlayerCounter.setText(playerCounter + DELIMTER + Game.MAX_PLAYERS);

            lblMatchTime.setText(GameTimer.DEFAULT_MATCH_START_TIME);/*formatTime(matchMinutes, matchSeconds)*/

        } else if (url.toString().contains("playerCard.fxml") && flpnParentToPlayerCard.getChildren().stream().count() == 1) {
            lblPlayerRole.setText(PlayerRole.Hunter.toString());
            imgCharacter.setImage(new Image(Game.HUNTER_IMAGE_PATH));
            //playersList.add(new HunterPlayer(PlayerRole.Hunter, imgCharacter.getImage()));
        } else if (url.toString().contains("playerCard.fxml") && flpnParentToPlayerCard.getChildren().stream().count() > 1) {
            lblPlayerRole.setText(PlayerRole.Survivor.toString());
            imgCharacter.setImage(new Image(Game.SURVIVOR_IMAGE_PATH));
            //playersList.add(new SruvivorPlayer(PlayerRole.Survivor, imgCharacter.getImage()));
        }
        gameTimer = GameTimer.getInstance();
    }
}