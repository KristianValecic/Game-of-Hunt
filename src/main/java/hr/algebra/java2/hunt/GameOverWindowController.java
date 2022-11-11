package hr.algebra.java2.hunt;

import hr.algebra.java2.model.Game;
import hr.algebra.java2.model.Move;
import hr.algebra.java2.model.Player;
import hr.algebra.java2.utilities.ReflectionUtils;
import hr.algebra.java2.utilities.SceneUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.StandardSocketOptions;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GameOverWindowController implements Initializable {
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

    @FXML
    void onCreateDocumentation(ActionEvent event) {
        try (FileWriter fileWriter = new FileWriter(new File("documentation.html"))){
            StringBuilder content = new StringBuilder();
            content.append(" <!DOCTYPE html>\n" +
                            "<link rel=\"stylesheet\" href=\"src\\main\\resources\\hr\\algebra\\java2\\hunt\\StyleDocumentation.css\">" +
                            "<html>\n" +
                            "<head>\n" +
                            "<title>Project documentation</title>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "<h1>" +
                            "Dokumentacija Game of hunt projekta\n" +
                            "</h1>\n");

            List<Path> fileList = Files.walk(Path.of("."))
                    .filter(p->
                            (p.getFileName().toString().endsWith(".class")
                            && !p.getFileName().toString().contains("module-info")))
                    .collect(Collectors.toList());

            for (Path path : fileList){
                String fullQualifiedClassName = "";
                String[] paths = path.toString().split("\\\\");

                Boolean startJoining = false;

                for(String segment : paths){
                    if ("classes".equals(segment)){
                        startJoining = true;
                        continue;
                    }
                    if(startJoining) {
                        if (segment.endsWith(".class")) {
                            fullQualifiedClassName += segment.substring(0, segment.lastIndexOf("."));
                        } else {
                            fullQualifiedClassName += segment + ".";
                        }
                    }
                }
                if (0 != fileList.indexOf(path)){
                    content.append("<hr>");
                }
                content.append("<h2>").append(paths[paths.length-1]).append("</h2>");

                Class<?> clazz = Class.forName(fullQualifiedClassName);
                content.append("<p>").append("<span>");
                ReflectionUtils.readClassAndMembersInfo(clazz, content);
                content.append("</span>").append("</p>");
            }
            //System.out.println(content);

            content.append("</body>\n" +
                            "</html> ");
            fileWriter.write(content.toString());
            SceneUtils.showDialog("Documentation created", "Documentation successfully created", "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
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
