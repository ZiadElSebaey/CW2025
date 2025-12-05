package com.comp2042.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Pos;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class CountdownPanel extends StackPane {
    
    private final Label countdownLabel;
    private Runnable onComplete;
    
    public CountdownPanel() {
        setAlignment(Pos.CENTER);
        setMinWidth(720);
        setMinHeight(680);
        getStyleClass().add("countdown-panel");
        
        countdownLabel = new Label();
        countdownLabel.getStyleClass().add("countdown-label");
        countdownLabel.setAlignment(Pos.CENTER);
        
        getChildren().add(countdownLabel);
    }
    
    public void startCountdown(Runnable onComplete) {
        this.onComplete = onComplete;
        countdownLabel.setVisible(true);
        countdownLabel.setOpacity(1.0);
        countdownLabel.setScaleX(1.0);
        countdownLabel.setScaleY(1.0);
        showNumber(3);
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
        countdownLabel.setScaleX(0.1);
        countdownLabel.setScaleY(0.1);
        countdownLabel.setOpacity(0.0);
        
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(200), countdownLabel);
        scaleIn.setFromX(0.1);
        scaleIn.setFromY(0.1);
        scaleIn.setToX(1.3);
        scaleIn.setToY(1.3);
        scaleIn.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
        
        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), countdownLabel);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        
        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(200), countdownLabel);
        scaleOut.setFromX(1.3);
        scaleOut.setFromY(1.3);
        scaleOut.setToX(0.8);
        scaleOut.setToY(0.8);
        scaleOut.setInterpolator(javafx.animation.Interpolator.EASE_IN);
        
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), countdownLabel);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        
        SequentialTransition sequence = new SequentialTransition(
            new javafx.animation.ParallelTransition(scaleIn, fadeIn),
            new javafx.animation.PauseTransition(Duration.millis(400)),
            new javafx.animation.ParallelTransition(scaleOut, fadeOut)
        );
        
        sequence.setOnFinished(_ -> showNumber(number - 1));
        sequence.play();
    }
}




