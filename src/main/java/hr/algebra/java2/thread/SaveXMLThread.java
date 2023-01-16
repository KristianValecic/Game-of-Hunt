package hr.algebra.java2.thread;

import hr.algebra.java2.dal.GameState;
import hr.algebra.java2.hunt.GameOverWindowController;
import hr.algebra.java2.model.Coordinate;
import hr.algebra.java2.model.Game;
import hr.algebra.java2.model.Move;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class SaveXMLThread implements Runnable{
    private String fileName;

    public SaveXMLThread(String fileName) {
        this.fileName = fileName;
    }
    public synchronized void saveMovesXML() {
        while (GameState.isWritingInGameState == true) {
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

            Element listRootElement = xmlDocument.createElement(GameOverWindowController.LIST_ROOT);

            xmlDocument.appendChild(listRootElement);
            //appends list nodes to root
            for (Move move : Game.getMoves()) {
                String playerName = move.getPlayerName();
                String playerRole = move.getPlayerRole();
                String moveString = move.getMove();
                Coordinate coordinate = move.getCoordinate();

                Element nodeRootElement = xmlDocument.createElement(GameOverWindowController.NODE_ROOT);

                Element playerNameElement = xmlDocument.createElement(GameOverWindowController.PLAYER_NAME);
                Node playerNameTextNode = xmlDocument.createTextNode(playerName);
                playerNameElement.appendChild(playerNameTextNode);
                nodeRootElement.appendChild(playerNameElement);

                Element playerRoleElement = xmlDocument.createElement(GameOverWindowController.PLAYER_ROLE);
                Node playerRoleTextNode = xmlDocument.createTextNode(playerRole);
                playerRoleElement.appendChild(playerRoleTextNode);
                nodeRootElement.appendChild(playerRoleElement);

                Element moveStringElement = xmlDocument.createElement(GameOverWindowController.MOVE_EVENT);
                Node moveStringTextNode = xmlDocument.createTextNode(moveString);
                moveStringElement.appendChild(moveStringTextNode);
                nodeRootElement.appendChild(moveStringElement);

                Element coordinateElement = xmlDocument.createElement(GameOverWindowController.COORDINATE);
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
    public void run() {
//        while(true) {
        saveMovesXML();
//        }
    }
}
