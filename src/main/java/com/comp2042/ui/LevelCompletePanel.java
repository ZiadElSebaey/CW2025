package com.comp2042.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Pos;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

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
        topSection.getChildren().addAll(congratulationsLabel, levelNumberLabel, spacer, levelNameLabel);
        
        VBox buttonSection = new VBox(7);
        buttonSection.setAlignment(Pos.CENTER);
        buttonSection.getChildren().addAll(nextLevelButton, levelsMenuButton, mainMenuButton);
        
        VBox mainContainer = new VBox(-10);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.getChildren().addAll(topSection, buttonSection);
        
        getChildren().add(mainContainer);
    }

    public void showLevelComplete(String levelName, int levelNumber, boolean hasNextLevel) {
        levelNameLabel.setText(levelName);
        levelNumberLabel.setText("LEVEL " + levelNumber + " COMPLETE!");
        
        nextLevelButton.setVisible(hasNextLevel);
        nextLevelButton.setManaged(hasNextLevel);
        
        animateLabel(congratulationsLabel);
        animateLabel(levelNumberLabel);
        animateLabel(levelNameLabel);
        
        playLevelCompleteAnimation();
    }

    private void playLevelCompleteAnimation() {
        congratulationsLabel.setScaleX(4.0);
        congratulationsLabel.setScaleY(4.0);
        congratulationsLabel.setOpacity(0);
        congratulationsLabel.setRotate(0);
        congratulationsLabel.setTranslateX(0);
        congratulationsLabel.setTranslateY(0);

        // Big slam entrance
        ScaleTransition slam = new ScaleTransition(Duration.millis(200), congratulationsLabel);
        slam.setFromX(4.0);
        slam.setFromY(4.0);
        slam.setToX(1.2);
        slam.setToY(1.2);

        FadeTransition fade = new FadeTransition(Duration.millis(200), congratulationsLabel);
        fade.setFromValue(0);
        fade.setToValue(1);

        ParallelTransition slamIn = new ParallelTransition(slam, fade);
        
        // Bounce back effect
        ScaleTransition bounce = new ScaleTransition(Duration.millis(150), congratulationsLabel);
        bounce.setFromX(1.2);
        bounce.setFromY(1.2);
        bounce.setToX(0.9);
        bounce.setToY(0.9);
        
        ScaleTransition bounceBack = new ScaleTransition(Duration.millis(100), congratulationsLabel);
        bounceBack.setFromX(0.9);
        bounceBack.setFromY(0.9);
        bounceBack.setToX(1.0);
        bounceBack.setToY(1.0);
        
        SequentialTransition bounceSequence = new SequentialTransition(bounce, bounceBack);
        
        slamIn.setOnFinished(_ -> {
            // Party shake with rotation
            javafx.animation.TranslateTransition shake1 = new javafx.animation.TranslateTransition(Duration.millis(30), congratulationsLabel);
            shake1.setByX(-20);
            shake1.setAutoReverse(true);
            shake1.setCycleCount(8);
            
            RotateTransition rotate1 = new RotateTransition(Duration.millis(30), congratulationsLabel);
            rotate1.setByAngle(-5);
            rotate1.setAutoReverse(true);
            rotate1.setCycleCount(8);
            
            ParallelTransition partyShake = new ParallelTransition(shake1, rotate1);
            partyShake.setOnFinished(__ -> {
                congratulationsLabel.setTranslateX(0);
                congratulationsLabel.setRotate(0);
                
                // Continuous party pulse
                ScaleTransition partyPulse = new ScaleTransition(Duration.millis(800), congratulationsLabel);
                partyPulse.setFromX(1.0);
                partyPulse.setFromY(1.0);
                partyPulse.setToX(1.25);
                partyPulse.setToY(1.25);
                partyPulse.setCycleCount(Timeline.INDEFINITE);
                partyPulse.setAutoReverse(true);
                
                RotateTransition partyRotate = new RotateTransition(Duration.millis(2000), congratulationsLabel);
                partyRotate.setByAngle(360);
                partyRotate.setCycleCount(Timeline.INDEFINITE);
                
                FadeTransition partyGlow = new FadeTransition(Duration.millis(600), congratulationsLabel);
                partyGlow.setFromValue(0.7);
                partyGlow.setToValue(1.0);
                partyGlow.setCycleCount(Timeline.INDEFINITE);
                partyGlow.setAutoReverse(true);
                
                ParallelTransition partyLoop = new ParallelTransition(partyPulse, partyRotate, partyGlow);
                partyLoop.play();
            });
            
            bounceSequence.setOnFinished(___ -> partyShake.play());
            bounceSequence.play();
        });
        
        slamIn.play();
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

