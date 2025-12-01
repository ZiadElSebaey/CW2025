package com.comp2042.ui;

import com.comp2042.logic.GameController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainMenuController {

    private Stage stage;

    private static Scene activeGameScene;
    private static GuiController activeGuiController;

    @FXML
    private Button resumeButton;

    @FXML
    private StackPane rootPane;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        updateResumeButtonVisibility();
        AnimatedBackground animatedBackground = new AnimatedBackground(720, 680);
        rootPane.getChildren().addFirst(animatedBackground);
    }

    private void updateResumeButtonVisibility() {
        boolean canResume = activeGuiController != null
                && activeGuiController.isPaused()
                && !activeGuiController.isGameOver();
        resumeButton.setVisible(canResume);
        resumeButton.setManaged(canResume);
    }

    @FXML
    private void onPlayClicked() {
        try {
            URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            Parent root = fxmlLoader.load();
            GuiController guiController = fxmlLoader.getController();
            guiController.setStage(stage);

            Scene gameScene = new Scene(root, 720, 680);
            activeGameScene = gameScene;
            activeGuiController = guiController;

            stage.setScene(gameScene);
            new GameController(guiController);
        } catch (IOException ignored) {
        }
    }

    @FXML
    private void onResumeClicked() {
        if (activeGameScene != null && activeGuiController != null) {
            stage.setScene(activeGameScene);
            activeGuiController.requestFocus();
        }
    }

    @FXML
    private void onHowToPlayClicked() {
        loadScene("howToPlay.fxml");
    }

    @FXML
    private void onSettingsClicked() {
        loadScene("settings.fxml");
    }

    private void loadScene(String fxmlFile) {
        try {
            URL location = getClass().getClassLoader().getResource(fxmlFile);
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            Parent root = fxmlLoader.load();
            Object controller = fxmlLoader.getController();
            if (controller instanceof HowToPlayController howToPlayController) {
                howToPlayController.setStage(stage);
            } else if (controller instanceof SettingsController settingsController) {
                settingsController.setStage(stage);
            }
            Scene scene = new Scene(root, 720, 680);
            stage.setScene(scene);
        } catch (IOException ignored) {
        }
    }

    @FXML
    private void onQuitClicked() {
        stage.close();
    }

    public static void clearActiveGame() {
        activeGameScene = null;
        activeGuiController = null;
    }
}

