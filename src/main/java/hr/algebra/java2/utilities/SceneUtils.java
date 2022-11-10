package hr.algebra.java2.utilities;

import hr.algebra.java2.hunt.StartMenuAplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class SceneUtils {
    private SceneUtils() {
    }

    public static void createScene(Stage stage, String fxmlName, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartMenuAplication.class.getResource(fxmlName));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add("file:src/main/resources/hr/algebra/java2/hunt/Style.css");
        scene.getRoot().requestFocus();
        stage.setResizable(false);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }


    public static void showDialog(String titleTxt, String headerTxt, String contentTxt) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titleTxt);
        alert.setHeaderText(headerTxt);
        alert.setContentText(contentTxt);
        alert.show();
    }
}

