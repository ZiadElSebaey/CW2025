package com.comp2042.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Panel displayed when a level is completed successfully.
 * Shows congratulations message and provides options to proceed to the next level,
 * return to levels menu, or go to main menu.
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 */
public class LevelCompletePanel extends VBox {

    private final Button nextLevelButton;
    private final Button levelsMenuButton;
    private final Button mainMenuButton;
    private final Label congratulationsLabel;
    private final Label levelNameLabel;
    private final Label levelNumberLabel;

    public LevelCompletePanel() {
        setAlignment(Pos.CENTER);
        setSpacing(4);
        setMinWidth(720);
        setPrefWidth(720);
        setMaxWidth(720);
        getStyleClass().add("gameover-panel");

        congratulationsLabel = new Label("CONGRATULATIONS!");
        congratulationsLabel.getStyleClass().add("gameover-title");
        congratulationsLabel.setAlignment(Pos.CENTER);
        congratulationsLabel.setMaxWidth(Double.MAX_VALUE);
        congratulationsLabel.setWrapText(true);
        congratulationsLabel.setStyle("-fx-font-size: 90px;");

        levelNumberLabel = new Label("LEVEL COMPLETE");
        levelNumberLabel.getStyleClass().add("gameover-score");
        levelNumberLabel.setAlignment(Pos.CENTER);
        
        levelNameLabel = new Label("");
        levelNameLabel.getStyleClass().add("gameover-highscore");
        levelNameLabel.setAlignment(Pos.CENTER);
        levelNameLabel.setVisible(false);
        levelNameLabel.setManaged(false);

        nextLevelButton = new Button("Next Level");
        nextLevelButton.getStyleClass().add("gameover-button");

        levelsMenuButton = new Button("Levels Menu");
        levelsMenuButton.getStyleClass().add("gameover-button");
        
        mainMenuButton = new Button("Main Menu");
        mainMenuButton.getStyleClass().add("gameover-button");

        Region spacer = new Region();
        spacer.setMinHeight(20);
        spacer.setPrefHeight(35);
        spacer.setMaxHeight(30);
        
        VBox topSection = new VBox(-10);
        topSection.setAlignment(Pos.CENTER);
        topSection.getChildren().addAll(congratulationsLabel, levelNumberLabel);
        
        VBox buttonSection = new VBox(7);
        buttonSection.setAlignment(Pos.CENTER);
        buttonSection.getChildren().addAll(nextLevelButton, levelsMenuButton, mainMenuButton);
        
        VBox mainContainer = new VBox(-10);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.getChildren().addAll(topSection, buttonSection);
        
        getChildren().add(mainContainer);
    }

    public void showLevelComplete(String levelName, int levelNumber, boolean hasNextLevel) {
        levelNumberLabel.setText("LEVEL " + levelNumber + " COMPLETE!");
        
        nextLevelButton.setVisible(hasNextLevel);
        nextLevelButton.setManaged(hasNextLevel);
        
        animateLabel(congratulationsLabel);
        animateLabel(levelNumberLabel);
        
        playLevelCompleteAnimation();
    }

    private void playLevelCompleteAnimation() {
        congratulationsLabel.setScaleX(3.0);
        congratulationsLabel.setScaleY(3.0);
        congratulationsLabel.setOpacity(0);
        congratulationsLabel.setTranslateX(0);
        congratulationsLabel.setTranslateY(0);

        ScaleTransition gentleScale = new ScaleTransition(Duration.millis(600), congratulationsLabel);
        gentleScale.setFromX(3.0);
        gentleScale.setFromY(3.0);
        gentleScale.setToX(1.0);
        gentleScale.setToY(1.0);
        gentleScale.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

        FadeTransition fade = new FadeTransition(Duration.millis(600), congratulationsLabel);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

        ParallelTransition entrance = new ParallelTransition(gentleScale, fade);
        entrance.setOnFinished(_ -> {
            ScaleTransition chillPulse = new ScaleTransition(Duration.millis(2000), congratulationsLabel);
            chillPulse.setFromX(1.0);
            chillPulse.setFromY(1.0);
            chillPulse.setToX(1.08);
            chillPulse.setToY(1.08);
            chillPulse.setCycleCount(Timeline.INDEFINITE);
            chillPulse.setAutoReverse(true);
            chillPulse.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);
            
            javafx.animation.TranslateTransition chillFloat = new javafx.animation.TranslateTransition(Duration.millis(3000), congratulationsLabel);
            chillFloat.setByY(-8);
            chillFloat.setCycleCount(Timeline.INDEFINITE);
            chillFloat.setAutoReverse(true);
            chillFloat.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);
            
            FadeTransition chillGlow = new FadeTransition(Duration.millis(1500), congratulationsLabel);
            chillGlow.setFromValue(0.9);
            chillGlow.setToValue(1.0);
            chillGlow.setCycleCount(Timeline.INDEFINITE);
            chillGlow.setAutoReverse(true);
            chillGlow.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);
            
            ParallelTransition chillLoop = new ParallelTransition(chillPulse, chillFloat, chillGlow);
            chillLoop.play();
        });
        
        entrance.play();
    }

    public Button getNextLevelButton() {
        return nextLevelButton;
    }

    public Button getLevelsMenuButton() {
        return levelsMenuButton;
    }
    
    public Button getMainMenuButton() {
        return mainMenuButton;
    }
    
    private void animateLabel(Label label) {
        if (label != null && label.isVisible()) {
            ScaleTransition pulse = new ScaleTransition(Duration.seconds(1.2), label);
            pulse.setFromX(1.0);
            pulse.setFromY(1.0);
            pulse.setToX(1.15);
            pulse.setToY(1.15);
            pulse.setCycleCount(Timeline.INDEFINITE);
            pulse.setAutoReverse(true);
            pulse.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);
            
            FadeTransition glow = new FadeTransition(Duration.seconds(1.2), label);
            glow.setFromValue(0.85);
            glow.setToValue(1.0);
            glow.setCycleCount(Timeline.INDEFINITE);
            glow.setAutoReverse(true);
            glow.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);
            
            ParallelTransition combined = new ParallelTransition(pulse, glow);
            combined.play();
        }
    }
    
}

