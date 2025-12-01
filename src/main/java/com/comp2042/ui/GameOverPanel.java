package com.comp2042.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GameOverPanel extends VBox {

    private final Button restartButton;
    private final Button mainMenuButton;
    private final Label scoreLabel;
    private final Label highScoreLabel;
    private final Label newHighScoreLabel;

    public GameOverPanel() {
        setAlignment(Pos.CENTER);
        setSpacing(20);
        setMinWidth(450);
        setPrefWidth(450);
        setMaxWidth(500);
        getStyleClass().add("gameover-panel");

        Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameover-title");
        gameOverLabel.setAlignment(Pos.CENTER);

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
    }

    public Button getRestartButton() {
        return restartButton;
    }

    public Button getMainMenuButton() {
        return mainMenuButton;
    }
}
