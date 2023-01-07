package hr.algebra.java2.hunt;

import hr.algebra.java2.networking.Server;
import hr.algebra.java2.thread.ClientThread;
import hr.algebra.java2.utilities.SceneUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StartMenuAplication extends Application {
    private static Stage mainStage;
    private static String name;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        StartMenuAplication.name = name;
    }

    @Override
    public void start(Stage stage) throws IOException {
        name = JOptionPane.showInputDialog("Name: ");

//        while(name.isEmpty()){
//            name = JOptionPane.showInputDialog("Ingresa el nombre con el que te deseas identificar");
//        }
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