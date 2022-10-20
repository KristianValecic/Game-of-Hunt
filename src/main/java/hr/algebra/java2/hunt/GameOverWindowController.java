package hr.algebra.java2.hunt;

import hr.algebra.java2.model.Game;
import hr.algebra.java2.model.Move;
import hr.algebra.java2.model.Player;
import hr.algebra.java2.utilities.SceneUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameOverWindowController implements Initializable {
    //private ObservableList<Player> players = FXCollections.observableArrayList(Game.getPlayersList());
    private ObservableList<Player> players = FXCollections.observableList(Game.getPlayersList());
    @FXML
    private TableColumn<Player, String> playerNameColumn;
    @FXML
    private TableColumn<Player, String> playerRoleColumn;
    @FXML
    private TableColumn<Player, Integer> playerWinScoreColumn;
    @FXML
    private TableView<Player> scoreTable;

    private ObservableList<Move> moveList = FXCollections.observableList(Game.getMoves());

    @FXML
    private TableView<Move> moveTable;

    @FXML
    private TableColumn<Move, String> playerMovesColumn;

    @FXML
    private TableColumn<Move, String> playerNameForMoveColumn;

    @FXML
    private TableColumn<Move, String> playerRoleForMoveColumn;

    @FXML
    void onPlayAgain(ActionEvent event) {
        try {
            Game.Rematch();
            SceneUtils.createScene(StartMenuAplication.getMainStage(), "playingField.fxml", Game.getWindowTitle());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerNameForMoveColumn.setCellValueFactory(new PropertyValueFactory<Move, String>("playerName"));
        playerRoleForMoveColumn.setCellValueFactory(new PropertyValueFactory<Move, String>("playerRole"));
        playerMovesColumn.setCellValueFactory(new PropertyValueFactory<Move, String>("move"));
        moveTable.setItems(moveList);

        playerNameColumn.setCellValueFactory(new PropertyValueFactory<Player, String>("playerName"));
        playerRoleColumn.setCellValueFactory(new PropertyValueFactory<Player, String>("playerRole"));
        playerWinScoreColumn.setCellValueFactory(new PropertyValueFactory<Player, Integer>("gamesWon"));
        scoreTable.setItems(players);
    }
}
