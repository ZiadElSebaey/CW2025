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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class GameOverPanel extends VBox {

    private final Button restartButton;
    private final Button mainMenuButton;
    private final Button leaderboardButton;
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
        setSpacing(4);
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
        
        leaderboardButton = new Button("🏆 Leaderboard");
        leaderboardButton.getStyleClass().add("gameover-button");

        Region spacer = new Region();
        spacer.setMinHeight(20);
        spacer.setPrefHeight(35);
        spacer.setMaxHeight(30);
        
        VBox topSection = new VBox(-10);
        topSection.setAlignment(Pos.CENTER);
        topSection.getChildren().addAll(gameOverLabel, scoreLabel, highScoreLabel, spacer, highScoreHolderLabel, newHighScoreLabel);
        
        VBox middleSection = new VBox(8);
        middleSection.setAlignment(Pos.CENTER);
        middleSection.getChildren().addAll(enterNameLabel, nameInputBox, savedLabel, errorLabel);
        
        VBox buttonSection = new VBox(7);
        buttonSection.setAlignment(Pos.CENTER);
        buttonSection.getChildren().addAll(restartButton, mainMenuButton, leaderboardButton);
        
        VBox mainContainer = new VBox(-10);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.getChildren().addAll(topSection, middleSection, buttonSection);
        
        getChildren().add(mainContainer);
    }

    public void showFinalScore(int score, int highScore, boolean isNewHighScore) {
        showFinalScore(score, highScore, isNewHighScore, null, false);
    }
    
    public void showFinalScore(int score, int highScore, boolean isNewHighScore, String playerName, boolean isGuest) {
        this.currentScore = score;
        
        if (playerName != null && !playerName.isEmpty()) {
            String capitalizedPlayerName = playerName.substring(0, 1).toUpperCase() + 
                                          (playerName.length() > 1 ? playerName.substring(1).toLowerCase() : "");
            scoreLabel.setText(capitalizedPlayerName + " (You): " + score);
        } else {
            scoreLabel.setText("Score: " + score);
        }
        
        boolean isLevelGame = playerName != null && playerName.equals("Level Player");
        
        if (isLevelGame) {
            highScoreLabel.setVisible(false);
            highScoreHolderLabel.setVisible(false);
            newHighScoreLabel.setVisible(false);
        } else {
            highScoreLabel.setText("High Score: " + highScore);
            highScoreLabel.setVisible(true);
            animateLabel(highScoreLabel);
            
            String highScoreHolder = HighScoreManager.getHighScoreHolder();
            if (highScoreHolder != null && !highScoreHolder.isEmpty()) {
                String capitalizedName = highScoreHolder.substring(0, 1).toUpperCase() + 
                                        (highScoreHolder.length() > 1 ? highScoreHolder.substring(1).toLowerCase() : "");
                highScoreHolderLabel.setText("by " + capitalizedName);
                highScoreHolderLabel.setVisible(true);
                animateLabel(highScoreHolderLabel);
            } else {
                highScoreHolderLabel.setVisible(false);
            }
            
            newHighScoreLabel.setVisible(isNewHighScore);
        }
        
        animateLabel(scoreLabel);
        
        if (isGuest || isLevelGame) {
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
            HighScoreManager.updateHighScore(currentScore, name);
            hideNameInput();
            savedLabel.setVisible(true);
            errorLabel.setVisible(false);
            
            boolean isLevelGame = name != null && name.equals("Level Player");
            
            if (!isLevelGame) {
                String highScoreHolder = HighScoreManager.getHighScoreHolder();
                if (highScoreHolder != null && !highScoreHolder.isEmpty()) {
                    String capitalizedName = highScoreHolder.substring(0, 1).toUpperCase() + 
                                            (highScoreHolder.length() > 1 ? highScoreHolder.substring(1).toLowerCase() : "");
                    highScoreHolderLabel.setText("by " + capitalizedName);
                    highScoreHolderLabel.setVisible(true);
                    animateLabel(highScoreHolderLabel);
                }
                
                int highScore = HighScoreManager.getHighScore();
                highScoreLabel.setText("High Score: " + highScore);
                highScoreLabel.setVisible(true);
            } else {
                highScoreLabel.setVisible(false);
                highScoreHolderLabel.setVisible(false);
            }
            
            if (name != null && !name.isEmpty()) {
                String capitalizedPlayerName = name.substring(0, 1).toUpperCase() + 
                                              (name.length() > 1 ? name.substring(1).toLowerCase() : "");
                scoreLabel.setText(capitalizedPlayerName + " (You): " + currentScore);
            }
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
    
    public Button getLeaderboardButton() {
        return leaderboardButton;
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
