package com.comp2042.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class GameOverPanel extends VBox {

    private final Button restartButton;
    private final Button mainMenuButton;
    private final Label scoreLabel;
    private final Label highScoreLabel;
    private final Label highScoreHolderLabel;
    private final Label newHighScoreLabel;
    private final Label gameOverLabel;
    private final Label enterNameLabel;
    private final HBox nameInputBox;
    private final TextField nameField;
    private final Button submitNameButton;
    private final Button skipButton;
    private final Label savedLabel;
    private final Label errorLabel;
    private int currentScore;

    public GameOverPanel() {
        setAlignment(Pos.CENTER);
        setSpacing(8);
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
        
        highScoreHolderLabel = new Label("");
        highScoreHolderLabel.getStyleClass().add("gameover-highscore-holder");
        
        newHighScoreLabel = new Label("NEW HIGH SCORE!");
        newHighScoreLabel.getStyleClass().add("gameover-new-highscore");
        newHighScoreLabel.setVisible(false);

        enterNameLabel = new Label("Enter name for leaderboard:");
        enterNameLabel.getStyleClass().add("leaderboard-prompt");
        
        nameField = new TextField();
        nameField.setPromptText("Your name");
        nameField.setMaxWidth(200);
        nameField.getStyleClass().add("name-input");
        nameField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                saveToLeaderboard();
            }
        });
        
        submitNameButton = new Button("Save");
        submitNameButton.getStyleClass().add("small-button");
        submitNameButton.setOnAction(_ -> saveToLeaderboard());
        
        skipButton = new Button("Skip");
        skipButton.getStyleClass().add("small-button");
        skipButton.setOnAction(_ -> hideNameInput());
        
        HBox buttonRow = new HBox(10, submitNameButton, skipButton);
        buttonRow.setAlignment(Pos.CENTER);
        
        nameInputBox = new HBox(15, nameField, buttonRow);
        nameInputBox.setAlignment(Pos.CENTER);
        
        savedLabel = new Label("✓ Saved to leaderboard!");
        savedLabel.getStyleClass().add("saved-label");
        savedLabel.setVisible(false);

        errorLabel = new Label("✗ Please enter a name!");
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setVisible(false);

        restartButton = new Button("Restart");
        restartButton.getStyleClass().add("gameover-button");

        mainMenuButton = new Button("Main Menu");
        mainMenuButton.getStyleClass().add("gameover-button");

        getChildren().addAll(gameOverLabel, scoreLabel, highScoreLabel, highScoreHolderLabel, newHighScoreLabel, 
                            enterNameLabel, nameInputBox, savedLabel, errorLabel, restartButton, mainMenuButton);
    }

    public void showFinalScore(int score, int highScore, boolean isNewHighScore) {
        showFinalScore(score, highScore, isNewHighScore, null, false);
    }
    
    public void showFinalScore(int score, int highScore, boolean isNewHighScore, String playerName, boolean isGuest) {
        this.currentScore = score;
        scoreLabel.setText("Score: " + score);
        highScoreLabel.setText("High Score: " + highScore);
        
        String highScoreHolder = HighScoreManager.getHighScoreHolder();
        if (highScoreHolder != null && !highScoreHolder.isEmpty()) {
            String capitalizedName = highScoreHolder.substring(0, 1).toUpperCase() + 
                                    (highScoreHolder.length() > 1 ? highScoreHolder.substring(1).toLowerCase() : "");
            highScoreHolderLabel.setText(capitalizedName);
            highScoreHolderLabel.setVisible(true);
            animateHighScoreHolder();
        } else {
            highScoreHolderLabel.setVisible(false);
        }
        
        newHighScoreLabel.setVisible(isNewHighScore);
        
        if (isGuest) {
            enterNameLabel.setVisible(false);
            nameInputBox.setVisible(false);
            savedLabel.setVisible(false);
            errorLabel.setVisible(false);
        } else if (playerName != null && !playerName.isEmpty()) {
            LeaderboardManager.addEntry(playerName, score);
            enterNameLabel.setVisible(false);
            nameInputBox.setVisible(false);
            savedLabel.setVisible(true);
            errorLabel.setVisible(false);
        } else {
            enterNameLabel.setVisible(true);
            nameInputBox.setVisible(true);
            nameField.clear();
            savedLabel.setVisible(false);
            errorLabel.setVisible(false);
        }
        
        playGameOverAnimation();
    }

    private void saveToLeaderboard() {
        String name = nameField.getText().trim();
        if (!name.isEmpty()) {
            LeaderboardManager.addEntry(name, currentScore);
            hideNameInput();
            savedLabel.setVisible(true);
            errorLabel.setVisible(false);
        } else {
            errorLabel.setVisible(true);
            savedLabel.setVisible(false);
        }
    }

    private void hideNameInput() {
        nameInputBox.setVisible(false);
    }

    private void playGameOverAnimation() {
        gameOverLabel.setScaleX(3.0);
        gameOverLabel.setScaleY(3.0);
        gameOverLabel.setOpacity(0);

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
    
    private void animateHighScoreHolder() {
        if (highScoreHolderLabel != null && highScoreHolderLabel.isVisible()) {
            ScaleTransition pulse = new ScaleTransition(Duration.seconds(1.2), highScoreHolderLabel);
            pulse.setFromX(1.0);
            pulse.setFromY(1.0);
            pulse.setToX(1.15);
            pulse.setToY(1.15);
            pulse.setCycleCount(Timeline.INDEFINITE);
            pulse.setAutoReverse(true);
            pulse.play();
            
            FadeTransition glow = new FadeTransition(Duration.seconds(1.2), highScoreHolderLabel);
            glow.setFromValue(0.7);
            glow.setToValue(1.0);
            glow.setCycleCount(Timeline.INDEFINITE);
            glow.setAutoReverse(true);
            glow.play();
        }
    }
}
