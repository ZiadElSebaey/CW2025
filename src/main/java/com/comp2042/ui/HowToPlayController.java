package com.comp2042.ui;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;


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
    
    @FXML
    private VBox creatorPanel;
    
    @FXML
    private StackPane dialogueContainer;
    
    @FXML
    private javafx.scene.image.ImageView dialogueImage;
    
    @FXML
    private javafx.scene.control.Label dialogueText;
    
    private Timeline dialogueTimeline;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        // Ensure next page button is visible and positioned correctly
        if (nextPageButton != null) {
            nextPageButton.setVisible(true);
            nextPageButton.setManaged(true);
            nextPageButton.setMouseTransparent(false);
            nextPageButton.setPickOnBounds(true);
            nextPageButton.setOpacity(1.0);
            nextPageButton.setText("▶");
            nextPageButton.setStyle("-fx-text-fill: #FFD700; -fx-opacity: 1.0; -fx-font-size: 60px; -fx-font-weight: bold; -fx-font-family: 'Frozenland';");
            nextPageButton.toFront();
            // Position button - adjust these values to move it
            // translateX: positive = right, negative = left
            // translateY: negative = up, positive = down
            nextPageButton.setTranslateX(170);
            nextPageButton.setTranslateY(-190);
            
            Platform.runLater(() -> {
                nextPageButton.setStyle("-fx-text-fill: #FFD700; -fx-opacity: 1.0; -fx-font-size: 60px; -fx-font-weight: bold; -fx-font-family: 'Frozenland';");
                nextPageButton.setText("▶");
                nextPageButton.setOpacity(1.0);
                nextPageButton.toFront();
            });
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
        
        if (creatorPanel != null) {
            TranslateTransition floatUp = new TranslateTransition(Duration.seconds(2), creatorPanel);
            floatUp.setByY(-10);
            floatUp.setCycleCount(Timeline.INDEFINITE);
            floatUp.setAutoReverse(true);
            floatUp.play();
        }
        
        if (dialogueContainer != null) {
            dialogueContainer.setVisible(true);
            dialogueContainer.setManaged(true);
            dialogueContainer.toFront();
        }
        
        startDialogueSequence();
        
        Platform.runLater(() -> {
            if (nextPageButton != null) {
                nextPageButton.toFront();
            }
        });
    }
    
    private void startDialogueSequence() {
        if (dialogueText == null) {
            return;
        }
        
        dialogueText.setOpacity(1.0);
        dialogueText.setText("On this very machine, I taught the blocks how to move. Now I'll teach you.");
        
        dialogueTimeline = new Timeline(
            new KeyFrame(Duration.seconds(4.0), e -> {
                if (dialogueText != null && dialogueText.getScene() != null) {
                    FadeTransition fadeOut = new FadeTransition(Duration.millis(500), dialogueText);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);
                    fadeOut.setOnFinished(event -> {
                        dialogueText.setText("Press > on my device to see next page");
                        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), dialogueText);
                        fadeIn.setFromValue(0.0);
                        fadeIn.setToValue(1.0);
                        fadeIn.play();
                    });
                    fadeOut.play();
                }
            }),
            new KeyFrame(Duration.seconds(8.0), e -> {
                if (dialogueText != null && dialogueText.getScene() != null) {
                    FadeTransition fadeOut = new FadeTransition(Duration.millis(500), dialogueText);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);
                    fadeOut.setOnFinished(event -> {
                        dialogueText.setText("Press ESC on my keyboard to go back to Main Menu");
                        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), dialogueText);
                        fadeIn.setFromValue(0.0);
                        fadeIn.setToValue(1.0);
                        fadeIn.play();
                    });
                    fadeOut.play();
                }
            }),
            new KeyFrame(Duration.seconds(12.0), e -> {
                if (dialogueText != null && dialogueText.getScene() != null) {
                    FadeTransition fadeOut = new FadeTransition(Duration.millis(500), dialogueText);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);
                    fadeOut.setOnFinished(event -> {
                        dialogueText.setText("On this very machine, I taught the blocks how to move. Now I'll teach you.");
                        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), dialogueText);
                        fadeIn.setFromValue(0.0);
                        fadeIn.setToValue(1.0);
                        fadeIn.play();
                    });
                    fadeOut.play();
                }
            })
        );
        dialogueTimeline.setCycleCount(Timeline.INDEFINITE);
        dialogueTimeline.play();
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
        SceneNavigator.navigateToScene(stage, "mainMenu.fxml", loader -> {
            MainMenuController menuController = loader.getController();
            menuController.setStage(stage);
        });
    }
}

