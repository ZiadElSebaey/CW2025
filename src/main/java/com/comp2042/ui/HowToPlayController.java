package com.comp2042.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class HowToPlayController {

    private Stage stage;

    @FXML
    private StackPane rootPane;
    
    @FXML
    private Button nextPageButton;
    
    @FXML
    private Button backButton;
    
    @FXML
    private VBox basicControlsContainer;
    
    @FXML
    private VBox gameControlsContainer;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        // Ensure next page button is visible and positioned correctly
        if (nextPageButton != null) {
            nextPageButton.setVisible(true);
            nextPageButton.setManaged(true);
            nextPageButton.toFront();
            // Position button - adjust these values to move it
            // translateX: positive = right, negative = left
            // translateY: negative = up, positive = down
            nextPageButton.setTranslateX(170);
            nextPageButton.setTranslateY(-190);
        }
        
        // Ensure back button is visible and positioned correctly
        if (backButton != null) {
            backButton.setVisible(true);
            backButton.setManaged(true);
            backButton.toFront();
            // Position button - moved very slightly right and very slightly down
            backButton.setTranslateX(-170);
            backButton.setTranslateY(45);
            // Tilt button upwards (positive rotation)
            backButton.getTransforms().removeIf(transform -> transform instanceof Rotate);
            Rotate rotate = new Rotate(15);
            backButton.getTransforms().add(rotate);
        }
        
        // Initially show Basic Controls and hide Game Controls
        if (basicControlsContainer != null) {
            basicControlsContainer.setVisible(true);
            basicControlsContainer.setManaged(true);
        }
        if (gameControlsContainer != null) {
            gameControlsContainer.setVisible(false);
            gameControlsContainer.setManaged(false);
        }
    }
    
    @FXML
    private void onNextPage(ActionEvent event) {
        // Toggle between Basic Controls and Game Controls
        if (basicControlsContainer != null && gameControlsContainer != null) {
            boolean isBasicVisible = basicControlsContainer.isVisible();
            
            if (isBasicVisible) {
                // Switch to Game Controls
                basicControlsContainer.setVisible(false);
                basicControlsContainer.setManaged(false);
                gameControlsContainer.setVisible(true);
                gameControlsContainer.setManaged(true);
            } else {
                // Switch back to Basic Controls
                gameControlsContainer.setVisible(false);
                gameControlsContainer.setManaged(false);
                basicControlsContainer.setVisible(true);
                basicControlsContainer.setManaged(true);
            }
        }
    }

    @FXML
    private void onBackClicked(ActionEvent event) {
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

