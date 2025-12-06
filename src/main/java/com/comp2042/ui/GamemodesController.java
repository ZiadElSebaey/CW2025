package com.comp2042.ui;

import com.comp2042.logic.GameController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class GamemodesController {

    @FXML
    private StackPane rootPane;
    @FXML
    private VBox nineteenEightyFourPanel;
    @FXML
    private VBox invertedPanel;
    @FXML
    private Button backButton;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        if (rootPane != null) {
            AnimatedBackground animatedBackground = new AnimatedBackground(720, 680);
            rootPane.getChildren().addFirst(animatedBackground);
        }
    }

    @FXML
    private void on1984Clicked() {
        try {
            if (stage == null) return;
            URL location = getClass().getClassLoader().getResource("tetris1984.fxml");
            if (location == null) return;
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            Parent root = fxmlLoader.load();
            Tetris1984Controller controller = fxmlLoader.getController();
            if (controller != null) {
                controller.setStage(stage);
            }
            Scene scene = new Scene(root, 720, 680);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onInvertedClicked() {
        try {
            if (stage == null) return;
            URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
            if (location == null) return;
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            Parent root = fxmlLoader.load();
            GuiController guiController = fxmlLoader.getController();
            if (guiController == null) return;
            guiController.setStage(stage);
            guiController.setGameMode("inverted");
            guiController.setLevel(null);
            guiController.setPlayerName("Inverted Player", false);
            GameController gameController = new com.comp2042.logic.GameController(guiController);
            guiController.setEventListener(gameController);
            Scene scene = new Scene(root, 720, 680);
            stage.setScene(scene);
            MainMenuController.activeGameScene = scene;
            MainMenuController.activeGuiController = guiController;
            guiController.startCountdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onBackClicked() {
        try {
            URL location = getClass().getClassLoader().getResource("mainMenu.fxml");
            if (location == null) return;
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            Parent root = fxmlLoader.load();
            MainMenuController menuController = fxmlLoader.getController();
            menuController.setStage(stage);
            menuController.showPlayMenu();
            Scene scene = new Scene(root, 720, 680);
            stage.setScene(scene);
        } catch (IOException ignored) {
        }
    }
}

