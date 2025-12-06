package com.comp2042.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

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

    private boolean musicOn = true;
    private boolean soundOn = true;

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
        
        AnimatedBackground animatedBackground = new AnimatedBackground(720, 680);
        rootPane.getChildren().addFirst(animatedBackground);
    }

    private void toggleMusic() {
        musicOn = !musicOn;
        musicButton.setText(musicOn ? "ON" : "OFF");
    }

    private void toggleSound() {
        soundOn = !soundOn;
        soundButton.setText(soundOn ? "ON" : "OFF");
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
            try {
                URL location = getClass().getClassLoader().getResource("mainMenu.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader(location);
                Parent root = fxmlLoader.load();
                MainMenuController menuController = fxmlLoader.getController();
                menuController.setStage(stage);
                Scene scene = new Scene(root, 720, 680);
                stage.setScene(scene);
            } catch (IOException ignored) {
            }
        }
    }
}

