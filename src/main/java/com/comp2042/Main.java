package com.comp2042;

import com.comp2042.ui.FontLoader;
import com.comp2042.ui.MainMenuController;
import com.comp2042.ui.MusicManager;
import com.comp2042.ui.SceneNavigator;
import com.comp2042.ui.SettingsManager;
import com.comp2042.ui.SoundManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FontLoader.loadFont("FROZENLAND.otf", 12);

        SettingsManager.ensureDirectoryExists();
        MusicManager.initialize();
        
        if (SettingsManager.isMusicEnabled()) {
            MusicManager.play();
        }

        SceneNavigator.navigateToScene(primaryStage, "mainMenu.fxml", loader -> {
            MainMenuController menuController = loader.getController();
            menuController.setStage(primaryStage);
        });

        primaryStage.setTitle("TetrisJFX");
        primaryStage.show();
    }
    
    @Override
    public void stop() throws Exception {
        MusicManager.dispose();
        SoundManager.dispose();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
