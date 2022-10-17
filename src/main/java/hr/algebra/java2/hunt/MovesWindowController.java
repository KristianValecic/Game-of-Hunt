package hr.algebra.java2.hunt;

import hr.algebra.java2.model.Game;
import hr.algebra.java2.model.Move;
import hr.algebra.java2.model.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;

import java.net.URL;
import java.util.ResourceBundle;

public class MovesWindowController implements Initializable {

    private ObservableList<Move> moveList = FXCollections.observableList(Game.getMoves());

    @FXML
    private TableView<Move> moveTable;

    @FXML
    private TableColumn<Move, String> playerMovesColumn;

    @FXML
    private TableColumn<Move, String> playerNameColumn;

    @FXML
    private TableColumn<Move, String> playerRoleColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerNameColumn.setCellValueFactory(new PropertyValueFactory<Move, String>("playerName"));
        playerRoleColumn.setCellValueFactory(new PropertyValueFactory<Move, String>("playerRole"));
        playerMovesColumn.setCellValueFactory(new PropertyValueFactory<Move, String>("move"));


        moveTable.setItems(moveList);
    }
}
