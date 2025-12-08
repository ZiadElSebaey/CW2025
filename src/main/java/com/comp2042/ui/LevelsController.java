package com.comp2042.ui;

import com.comp2042.logic.Level;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LevelsController implements Initializable {
    
    @FXML
    private GridPane levelsGrid;
    
    @FXML
    private Button backButton;
    
    @FXML
    private Button resetButton;
    
    @FXML
    private javafx.scene.layout.StackPane rootPane;
    
    private Stage stage;
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AnimatedBackground animatedBackground = new AnimatedBackground(WindowConstants.WINDOW_WIDTH, WindowConstants.WINDOW_HEIGHT);
        rootPane.getChildren().addFirst(animatedBackground);
        
        LevelProgressManager.ensureDirectoryExists();
        LevelProgressManager.initialize();
        
        List<Level> levels = LevelManager.getLevels();
        
        int col = 0;
        int row = 0;
        int colsPerRow = 2;
        
        for (Level level : levels) {
            javafx.scene.Node levelNode = createLevelButton(level);
            levelsGrid.add(levelNode, col, row);
            
            col++;
            if (col >= colsPerRow) {
                col = 0;
                row++;
            }
        }
        
        levelsGrid.setHgap(15);
        levelsGrid.setVgap(15);
        levelsGrid.setAlignment(javafx.geometry.Pos.CENTER);
        
        backButton.setOnAction(_ -> returnToMainMenu());
        resetButton.setOnAction(_ -> resetLevels());
    }
    
    private javafx.scene.Node createLevelButton(Level level) {
        javafx.scene.layout.VBox container = new javafx.scene.layout.VBox(6);
        container.setAlignment(javafx.geometry.Pos.CENTER);
        container.setPrefWidth(160);
        container.setPrefHeight(100);
        container.getStyleClass().add("level-container");
        
        javafx.scene.control.Label numberLabel = new javafx.scene.control.Label("LEVEL " + level.getLevelNumber());
        numberLabel.getStyleClass().add("level-number");
        
        javafx.scene.control.Label nameLabel = new javafx.scene.control.Label(level.getName());
        nameLabel.getStyleClass().add("level-name");
        
        javafx.scene.control.Label objectiveLabel = new javafx.scene.control.Label(level.getObjective());
        objectiveLabel.getStyleClass().add("level-objective");
        objectiveLabel.setWrapText(true);
        objectiveLabel.setMaxWidth(140);
        
        boolean unlocked = LevelManager.isLevelUnlocked(level.getLevelNumber());
        if (!unlocked) {
            container.getStyleClass().add("level-locked");
            javafx.scene.control.Label lockedLabel = new javafx.scene.control.Label("🔒 LOCKED");
            lockedLabel.getStyleClass().add("level-locked-text");
            container.getChildren().addAll(numberLabel, lockedLabel);
        } else {
            container.getStyleClass().add("level-unlocked");
            container.getChildren().addAll(numberLabel, nameLabel, objectiveLabel);
            container.setOnMouseClicked(_ -> startLevel(level));
        }
        
        return container;
    }
    
    private void startLevel(Level level) {
        SceneNavigator.navigateToScene(stage, "gameLayout.fxml", loader -> {
            GuiController gameController = loader.getController();
            gameController.setPlayerName("Level Player", true);
            gameController.setLevel(level);
            gameController.setStage(stage);
            gameController.startLevelGame();
        });
    }
    
    private void resetLevels() {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Reset Progress");
        confirmationDialog.setHeaderText(null);
        confirmationDialog.setContentText("Are you sure you want to reset all the levels?");
        
        ButtonType acceptButton = new ButtonType("Accept");
        ButtonType closeButton = new ButtonType("Close");
        confirmationDialog.getButtonTypes().setAll(acceptButton, closeButton);
        
        java.util.Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (result.isPresent() && result.get() == acceptButton) {
            LevelProgressManager.resetLevelProgress();
            refreshLevelsGrid();
        }
    }
    
    private void refreshLevelsGrid() {
        levelsGrid.getChildren().clear();
        List<Level> levels = LevelManager.getLevels();
        
        int col = 0;
        int row = 0;
        int colsPerRow = 2;
        
        for (Level level : levels) {
            javafx.scene.Node levelNode = createLevelButton(level);
            levelsGrid.add(levelNode, col, row);
            
            col++;
            if (col >= colsPerRow) {
                col = 0;
                row++;
            }
        }
    }
    
    private void returnToMainMenu() {
        SceneNavigator.navigateToScene(stage, "mainMenu.fxml", loader -> {
            MainMenuController menuController = loader.getController();
            menuController.setStage(stage);
        });
    }
}

