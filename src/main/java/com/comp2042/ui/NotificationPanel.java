package com.comp2042.ui;

import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class NotificationPanel extends BorderPane {

    private static final int MIN_HEIGHT = 200;
    private static final int MIN_WIDTH = 220;

    private final int scoreValue;
    
    public NotificationPanel(String text) {
        setMinHeight(MIN_HEIGHT);
        setMinWidth(MIN_WIDTH);
        String scoreStr = text.replace("+", "").trim();
        int parsedScore;
        try {
            parsedScore = Integer.parseInt(scoreStr);
        } catch (NumberFormatException e) {
            parsedScore = 0;
        }
        this.scoreValue = parsedScore;
        Label scoreLabel = new Label(text);
        scoreLabel.getStyleClass().add("bonusStyle");
        scoreLabel.setEffect(new Glow(1.0));
        if (scoreValue >= 200) {
            scoreLabel.setTextFill(Color.rgb(0, 255, 255));
        } else {
            scoreLabel.setTextFill(Color.rgb(200, 100, 255));
        }
        setCenter(scoreLabel);
    }

    public void showScore(ObservableList<Node> list) {
        Label scoreLabel = (Label) getCenter();
        Glow glowEffect = new Glow(1.0);
        scoreLabel.setEffect(glowEffect);
        
        if (scoreValue >= 200) {
            playCrazyAnimation(list, glowEffect, scoreLabel);
        } else {
            playNormalAnimation(list, glowEffect);
        }
    }
    
    private void playNormalAnimation(ObservableList<Node> list, Glow glowEffect) {
        if (scoreValue == 50) {
            playChillShakeAnimation(list, glowEffect);
            return;
        }
        
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(40), this);
        scaleIn.setFromX(0.3);
        scaleIn.setFromY(0.3);
        scaleIn.setToX(1.2);
        scaleIn.setToY(1.2);
        scaleIn.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
        
        ScaleTransition scaleBounce = new ScaleTransition(Duration.millis(30), this);
        scaleBounce.setFromX(1.2);
        scaleBounce.setFromY(1.2);
        scaleBounce.setToX(1.0);
        scaleBounce.setToY(1.0);
        scaleBounce.setInterpolator(javafx.animation.Interpolator.EASE_IN);
        
        javafx.animation.Timeline glowPulse = new javafx.animation.Timeline();
        glowPulse.getKeyFrames().addAll(
            new javafx.animation.KeyFrame(Duration.ZERO, e -> glowEffect.setLevel(1.0)),
            new javafx.animation.KeyFrame(Duration.millis(30), e -> glowEffect.setLevel(0.6)),
            new javafx.animation.KeyFrame(Duration.millis(60), e -> glowEffect.setLevel(1.0))
        );
        glowPulse.setCycleCount(5);
        glowPulse.setDelay(Duration.millis(70));
        
        TranslateTransition shakeX = new TranslateTransition(Duration.millis(25), this);
        shakeX.setFromX(0);
        shakeX.setToX(12);
        shakeX.setCycleCount(6);
        shakeX.setAutoReverse(true);
        shakeX.setDelay(Duration.millis(70));
        
        TranslateTransition shakeY = new TranslateTransition(Duration.millis(27), this);
        shakeY.setFromY(0);
        shakeY.setToY(8);
        shakeY.setCycleCount(5);
        shakeY.setAutoReverse(true);
        shakeY.setDelay(Duration.millis(70));
        
        ParallelTransition entrance = new ParallelTransition(scaleIn);
        ParallelTransition shake = new ParallelTransition(shakeX, shakeY, glowPulse);
        SequentialTransition fullAnimation = new SequentialTransition(entrance, scaleBounce, shake);
        fullAnimation.setOnFinished(event -> list.remove(this));
        fullAnimation.play();
    }
    
    private void playChillShakeAnimation(ObservableList<Node> list, Glow glowEffect) {
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(60), this);
        scaleIn.setFromX(0.5);
        scaleIn.setFromY(0.5);
        scaleIn.setToX(1.0);
        scaleIn.setToY(1.0);
        scaleIn.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
        
        javafx.animation.Timeline glowPulse = new javafx.animation.Timeline();
        glowPulse.getKeyFrames().addAll(
            new javafx.animation.KeyFrame(Duration.ZERO, e -> glowEffect.setLevel(0.8)),
            new javafx.animation.KeyFrame(Duration.millis(80), e -> glowEffect.setLevel(0.5)),
            new javafx.animation.KeyFrame(Duration.millis(160), e -> glowEffect.setLevel(0.8))
        );
        glowPulse.setCycleCount(4);
        glowPulse.setDelay(Duration.millis(60));
        
        TranslateTransition shakeX = new TranslateTransition(Duration.millis(50), this);
        shakeX.setFromX(0);
        shakeX.setToX(6);
        shakeX.setCycleCount(8);
        shakeX.setAutoReverse(true);
        shakeX.setDelay(Duration.millis(60));
        shakeX.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);
        
        TranslateTransition shakeY = new TranslateTransition(Duration.millis(55), this);
        shakeY.setFromY(0);
        shakeY.setToY(4);
        shakeY.setCycleCount(7);
        shakeY.setAutoReverse(true);
        shakeY.setDelay(Duration.millis(60));
        shakeY.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);
        
        ParallelTransition entrance = new ParallelTransition(scaleIn);
        ParallelTransition shake = new ParallelTransition(shakeX, shakeY, glowPulse);
        SequentialTransition fullAnimation = new SequentialTransition(entrance, shake);
        fullAnimation.setOnFinished(event -> list.remove(this));
        fullAnimation.play();
    }
    
    private void playCrazyAnimation(ObservableList<Node> list, Glow glowEffect, Label scoreLabel) {
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(30), this);
        scaleIn.setFromX(0.0);
        scaleIn.setFromY(0.0);
        scaleIn.setToX(1.8);
        scaleIn.setToY(1.8);
        scaleIn.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
        
        ScaleTransition scaleBounce1 = new ScaleTransition(Duration.millis(25), this);
        scaleBounce1.setFromX(1.8);
        scaleBounce1.setFromY(1.8);
        scaleBounce1.setToX(0.9);
        scaleBounce1.setToY(0.9);
        scaleBounce1.setInterpolator(javafx.animation.Interpolator.EASE_IN);
        
        ScaleTransition scaleBounce2 = new ScaleTransition(Duration.millis(20), this);
        scaleBounce2.setFromX(0.9);
        scaleBounce2.setFromY(0.9);
        scaleBounce2.setToX(1.3);
        scaleBounce2.setToY(1.3);
        scaleBounce2.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
        
        ScaleTransition scaleBounce3 = new ScaleTransition(Duration.millis(15), this);
        scaleBounce3.setFromX(1.3);
        scaleBounce3.setFromY(1.3);
        scaleBounce3.setToX(1.0);
        scaleBounce3.setToY(1.0);
        scaleBounce3.setInterpolator(javafx.animation.Interpolator.EASE_IN);
        
        javafx.animation.Timeline colorFlash = new javafx.animation.Timeline();
        colorFlash.getKeyFrames().addAll(
            new javafx.animation.KeyFrame(Duration.ZERO, e -> scoreLabel.setTextFill(Color.rgb(0, 255, 255))),
            new javafx.animation.KeyFrame(Duration.millis(35), e -> scoreLabel.setTextFill(Color.rgb(255, 255, 0))),
            new javafx.animation.KeyFrame(Duration.millis(70), e -> scoreLabel.setTextFill(Color.rgb(0, 255, 255))),
            new javafx.animation.KeyFrame(Duration.millis(105), e -> scoreLabel.setTextFill(Color.rgb(255, 255, 0))),
            new javafx.animation.KeyFrame(Duration.millis(140), e -> scoreLabel.setTextFill(Color.rgb(0, 255, 255)))
        );
        colorFlash.setCycleCount(6);
        colorFlash.setDelay(Duration.millis(90));
        
        javafx.animation.Timeline glowPulse = new javafx.animation.Timeline();
        glowPulse.getKeyFrames().addAll(
            new javafx.animation.KeyFrame(Duration.ZERO, e -> glowEffect.setLevel(1.0)),
            new javafx.animation.KeyFrame(Duration.millis(25), e -> glowEffect.setLevel(0.3)),
            new javafx.animation.KeyFrame(Duration.millis(50), e -> glowEffect.setLevel(1.0)),
            new javafx.animation.KeyFrame(Duration.millis(75), e -> glowEffect.setLevel(0.3)),
            new javafx.animation.KeyFrame(Duration.millis(100), e -> glowEffect.setLevel(1.0))
        );
        glowPulse.setCycleCount(7);
        glowPulse.setDelay(Duration.millis(90));
        
        TranslateTransition shakeX = new TranslateTransition(Duration.millis(22), this);
        shakeX.setFromX(0);
        shakeX.setToX(20);
        shakeX.setCycleCount(10);
        shakeX.setAutoReverse(true);
        shakeX.setDelay(Duration.millis(90));
        
        TranslateTransition shakeY = new TranslateTransition(Duration.millis(24), this);
        shakeY.setFromY(0);
        shakeY.setToY(15);
        shakeY.setCycleCount(9);
        shakeY.setAutoReverse(true);
        shakeY.setDelay(Duration.millis(90));
        
        RotateTransition shakeRotate = new RotateTransition(Duration.millis(20), this);
        shakeRotate.setFromAngle(-12);
        shakeRotate.setToAngle(12);
        shakeRotate.setCycleCount(11);
        shakeRotate.setAutoReverse(true);
        shakeRotate.setDelay(Duration.millis(90));
        
        ParallelTransition entrance = new ParallelTransition(scaleIn);
        SequentialTransition bounce = new SequentialTransition(scaleBounce1, scaleBounce2, scaleBounce3);
        ParallelTransition shake = new ParallelTransition(shakeX, shakeY, shakeRotate, glowPulse, colorFlash);
        SequentialTransition fullAnimation = new SequentialTransition(entrance, bounce, shake);
        fullAnimation.setOnFinished(event -> list.remove(this));
        fullAnimation.play();
    }
}
