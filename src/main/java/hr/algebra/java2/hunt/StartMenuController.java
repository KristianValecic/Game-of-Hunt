package hr.algebra.java2.hunt;

import hr.algebra.java2.dal.GameState;
import hr.algebra.java2.message.Message;
import hr.algebra.java2.model.*;
import hr.algebra.java2.networking.ChatServiceInterface;
import hr.algebra.java2.networking.Server;
import hr.algebra.java2.thread.ClientThread;
import hr.algebra.java2.utilities.SceneUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.awt.event.InputMethodEvent;
import java.io.*;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StartMenuController implements Initializable {
    private static final String DELIMTER = "/";

    private static final String MIN_PLAYER_ERROR_MSG = "Minimum amount of players is " + Game.MIN_PLAYERS;
    private static final String MAX_MATCHES_ERROR_MSG = "Maximum amount of matches is " + Game.MAX_MATCHES;
    private static final String MIN_MATCHES_ERROR_MSG = "Minimum amount matches is " + Game.MIN_MATCHES;
    private static final String MAX_MATCH_TIME_ERROR = "Maximum match time is " + GameTimer.MAX_MATCH_TIME;
    private static final String MIN_MATCH_TIME_ERROR = "Minimum match time is " + GameTimer.MIN_MATCH_TIME;
    private static int matchCounter = 1;
    private static int playerCounter = 0;

    //removed only left here cuz too much refactoring
    private static Button btnAddPlayerFromParent;
    private static List<Player> playersList = new ArrayList<>(); //TODO: promijeni da je svaka instanca igre svoj igrac
    private static Player player;

    //removed only left here cuz too much refactoring
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
    private FlowPane flpnMainMenuPLayers;
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
    @FXML
    private Button btnSendMsg;
    @FXML
    private TextField chatMsgTextField;
    @FXML
    private TextArea chatTextArea;
    private GameTimer gameTimer;
    public static int sendStatus = 0;

    private ChatServiceInterface stub;

    public void loadOnlineGameState(GameState gameState) {
        System.out.println("Got game state!");

        matchCounter = gameState.getMatchAllCount();
        gameTimer = gameState.getGameTimer();
        clearAddedPlayers(playerCounter);
        for (Player p : gameState.getPlayersList()) {
            onClickAddPlayer();
            //imena treba dodati
        }
        refreshLblMatch();
        refreshLblMatchTime();
    }

    @FXML
    protected void onClickAddPlayer() {
        if (playerCounter == Game.MAX_PLAYERS) {
            return;
        }

        createPLayerCard();

        //Game.addAlivePlayer(player);
        if (playerCounter == Game.MAX_PLAYERS) {
            flpnMainMenuPLayers.getChildren().remove(btnAddPlayer);
        }
        lblPlayerCounter.setText(playerCounter + DELIMTER + Game.MAX_PLAYERS);
    }

    private void createPLayerCard() {
        FXMLLoader fxmlLoader = new FXMLLoader(StartMenuController.class.getResource("playerCard.fxml"));

        try {
            flpnMainMenuPLayers.getChildren().add(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startGame() {
        //provjerava je li playerCount i je li su sva imena unesena
        if (!playersValid()) {
            return;
        }

        fillPlayerList();

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

    private static void fillPlayerList() {
        for (Node node : flpnParentToPlayerCard.getChildren()) {
            if (node.getClass().equals(Pane.class)) {
                GridPane playerCard = ((GridPane) ((Pane) node)
                        .getChildren().get(0));//0 is index of gidpane in window

                String playerName = String.valueOf(((TextField) playerCard.getChildren().get(1)).getText()); // 1 is index of wanted textfield
                String playerRole = "";
                for (Node n : playerCard.getChildren()) {
                    if (n.getClass().equals(FlowPane.class)) {
                        for (Node lbl : ((FlowPane) n).getChildren()) {
                            if (lbl.getClass().equals(Label.class)) {
                                playerRole = ((Label) lbl).getText();
                            }
                        }
                    }
                }
                if (playerRole.equals(PlayerRole.Hunter.toString())) {
                    playersList.add(new HunterPlayer(playerName, PlayerRole.Hunter, new Image(Game.HUNTER_IMAGE_PATH)));
                } else {
                    playersList.add(new SurvivorPlayer(playerName, PlayerRole.Survivor, new Image(Game.SURVIVOR_IMAGE_PATH)));
                }
            }
        }
    }

    @FXML
    protected void onClickRemovePlayer(/*ActionEvent e*/) {

        removePlayerFormMenu();
//        flpnParentToPlayerCard.getChildren().removeIf(node -> {
//            Node sender =((Button)e.getSource()).getParent().getParent().getParent();
//            if (Game.HUNTER_IMAGE_PATH.equals(((ImageView)((GridPane)((Pane)sender).getChildren().get(0)).getChildren().get(0)).getImage().getUrl())){
//                return false;
//            }
//            return node.equals(sender);
//        });
        //playersList.remove(playerCounter - 1);


        // playerCounter--;
    }

    private static void removePlayerFormMenu() {
        if (playerCounter == Game.MAX_PLAYERS) {
            flpnParentToPlayerCard.getChildren().add(0, btnAddPlayerFromParent);
        }
        flpnParentToPlayerCard.getChildren().remove(playerCounter == Game.MAX_PLAYERS ? playerCounter - 1 : playerCounter);
        playerCounter--;
        lblPlayerCounterFormParent.setText(playerCounter + DELIMTER + Game.MAX_PLAYERS);
    }

    @FXML
    void onClickSubtractMatchTime(ActionEvent event) {
        if (gameTimer.validateMinTime()) { /*gameTimer.formatTime(matchMinutes, matchSeconds).equals(gameTimer.minMatchTime())*/
            return;
        }

        gameTimer.subMatchTime();
        refreshLblMatchTime();
    }

    private void refreshLblMatchTime() {
        lblMatchTime.setText(gameTimer.getTime());/*gameTimer.formatTime(matchMinutes, matchSeconds)*/

        if (gameTimer.validateMinTime()) {
            btnSubMatchTime.setOpacity(0.5);
            lblErrorForm.setText(MIN_MATCH_TIME_ERROR);
        } else {
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
        } else {
            lblErrorForm.setText("");
            btnSubMatchTime.setOpacity(1);
        }
    }

    @FXML
    protected void onClickAddMatch() {

        refreshLblMatch();

        lblMatchCounter.setText(Integer.toString(matchCounter));
    }

    private void refreshLblMatch() {
        if (matchCounter >= Game.MAX_MATCHES) {
            lblErrorForm.setText(MAX_MATCHES_ERROR_MSG);
            btnAddMatch.setOpacity(0.5);
            return;
        } else {
            btnSubMatch.setOpacity(1);
            lblErrorForm.setText("");
        }
        lblMatchCounter.setText(Integer.toString(matchCounter));
        matchCounter++;
    }

    @FXML
    protected void onClickSubtractMatch() {
        if (matchCounter <= Game.MIN_MATCHES) {
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
        if (playerCounter < Game.MIN_PLAYERS) {
            lblErrorForm.setText(MIN_PLAYER_ERROR_MSG);
            return false;
        }
        return true;
    }

    @FXML
    void onLoadGame(ActionEvent event) throws IOException, ClassNotFoundException {
        try (ObjectInputStream deserializator = new ObjectInputStream(new FileInputStream(Game.SER_FILE))) {
            GameState gameState = (GameState) deserializator.readObject();

            matchCounter = gameState.getMatchAllCount();
            gameTimer = gameState.getGameTimer();
            clearAddedPlayers(playerCounter);
            for (Player p : gameState.getPlayersList()) {
                onClickAddPlayer();
                //imena treba dodati
            }
            refreshLblMatch();
            refreshLblMatchTime();
        }
    }


    private void clearAddedPlayers(int playerCounterForLoop) {
        //flpnMainMenuPLayers.getChildren().removeIf(node -> !btnAddPlayer.equals(node));
        for (int i = 0; i < playerCounterForLoop; i++) {
            removePlayerFormMenu();
        }
        //playerCounter = 0;
    }

    @FXML
    void onSaveGame(ActionEvent event) throws IOException {
        GameState gameState = new GameState();

        //spremiti matcheve, vrijeme i igrace
        gameState.setMatchAllCount(matchCounter);
        gameState.setTimerState(gameTimer);
        //gameState.savePlayerAddedCounter(playerCounter);
        fillPlayerList();
        gameState.setPlayersList(playersList);

        try (ObjectOutputStream serializator = new ObjectOutputStream(new FileOutputStream(Game.SER_FILE)
        )) {
            serializator.writeObject(gameState);
        }
    }

    public void sendMessage()  {
        try {
            String msg = chatMsgTextField.getText();
            Message messageFromPlayer = Message.createMessageFromPlayer(player, msg);
            stub.sendMessage(messageFromPlayer);
            sendStatus = 1;
            //refreshChat();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onNameChanged(InputMethodEvent event) {
        System.out.println(tfPlayerName.getText());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameTimer = GameTimer.getInstance();

        if (url.toString().contains("startMenu.fxml")) {
            lblPlayerCounterFormParent = lblPlayerCounter;
            flpnParentToPlayerCard = flpnMainMenuPLayers;
            btnAddPlayerFromParent = btnAddPlayer;

            try {
                //client = new ClientImpl(this);
                Registry registry = LocateRegistry.getRegistry(Server.HOST, Server.PORT);
                stub = (ChatServiceInterface) registry.lookup(ChatServiceInterface.REMOTE_OBJECT_NAME);
            } catch (RemoteException | NotBoundException e) {
                throw new RuntimeException(e);
            }

            //starting client thread that get server messages
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new ClientThread(this));

            lblMatchCounter.setText(Integer.toString(matchCounter));
            lblPlayerCounter.setText(playerCounter + DELIMTER + Game.MAX_PLAYERS);

            lblMatchTime.setText(GameTimer.DEFAULT_MATCH_START_TIME);/*formatTime(matchMinutes, matchSeconds)*/

//            if (flpnParentToPlayerCard.getChildren().stream().count() == 1) {
//                player = new Player("", PlayerRole.Hunter, new Image(Game.HUNTER_IMAGE_PATH));
//            }else if (flpnParentToPlayerCard.getChildren().stream().count() > 1){
//                player = new Player("", PlayerRole.Survivor, new Image(Game.SURVIVOR_IMAGE_PATH));
//            }
            onClickAddPlayer();
        } else if (url.toString().contains("playerCard.fxml") && flpnParentToPlayerCard.getChildren().stream().count() == 0) {
            lblPlayerRole.setText(PlayerRole.Hunter.toString());
            imgCharacter.setImage(new Image(Game.HUNTER_IMAGE_PATH));
            player = new HunterPlayer("", PlayerRole.Hunter, new Image(Game.HUNTER_IMAGE_PATH));
        } else if (url.toString().contains("playerCard.fxml") && flpnParentToPlayerCard.getChildren().stream().count() >= 1) {
            lblPlayerRole.setText(PlayerRole.Survivor.toString());
            imgCharacter.setImage(new Image(Game.SURVIVOR_IMAGE_PATH));
            player = new SurvivorPlayer("", PlayerRole.Survivor, new Image(Game.SURVIVOR_IMAGE_PATH));
        }
    }

    public void refreshChat() throws RemoteException {
        StringBuilder sb = new StringBuilder();
        for (Message message:stub.getChatHistory()) {
            sb.append(message);
            sb.append("\n");
        }

        chatTextArea.setText(sb.toString());
        sendStatus = 0;
    }

}