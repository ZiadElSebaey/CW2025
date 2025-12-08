package com.comp2042.ui;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Controller for the settings screen.
 * Manages user preferences including music, sound effects, and ghost block settings.
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 */
public class SettingsController {

    private Stage stage;
    private Scene gameScene;
    private GuiController guiController;

    @FXML
    private StackPane rootPane;

    @FXML
    private Button musicButton;

    @FXML
    private Button soundButton;

    @FXML
    private Button ghostBlockButton;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setReturnToGame(Scene gameScene, GuiController guiController) {
        this.gameScene = gameScene;
        this.guiController = guiController;
    }

    @FXML
    private void initialize() {
        SettingsManager.ensureDirectoryExists();
        
        musicButton.setOnAction(_ -> toggleMusic());
        soundButton.setOnAction(_ -> toggleSound());
        ghostBlockButton.setOnAction(_ -> toggleGhostBlock());
        
        // Load current settings
        boolean ghostEnabled = SettingsManager.isGhostBlockEnabled();
        ghostBlockButton.setText(ghostEnabled ? "ON" : "OFF");
        
        boolean musicEnabled = SettingsManager.isMusicEnabled();
        musicButton.setText(musicEnabled ? "ON" : "OFF");
        
        boolean soundEffectsEnabled = SettingsManager.isSoundEffectsEnabled();
        soundButton.setText(soundEffectsEnabled ? "ON" : "OFF");
        
        AnimatedBackground animatedBackground = new AnimatedBackground(WindowConstants.WINDOW_WIDTH, WindowConstants.WINDOW_HEIGHT);
        rootPane.getChildren().addFirst(animatedBackground);
    }

    private void toggleMusic() {
        boolean newState = !SettingsManager.isMusicEnabled();
        SettingsManager.setMusicEnabled(newState);
        musicButton.setText(newState ? "ON" : "OFF");
        
        if (newState) {
            MusicManager.play();
        } else {
            MusicManager.stop();
        }
    }

    private void toggleSound() {
        boolean newState = !SettingsManager.isSoundEffectsEnabled();
        SettingsManager.setSoundEffectsEnabled(newState);
        soundButton.setText(newState ? "ON" : "OFF");
    }

    private void toggleGhostBlock() {
        boolean newState = !SettingsManager.isGhostBlockEnabled();
        SettingsManager.setGhostBlockEnabled(newState);
        ghostBlockButton.setText(newState ? "ON" : "OFF");
        
        // Update ghost block visibility in game if controller is available
        if (guiController != null) {
            guiController.updateGhostBlockVisibility();
        }
    }

    @FXML
    private void onBackClicked() {
        if (gameScene != null && guiController != null) {
            stage.setScene(gameScene);
            guiController.requestFocus();
        } else {
            SceneNavigator.navigateToScene(stage, "mainMenu.fxml", loader -> {
                MainMenuController menuController = loader.getController();
                menuController.setStage(stage);
            });
        }
    }
}

