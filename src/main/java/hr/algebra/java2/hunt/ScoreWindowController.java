package hr.algebra.java2.hunt;

import hr.algebra.java2.model.Game;
import hr.algebra.java2.model.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class ScoreWindowController implements Initializable {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerNameColumn.setCellValueFactory(new PropertyValueFactory<Player, String>("playerName"));
        playerRoleColumn.setCellValueFactory(new PropertyValueFactory<Player, String>("playerRole"));
        playerWinScoreColumn.setCellValueFactory(new PropertyValueFactory<Player, Integer>("gamesWon"));

        scoreTable.setItems(players);
    }
}
