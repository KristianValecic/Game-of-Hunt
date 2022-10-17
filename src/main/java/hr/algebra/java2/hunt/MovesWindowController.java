package hr.algebra.java2.hunt;

import hr.algebra.java2.model.Player;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MovesWindowController {
    @FXML
    private TableView<Player> moveTable;

    @FXML
    private TableColumn<Player, String> playerMovesColumn;

    @FXML
    private TableColumn<Player, String> playerNameColumn;

    @FXML
    private TableColumn<Player, String> playerRoleColumn;

}
