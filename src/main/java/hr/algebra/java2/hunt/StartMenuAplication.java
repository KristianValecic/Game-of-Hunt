package hr.algebra.java2.hunt;

import hr.algebra.java2.utilities.SceneUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartMenuAplication extends Application {
    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(StartMenuAplication.class.getResource("startMenu.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        stage.setTitle("Game of Hunt");
//        stage.setScene(scene);
//        stage.show();
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