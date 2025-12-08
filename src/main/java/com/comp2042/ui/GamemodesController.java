package com.comp2042.ui;

import com.comp2042.logic.GameController;
import com.comp2042.ui.GameMode;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controller for the game modes selection screen.
 * Allows users to choose between different game modes (Normal, Inverted, 1984).
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 */
@SuppressWarnings("unused")
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
            AnimatedBackground animatedBackground = new AnimatedBackground(WindowConstants.WINDOW_WIDTH, WindowConstants.WINDOW_HEIGHT);
            rootPane.getChildren().addFirst(animatedBackground);
        }
    }

    @FXML
    private void on1984Clicked() {
        if (stage == null) return;
        SceneNavigator.navigateToScene(stage, "tetris1984.fxml", loader -> {
            Tetris1984Controller controller = loader.getController();
            if (controller != null) {
                controller.setStage(stage);
            }
        });
    }

    @FXML
    private void onInvertedClicked() {
        if (stage == null) return;
        SceneNavigator.navigateToScene(stage, "gameLayout.fxml", loader -> {
            GuiController guiController = loader.getController();
            if (guiController == null) return;
            guiController.setStage(stage);
            guiController.setGameMode(GameMode.INVERTED.getValue());
            guiController.setLevel(null);
            guiController.setPlayerName("Inverted Player", false);
            GameController gameController = new com.comp2042.logic.GameController(guiController);
            guiController.setEventListener(gameController);
            Scene scene = stage.getScene();
            if (scene != null) {
                MainMenuController.activeGameScene = scene;
            }
            MainMenuController.activeGuiController = guiController;
            guiController.startCountdown();
        });
    }

    @FXML
    private void onBackClicked() {
        SceneNavigator.navigateToScene(stage, "mainMenu.fxml", loader -> {
            MainMenuController menuController = loader.getController();
            menuController.setStage(stage);
            menuController.showPlayMenu();
        });
    }
}
