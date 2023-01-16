package hr.algebra.java2.thread;

import hr.algebra.java2.dal.GameState;
import hr.algebra.java2.hunt.GameOverWindowController;
import hr.algebra.java2.model.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadXMLThread implements Runnable {
    private boolean loadedFile = false;
    private String fileName;

    public LoadXMLThread(String fileName) {
        this.fileName = fileName;
    }

    public synchronized void loadMovesXML() {
        while (GameState.isWritingInGameState == true) {
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

            NodeList nodeList = xmlDocument.getElementsByTagName(GameOverWindowController.NODE_ROOT);

            List<Move> moves = new ArrayList<>();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node moveNode = nodeList.item(i);

                if (moveNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element gradElement = (Element) moveNode;

                    String playerName = gradElement
                            .getElementsByTagName(GameOverWindowController.PLAYER_NAME)
                            .item(0)
                            .getTextContent();

                    System.out.println("Naziv igracha: " + playerName);

                    String playerRole = gradElement
                            .getElementsByTagName(GameOverWindowController.PLAYER_ROLE)
                            .item(0)
                            .getTextContent();

                    System.out.println("Role: " + playerRole);

                    String moveEvent = gradElement
                            .getElementsByTagName(GameOverWindowController.MOVE_EVENT)
                            .item(0)
                            .getTextContent();

                    System.out.println("event: " + moveEvent);

                    String coordinate = gradElement
                            .getElementsByTagName(GameOverWindowController.COORDINATE)
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
            loadedFile = true;
            notifyAll();

            //return moves;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (!loadedFile){
            loadMovesXML();
        }
//        for (int i = 0; i < 10; i++) {
//            loadMovesXML();
//        if (loadedFile){break;}
//            try {
//                Thread.currentThread().sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
    }
}
