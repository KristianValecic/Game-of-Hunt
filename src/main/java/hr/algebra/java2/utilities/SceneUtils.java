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
//        AtomicInteger refreshRate = new AtomicInteger();
//        stage.addEventHandler(WindowEvent.WINDOW_SHOWN, e -> {
//            Screen screen = Screen.getScreensForRectangle(
//                    stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight()
//            ).get(0);
//            if (screen == null)
//                return;
//            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//            GraphicsDevice d = ge.getScreenDevices()[Screen.getScreens().indexOf(screen)];
//            refreshRate.set(d.getDisplayMode().getRefreshRate()); // /!\ r is an int whereas screen refresh rate is often not an integer
//            //myAnimation.setRefreshRate(r);
//            //TODO: re-assess when window is moved to other screen
//        });
        stage.show();
        //return refreshRate.get();
    }
}

