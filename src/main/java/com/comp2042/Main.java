package com.comp2042;

import com.comp2042.ui.MainMenuController;
import com.comp2042.ui.MusicManager;
import com.comp2042.ui.SettingsManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        InputStream fontStream = getClass().getClassLoader().getResourceAsStream("FROZENLAND.otf");
        if (fontStream != null) {
            Font.loadFont(fontStream, 12);
        }

        SettingsManager.ensureDirectoryExists();
        MusicManager.initialize();
        
        if (SettingsManager.isMusicEnabled()) {
            MusicManager.play();
        }

        URL location = getClass().getClassLoader().getResource("mainMenu.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();
        MainMenuController menuController = fxmlLoader.getController();
        menuController.setStage(primaryStage);

        primaryStage.setTitle("TetrisJFX");
        Scene scene = new Scene(root, 720, 680);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    @Override
    public void stop() throws Exception {
        MusicManager.dispose();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
