package com.comp2042;

import com.comp2042.ui.FontLoader;
import com.comp2042.ui.MainMenuController;
import com.comp2042.ui.MusicManager;
import com.comp2042.ui.SceneNavigator;
import com.comp2042.ui.SettingsManager;
import com.comp2042.ui.SoundManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main entry point for the TetrisJFX application.
 * Initializes the JavaFX application, loads fonts, sets up music and sound systems,
 * and displays the main menu.
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 */
public class Main extends Application {

    /**
     * Initializes and starts the JavaFX application.
     * Sets up fonts, settings, music, and displays the main menu.
     * 
     * @param primaryStage the primary stage for the application
     * @throws Exception if initialization fails
     */
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
    
    /**
     * Called when the application is shutting down.
     * Cleans up music and sound resources.
     * 
     * @throws Exception if cleanup fails
     */
    @Override
    public void stop() throws Exception {
        MusicManager.dispose();
        SoundManager.dispose();
        super.stop();
    }

    /**
     * Main method to launch the JavaFX application.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
