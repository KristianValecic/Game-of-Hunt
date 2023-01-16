package hr.algebra.java2.hunt;

import hr.algebra.java2.dal.GameState;
import hr.algebra.java2.model.*;
import hr.algebra.java2.thread.LoadXMLThread;
import hr.algebra.java2.thread.SaveXMLThread;
import hr.algebra.java2.utilities.ReflectionUtils;
import hr.algebra.java2.utilities.SceneUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class GameOverWindowController implements Initializable {
    public static final String LIST_ROOT = "Moves";
    public static final String NODE_ROOT = "Move";
    public static final String PLAYER_ROLE = "playerRole";
    public static final String MOVE_EVENT = "moveString";
    public static final String PLAYER_NAME = "playerName";
    public static final String COORDINATE = "coordinate";
    private final ObservableList<Player> players = FXCollections.observableList(Game.getPlayersList());
    @FXML
    private TableColumn<Player, String> playerNameColumn;
    @FXML
    private TableColumn<Player, String> playerRoleColumn;
    @FXML
    private TableColumn<Player, Integer> playerWinScoreColumn;
    @FXML
    private TableView<Player> scoreTable;

    private final ObservableList<Move> moveObservableList = FXCollections.observableList(new ArrayList<>(Game.getMoves()));

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
        try (FileWriter fileWriter = new FileWriter(new File("documentation.html"))) {
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
                    .filter(p ->
                            (p.getFileName().toString().endsWith(".class")
                                    && !p.getFileName().toString().contains("module-info")))
                    .collect(Collectors.toList());

            for (Path path : fileList) {
                String fullQualifiedClassName = "";
                String[] paths = path.toString().split("\\\\");

                Boolean startJoining = false;

                for (String segment : paths) {
                    if ("classes".equals(segment)) {
                        startJoining = true;
                        continue;
                    }
                    if (startJoining) {
                        if (segment.endsWith(".class")) {
                            fullQualifiedClassName += segment.substring(0, segment.lastIndexOf("."));
                        } else {
                            fullQualifiedClassName += segment + ".";
                        }
                    }
                }
                if (0 != fileList.indexOf(path)) {
                    content.append("<hr>");
                }
                content.append("<h2>").append(paths[paths.length - 1]).append("</h2>");

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
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void playReplay(ActionEvent event) {
        saveReplayXML();
    }

    private void saveReplayXML() {
        try {
            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.execute(new SaveXMLThread("moves.xml"));
            executorService.execute(new LoadXMLThread("moves.xml"));

//            new Thread(() -> {
//                SaveXMLThread.saveMovesXML("moves.xml");
//            }).start();
//            new Thread(() -> {
//                List<Move> moves = loadMovesXML("moves.xml");
//            }).start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<Move> loadMovesXML(String fileName) {
        while (GameState.isWritingInGameState) {
            try {
                System.out.println("Thread tried to read in moves.xml");
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        GameState.isWritingInGameState = true;

        try {
            FileInputStream movesStream = new FileInputStream(fileName);

            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            Document xmlDocument = parser.parse(movesStream);

            String parentNodeName = xmlDocument.getDocumentElement().getNodeName();

            NodeList nodeList = xmlDocument.getElementsByTagName(NODE_ROOT);

            List<Move> moves = new ArrayList<>();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node moveNode = nodeList.item(i);

                if (moveNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element gradElement = (Element) moveNode;

                    String playerName = gradElement
                            .getElementsByTagName(PLAYER_NAME)
                            .item(0)
                            .getTextContent();

                    System.out.println("Naziv igracha: " + playerName);

                    String playerRole = gradElement
                            .getElementsByTagName(PLAYER_ROLE)
                            .item(0)
                            .getTextContent();

                    System.out.println("Role: " + playerRole);

                    String moveEvent = gradElement
                            .getElementsByTagName(MOVE_EVENT)
                            .item(0)
                            .getTextContent();

                    System.out.println("event: " + moveEvent);

                    String coordinate = gradElement
                            .getElementsByTagName(COORDINATE)
                            .item(0)
                            .getTextContent();

                    System.out.println("coordinate: " + coordinate);

                    Move newMove = new Move(
                            new Player(playerName, PlayerRole.valueOf(playerRole)),
                            moveEvent,
                            Coordinate.getCoordinateFormString(coordinate));

                    moves.add(newMove);
                }
            }
            System.out.println("Parent node name: " + parentNodeName);
            System.out.println("Reading moves.xml successsful");
            GameState.isWritingInGameState = false;
            notifyAll();

            return moves;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized void saveMovesXML(String fileName) {
        while (GameState.isWritingInGameState) {
            try {
                System.out.println("Thread tried to write in moves.xml");
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        GameState.isWritingInGameState = true;

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document xmlDocument = documentBuilder.newDocument();

            Element listRootElement = xmlDocument.createElement(LIST_ROOT);

            xmlDocument.appendChild(listRootElement);
            //appends list nodes to root
            for (Move move : Game.getMoves()) {
                String playerName = move.getPlayerName();
                String playerRole = move.getPlayerRole();
                String moveString = move.getMove();
                Coordinate coordinate = move.getCoordinate();

                Element nodeRootElement = xmlDocument.createElement(NODE_ROOT);

                Element playerNameElement = xmlDocument.createElement(PLAYER_NAME);
                Node playerNameTextNode = xmlDocument.createTextNode(playerName);
                playerNameElement.appendChild(playerNameTextNode);
                nodeRootElement.appendChild(playerNameElement);

                Element playerRoleElement = xmlDocument.createElement(PLAYER_ROLE);
                Node playerRoleTextNode = xmlDocument.createTextNode(playerRole);
                playerRoleElement.appendChild(playerRoleTextNode);
                nodeRootElement.appendChild(playerRoleElement);

                Element moveStringElement = xmlDocument.createElement(MOVE_EVENT);
                Node moveStringTextNode = xmlDocument.createTextNode(moveString);
                moveStringElement.appendChild(moveStringTextNode);
                nodeRootElement.appendChild(moveStringElement);

                Element coordinateElement = xmlDocument.createElement(COORDINATE);
                Node coordinateTextNode = xmlDocument.createTextNode(coordinate == null ? "" : coordinate.toString());
                coordinateElement.appendChild(coordinateTextNode);
                nodeRootElement.appendChild(coordinateElement);

                listRootElement.appendChild(nodeRootElement);
            }
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            Source xmlSource = new DOMSource(xmlDocument);
            Result xmlResult = new StreamResult(new File(fileName));

            transformer.transform(xmlSource, xmlResult);
        } catch (ParserConfigurationException | TransformerException e) {
            throw new RuntimeException(e);
        }

        GameState.isWritingInGameState = false;
        notifyAll();

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("XML file created!");
            alert.setHeaderText("XML file was successfuly created!");
            alert.setContentText("File 'moves.xml' was created!");
            alert.showAndWait();
        });
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerNameForMoveColumn.setCellValueFactory(new PropertyValueFactory<Move, String>("playerName"));
        playerRoleForMoveColumn.setCellValueFactory(new PropertyValueFactory<Move, String>("playerRole"));
        playerMovesColumn.setCellValueFactory(new PropertyValueFactory<Move, String>("move"));
        moveTable.setItems(moveObservableList);

        playerNameColumn.setCellValueFactory(new PropertyValueFactory<Player, String>("playerName"));
        playerRoleColumn.setCellValueFactory(new PropertyValueFactory<Player, String>("playerRole"));
        playerWinScoreColumn.setCellValueFactory(new PropertyValueFactory<Player, Integer>("gamesWon"));
        scoreTable.setItems(players);
    }
}
