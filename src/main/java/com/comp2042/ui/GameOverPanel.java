package com.comp2042.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.ParallelTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class GameOverPanel extends VBox {

    private final Button restartButton;
    private final Button mainMenuButton;
    private final Label scoreLabel;
    private final Label highScoreLabel;
    private final Label newHighScoreLabel;
    private final Label gameOverLabel;

    public GameOverPanel() {
        setAlignment(Pos.CENTER);
        setSpacing(25);
        setMinWidth(720);
        setPrefWidth(720);
        setMaxWidth(720);
        getStyleClass().add("gameover-panel");

        gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameover-title");
        gameOverLabel.setAlignment(Pos.CENTER);
        gameOverLabel.setMaxWidth(Double.MAX_VALUE);

        scoreLabel = new Label("Score: 0");
        scoreLabel.getStyleClass().add("gameover-score");
        
        highScoreLabel = new Label("High Score: 0");
        highScoreLabel.getStyleClass().add("gameover-highscore");
        
        newHighScoreLabel = new Label("NEW HIGH SCORE!");
        newHighScoreLabel.getStyleClass().add("gameover-new-highscore");
        newHighScoreLabel.setVisible(false);

        restartButton = new Button("Restart");
        restartButton.getStyleClass().add("gameover-button");

        mainMenuButton = new Button("Main Menu");
        mainMenuButton.getStyleClass().add("gameover-button");

        getChildren().addAll(gameOverLabel, scoreLabel, highScoreLabel, newHighScoreLabel, restartButton, mainMenuButton);
    }

    public void showFinalScore(int score, int highScore, boolean isNewHighScore) {
        scoreLabel.setText("Score: " + score);
        highScoreLabel.setText("High Score: " + highScore);
        newHighScoreLabel.setVisible(isNewHighScore);
        playGameOverAnimation();
    }

    private void playGameOverAnimation() {
        // SLAM DOWN effect - fast and impactful
        gameOverLabel.setScaleX(3.0);
        gameOverLabel.setScaleY(3.0);
        gameOverLabel.setOpacity(0);

        // Quick slam from big to normal
        ScaleTransition slam = new ScaleTransition(Duration.millis(150), gameOverLabel);
        slam.setFromX(3.0);
        slam.setFromY(3.0);
        slam.setToX(1.0);
        slam.setToY(1.0);

        FadeTransition fade = new FadeTransition(Duration.millis(100), gameOverLabel);
        fade.setFromValue(0);
        fade.setToValue(1);

        ParallelTransition slamIn = new ParallelTransition(slam, fade);
        slamIn.setOnFinished(_ -> {
            // Shake effect after slam
            javafx.animation.TranslateTransition shake1 = new javafx.animation.TranslateTransition(Duration.millis(50), gameOverLabel);
            shake1.setByX(-15);
            shake1.setAutoReverse(true);
            shake1.setCycleCount(6);
            shake1.setOnFinished(_ -> gameOverLabel.setTranslateX(0));
            shake1.play();
        });
        slamIn.play();
    }

    public Button getRestartButton() {
        return restartButton;
    }

    public Button getMainMenuButton() {
        return mainMenuButton;
    }
}
