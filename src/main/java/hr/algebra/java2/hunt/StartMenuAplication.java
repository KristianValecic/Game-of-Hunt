package hr.algebra.java2.hunt;

import hr.algebra.java2.networking.Server;
import hr.algebra.java2.utilities.SceneUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class StartMenuAplication extends Application {
    private static Stage mainStage;
    public static int AppCounter = 0;

    @Override
    public void start(Stage stage) throws IOException {
        AppCounter++;
        try (Socket clientSocket = new Socket(Server.HOST, Server.PORT)){
            System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" +clientSocket.getPort());

            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());

//            if (ois.available() > 0){
                oos.writeObject("Client " + AppCounter + " connected to server");

                String rtrnMess = (String)ois.readObject();
                System.out.println(rtrnMess);
                rtrnMess = (String)ois.readObject();
                System.out.println(rtrnMess);
//            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Connection failed");
            e.printStackTrace();
        }
//        System.err.println("Connected successfully");

        this.mainStage = stage;
        SceneUtils.createScene(stage, "startMenu.fxml", "Game of Hunt");
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void main(String[] args) {
        launch();
    }
}