package hr.algebra.java2.utilities;

import hr.algebra.java2.hunt.StartMenuAplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
        scene.getRoot().requestFocus();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}

