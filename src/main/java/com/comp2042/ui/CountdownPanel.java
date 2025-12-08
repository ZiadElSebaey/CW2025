package com.comp2042.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.geometry.Pos;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.shape.Rectangle;

public class CountdownPanel extends StackPane {
    
    private final Label countdownLabel;
    private final Rectangle scanlineOverlay;
    private Runnable onComplete;
    private boolean is1984Mode = false;
    private boolean isSlowerMode = false;
    
    public CountdownPanel() {
        setAlignment(Pos.CENTER);
        setMinWidth(720);
        setMinHeight(680);
        getStyleClass().add("countdown-panel");
        
        countdownLabel = new Label();
        countdownLabel.getStyleClass().add("countdown-label");
        countdownLabel.setAlignment(Pos.CENTER);
        
        scanlineOverlay = new Rectangle(WindowConstants.WINDOW_WIDTH, WindowConstants.WINDOW_HEIGHT);
        scanlineOverlay.setFill(Color.color(0, 0, 0, 0.1));
        scanlineOverlay.setVisible(false);
        scanlineOverlay.setMouseTransparent(true);
        
        getChildren().addAll(countdownLabel, scanlineOverlay);
    }
    
    public void set1984Mode(boolean enabled) {
        is1984Mode = enabled;
        if (enabled) {
            getStyleClass().clear();
            getStyleClass().add("countdown-panel-1984");
            setBackground(javafx.scene.layout.Background.EMPTY);
            setOpacity(1.0);
            
            countdownLabel.getStyleClass().clear();
            countdownLabel.getStyleClass().add("countdown-label-1984");
            countdownLabel.setTextFill(Color.WHITE);
            countdownLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 180));
            
            scanlineOverlay.setVisible(false);
        }
    }
    
    public void startCountdown(Runnable onComplete) {
        startCountdown(onComplete, false);
    }
    
    public void startCountdown(Runnable onComplete, boolean slower) {
        this.onComplete = onComplete;
        this.isSlowerMode = slower;
        countdownLabel.setVisible(true);
        countdownLabel.setOpacity(1.0);
        countdownLabel.setScaleX(1.0);
        countdownLabel.setScaleY(1.0);
        playCountdownSound();
        showNumber(3);
    }
    
    private void playCountdownSound() {
        SoundManager.playSound("3-2-1");
    }
    
    private void showNumber(int number) {
        if (number < 1) {
            countdownLabel.setVisible(false);
            if (onComplete != null) {
                onComplete.run();
            }
            return;
        }
        
        countdownLabel.setText(String.valueOf(number));
        
        if (is1984Mode) {
            showNumber1984(number);
        } else {
            showNumberNormal(number);
        }
    }
    
    private void showNumberNormal(int number) {
        countdownLabel.setScaleX(0.1);
        countdownLabel.setScaleY(0.1);
        countdownLabel.setOpacity(0.0);
        
        int speedMultiplier = isSlowerMode ? 2 : 1;
        
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(200 * speedMultiplier), countdownLabel);
        scaleIn.setFromX(0.1);
        scaleIn.setFromY(0.1);
        scaleIn.setToX(1.3);
        scaleIn.setToY(1.3);
        scaleIn.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
        
        FadeTransition fadeIn = new FadeTransition(Duration.millis(200 * speedMultiplier), countdownLabel);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        
        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(200 * speedMultiplier), countdownLabel);
        scaleOut.setFromX(1.3);
        scaleOut.setFromY(1.3);
        scaleOut.setToX(0.8);
        scaleOut.setToY(0.8);
        scaleOut.setInterpolator(javafx.animation.Interpolator.EASE_IN);
        
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200 * speedMultiplier), countdownLabel);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        
        SequentialTransition sequence = new SequentialTransition(
            new javafx.animation.ParallelTransition(scaleIn, fadeIn),
            new javafx.animation.PauseTransition(Duration.millis(400 * speedMultiplier)),
            new javafx.animation.ParallelTransition(scaleOut, fadeOut)
        );
        
        sequence.setOnFinished(_ -> showNumber(number - 1));
        sequence.play();
    }
    
    private void showNumber1984(int number) {
        countdownLabel.setScaleX(0.3);
        countdownLabel.setScaleY(0.3);
        countdownLabel.setOpacity(0.0);
        countdownLabel.setRotate(0);
        
        Timeline filmFlicker = new Timeline(
            new KeyFrame(Duration.millis(0), e -> {
                countdownLabel.setOpacity(0.0);
                countdownLabel.setScaleX(0.3);
                countdownLabel.setScaleY(0.3);
            }),
            new KeyFrame(Duration.millis(50), e -> {
                countdownLabel.setOpacity(0.4);
            }),
            new KeyFrame(Duration.millis(100), e -> {
                countdownLabel.setOpacity(0.7);
                countdownLabel.setScaleX(0.6);
                countdownLabel.setScaleY(0.6);
            }),
            new KeyFrame(Duration.millis(150), e -> {
                countdownLabel.setOpacity(0.9);
            }),
            new KeyFrame(Duration.millis(200), e -> {
                countdownLabel.setOpacity(1.0);
                countdownLabel.setScaleX(1.0);
                countdownLabel.setScaleY(1.0);
            })
        );
        
        Timeline holdFlicker = new Timeline(
            new KeyFrame(Duration.millis(0), e -> countdownLabel.setOpacity(1.0)),
            new KeyFrame(Duration.millis(100), e -> countdownLabel.setOpacity(0.98)),
            new KeyFrame(Duration.millis(200), e -> countdownLabel.setOpacity(1.0)),
            new KeyFrame(Duration.millis(300), e -> countdownLabel.setOpacity(0.99)),
            new KeyFrame(Duration.millis(400), e -> countdownLabel.setOpacity(1.0))
        );
        holdFlicker.setCycleCount(2);
        
        Timeline fadeOut = new Timeline(
            new KeyFrame(Duration.millis(0), e -> {
                countdownLabel.setOpacity(1.0);
                countdownLabel.setScaleX(1.0);
                countdownLabel.setScaleY(1.0);
            }),
            new KeyFrame(Duration.millis(50), e -> {
                countdownLabel.setOpacity(0.7);
                countdownLabel.setScaleX(0.8);
                countdownLabel.setScaleY(0.8);
            }),
            new KeyFrame(Duration.millis(100), e -> {
                countdownLabel.setOpacity(0.3);
                countdownLabel.setScaleX(0.5);
                countdownLabel.setScaleY(0.5);
            }),
            new KeyFrame(Duration.millis(150), e -> {
                countdownLabel.setOpacity(0.0);
                countdownLabel.setScaleX(0.2);
                countdownLabel.setScaleY(0.2);
            })
        );
        
        SequentialTransition sequence = new SequentialTransition(
            filmFlicker,
            new javafx.animation.PauseTransition(Duration.millis(200)),
            holdFlicker,
            new javafx.animation.PauseTransition(Duration.millis(200)),
            fadeOut
        );
        
        sequence.setOnFinished(_ -> {
            countdownLabel.setRotate(0);
            showNumber(number - 1);
        });
        sequence.play();
    }
}




