package com.comp2042.ui;

import com.comp2042.ui.GameMode;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GameOverPanel extends StackPane {

    private final Button restartButton;
    private final Button mainMenuButton;
    private final Button leaderboardButton;
    private final Button backButton1984;
    private final Button backButtonInverted;
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
    private final Label timePlayedLabel;
    private int currentScore;
    private String gameMode;
    private VBox mainContainer;
    private VBox buttonSection;
    private VBox topSection;
    private VBox middleSection;

    public GameOverPanel() {
        setMinWidth(720);
        setPrefWidth(720);
        setMaxWidth(720);
        setMinHeight(680);
        setPrefHeight(680);
        setMaxHeight(680);
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

        timePlayedLabel = new Label("");
        timePlayedLabel.getStyleClass().add("gameover-score");
        timePlayedLabel.setVisible(false);

        restartButton = new Button("Restart");
        restartButton.getStyleClass().add("gameover-button");

        mainMenuButton = new Button("Main Menu");
        mainMenuButton.getStyleClass().add("gameover-button");
        
        leaderboardButton = new Button("🏆 Leaderboard");
        leaderboardButton.getStyleClass().add("gameover-button");
        
        backButton1984 = new Button("←");
        backButton1984.getStyleClass().add("back-icon-1984");
        backButton1984.setVisible(false);
        backButton1984.setManaged(false);
        backButton1984.setPrefSize(50, 50);
        backButton1984.setLayoutX(15);
        backButton1984.setLayoutY(15);
        backButton1984.setPrefSize(40, 40);
        
        backButtonInverted = new Button("←");
        backButtonInverted.getStyleClass().add("back-icon-1984");
        backButtonInverted.setVisible(false);
        backButtonInverted.setManaged(false);
        backButtonInverted.setPrefSize(40, 40);

        Region spacer = new Region();
        spacer.setMinHeight(20);
        spacer.setPrefHeight(35);
        spacer.setMaxHeight(30);
        
        topSection = new VBox(-10);
        topSection.setAlignment(Pos.CENTER);
        topSection.getChildren().addAll(gameOverLabel, scoreLabel, timePlayedLabel, highScoreLabel, spacer, highScoreHolderLabel, newHighScoreLabel);
        
        middleSection = new VBox(8);
        middleSection.setAlignment(Pos.CENTER);
        middleSection.getChildren().addAll(enterNameLabel, nameInputBox, savedLabel, errorLabel);
        
        buttonSection = new VBox(7);
        buttonSection.setAlignment(Pos.CENTER);
        buttonSection.getChildren().addAll(restartButton, mainMenuButton, leaderboardButton);
        
        mainContainer = new VBox(-10);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.getChildren().addAll(topSection, middleSection, buttonSection);
        
        getChildren().add(mainContainer);
        StackPane.setAlignment(mainContainer, Pos.CENTER);
        
        StackPane.setAlignment(backButton1984, Pos.TOP_LEFT);
        StackPane.setMargin(backButton1984, new javafx.geometry.Insets(15, 15, 0, 0));
        getChildren().add(backButton1984);
        
        StackPane.setAlignment(backButtonInverted, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(backButtonInverted, new javafx.geometry.Insets(0, 0, 15, 15));
        getChildren().add(backButtonInverted);
    }

    public void showFinalScore(int score, int highScore, boolean isNewHighScore) {
        showFinalScore(score, highScore, isNewHighScore, null, false, 0);
    }
    
    public void showFinalScore(int score, int highScore, boolean isNewHighScore, String playerName, boolean isGuest) {
        showFinalScore(score, highScore, isNewHighScore, playerName, isGuest, 0);
    }
    
    public void showFinalScore(int score, int highScore, boolean isNewHighScore, String playerName, boolean isGuest, long timePlayed) {
        this.currentScore = score;
        
        if (playerName != null && !playerName.isEmpty()) {
            String capitalizedPlayerName = playerName.substring(0, 1).toUpperCase() + 
                                          (playerName.length() > 1 ? playerName.substring(1).toLowerCase() : "");
            scoreLabel.setText(capitalizedPlayerName + " (You): " + score);
        } else {
            scoreLabel.setText("Score: " + score);
        }
        
        if (timePlayed > 0) {
            int minutes = (int)(timePlayed / 60);
            int seconds = (int)(timePlayed % 60);
            String timeStr = String.format("Time Played: %d:%02d", minutes, seconds);
            timePlayedLabel.setText(timeStr);
            timePlayedLabel.setVisible(true);
            animateLabel(timePlayedLabel);
        } else {
            timePlayedLabel.setVisible(false);
        }
        
        boolean isLevelGame = playerName != null && playerName.equals("Level Player");
        boolean isInvertedMode = GameMode.INVERTED.matches(gameMode);
        boolean is1984Mode = GameMode.MODE_1984.matches(gameMode);
        
        if (isLevelGame || isInvertedMode || is1984Mode) {
            highScoreLabel.setVisible(false);
            highScoreHolderLabel.setVisible(false);
            newHighScoreLabel.setVisible(false);
            if (is1984Mode) {
                timePlayedLabel.setVisible(false);
            }
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
        
        if (isGuest || isLevelGame || isInvertedMode || is1984Mode) {
            enterNameLabel.setVisible(false);
            nameInputBox.setVisible(false);
            savedLabel.setVisible(false);
            errorLabel.setVisible(false);
            if (isLevelGame || isInvertedMode || is1984Mode) {
                leaderboardButton.setVisible(false);
                leaderboardButton.setManaged(false);
            }
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
        
        // Skip animation for inverted mode
        if (!GameMode.INVERTED.matches(gameMode)) {
            playGameOverAnimation();
        }
    }

    private void saveToLeaderboard() {
        String name = nameField.getText().trim();
        if (!name.isEmpty()) {
            boolean isInvertedMode = gameMode != null && gameMode.equals("inverted");
            boolean is1984Mode = gameMode != null && gameMode.equals("1984");
            
            if (!isInvertedMode && !is1984Mode) {
                LeaderboardManager.addEntry(name, currentScore);
                HighScoreManager.updateHighScore(currentScore, name);
            }
            
            hideNameInput();
            savedLabel.setVisible(true);
            errorLabel.setVisible(false);
            
            boolean isLevelGame = name != null && name.equals("Level Player");
            
            if (!isLevelGame && !isInvertedMode) {
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
    
    public Label getGameOverLabel() {
        return gameOverLabel;
    }
    
    public Label getScoreLabel() {
        return scoreLabel;
    }
    
    public Label getTimePlayedLabel() {
        return timePlayedLabel;
    }
    
    public VBox getButtonSection() {
        return buttonSection;
    }
    
    public void applyInvertedLayout() {
        // Adjust game over title position for inverted mode
        if (gameOverLabel != null) {
            // Remove center alignment constraint to allow manual positioning
            gameOverLabel.setMaxWidth(Double.MAX_VALUE);
            
            // Move title down (positive Y = down, negative Y = up)
            // Adjust this value: larger = more down, smaller/negative = more up
            gameOverLabel.setTranslateY(320);
            
            // Move title horizontally (positive X = right, negative X = left)
            // Adjust this value: 0 = center, positive = right, negative = left
            gameOverLabel.setTranslateX(-100);
            
            // Use layoutX for absolute positioning (alternative to translateX)
            // Uncomment the line below and comment translateX if translateX doesn't work
            // gameOverLabel.setLayoutX(300);
        }
        
        // Adjust score label position if needed
        if (scoreLabel != null) {
            // Remove center alignment constraint
            scoreLabel.setMaxWidth(Double.MAX_VALUE);
            
            // Move score label down (adjust to match title position)
            scoreLabel.setTranslateY(180);
            
            // Move score label horizontally (adjust to match title position)
            // Adjust this value: 0 = center, positive = right, negative = left
        scoreLabel.setTranslateX(-137);
            
            // Use layoutX for absolute positioning (alternative to translateX)
            // Uncomment the line below and comment translateX if translateX doesn't work
            // scoreLabel.setLayoutX(300);
        }
        
        if (timePlayedLabel != null) {
            timePlayedLabel.setMaxWidth(Double.MAX_VALUE);
            timePlayedLabel.setTranslateY(5);
            timePlayedLabel.setTranslateX(4);
        }
        
        // Change VBox alignment to allow manual positioning
        if (topSection != null) {
            topSection.setAlignment(Pos.TOP_LEFT);
        }
        
        // Adjust Main Menu button position for inverted mode
        if (mainMenuButton != null) {
            // Make button smaller
            mainMenuButton.setMaxWidth(150);
            mainMenuButton.setPrefWidth(150);
            
            // Move button vertically (positive Y = down, negative Y = up)
            // Adjust this value: larger = more down, smaller/negative = more up
            mainMenuButton.setTranslateY(-450);
            
            // Move button horizontally (positive X = right, negative X = left)
            // Adjust this value: 0 = center, positive = right, negative = left
            // Moved to the right
            mainMenuButton.setTranslateX(-22);
            
            // Use layoutX for absolute positioning (alternative to translateX)
            // Uncomment the line below and comment translateX if translateX doesn't work
            // mainMenuButton.setLayoutX(0);
        }
        
        // Adjust Restart button position for inverted mode
        if (restartButton != null) {
            // Make button smaller
            restartButton.setMaxWidth(150);
            restartButton.setPrefWidth(150);
            
            // Move button vertically (positive Y = down, negative Y = up)
            // Adjust this value: larger = more down, smaller/negative = more up
            restartButton.setTranslateY(-450);
            
            // Move button horizontally (positive X = right, negative X = left)
            // Adjust this value: 0 = center, positive = right, negative = left
            // Moved to the right
            restartButton.setTranslateX(-22);
            
            // Use layoutX for absolute positioning (alternative to translateX)
            // Uncomment the line below and comment translateX if translateX doesn't work
            // restartButton.setLayoutX(0);
        }
        
        // Make button section smaller
        if (buttonSection != null) {
            buttonSection.setMaxWidth(200);
            buttonSection.setPrefWidth(200);
        }
        
        // Change buttonSection alignment to allow manual positioning
        if (buttonSection != null) {
            buttonSection.setAlignment(Pos.TOP_LEFT);
        }
        
        // Show and position back button for inverted mode
        if (backButtonInverted != null) {
            backButtonInverted.setVisible(true);
            backButtonInverted.setManaged(true);
            StackPane.setAlignment(backButtonInverted, Pos.BOTTOM_RIGHT);
            // Move button more to the left to center it in the panel
            StackPane.setMargin(backButtonInverted, new Insets(0, 0, 0, 20));
            
            // Remove panel background and make button bigger
            backButtonInverted.getStyleClass().clear();
            backButtonInverted.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: white; -fx-font-size: 40px; -fx-cursor: hand;");
            backButtonInverted.setPrefSize(60, 60);
        }
    }
    
    public void resetLayout() {
        // Reset rotations and positioning - handled in GuiController
        if (gameOverLabel != null) {
            gameOverLabel.setTranslateX(0);
            gameOverLabel.setTranslateY(0);
            gameOverLabel.setLayoutX(0);
            gameOverLabel.setLayoutY(0);
        }
        if (scoreLabel != null) {
            scoreLabel.setTranslateX(0);
            scoreLabel.setTranslateY(0);
            scoreLabel.setLayoutX(0);
            scoreLabel.setLayoutY(0);
        }
        if (timePlayedLabel != null) {
            timePlayedLabel.setTranslateX(0);
            timePlayedLabel.setTranslateY(0);
            timePlayedLabel.setLayoutX(0);
            timePlayedLabel.setLayoutY(0);
        }
        if (mainMenuButton != null) {
            mainMenuButton.setTranslateX(0);
            mainMenuButton.setTranslateY(0);
            mainMenuButton.setLayoutX(0);
            mainMenuButton.setLayoutY(0);
        }
        if (restartButton != null) {
            restartButton.setTranslateX(0);
            restartButton.setTranslateY(0);
            restartButton.setLayoutX(0);
            restartButton.setLayoutY(0);
        }
        if (topSection != null) {
            topSection.setAlignment(Pos.CENTER);
        }
        if (buttonSection != null) {
            buttonSection.setAlignment(Pos.CENTER);
        }
        if (backButtonInverted != null) {
            backButtonInverted.setVisible(false);
            backButtonInverted.setManaged(false);
        }
    }
    
    public void setGameMode(String mode) {
        this.gameMode = mode;
        boolean is1984Mode = GameMode.MODE_1984.matches(mode);
        if (is1984Mode) {
            apply1984Style();
        }
    }
    
    private void apply1984Style() {
        getStyleClass().clear();
        getStyleClass().add("gameover-panel-1984");
        setBackground(javafx.scene.layout.Background.EMPTY);
        
        gameOverLabel.getStyleClass().clear();
        gameOverLabel.getStyleClass().add("gameover-title-1984");
        gameOverLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        gameOverLabel.setFont(javafx.scene.text.Font.font("Courier New", javafx.scene.text.FontWeight.BOLD, 48));
        
        scoreLabel.getStyleClass().clear();
        scoreLabel.getStyleClass().add("gameover-score-1984");
        scoreLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        scoreLabel.setFont(javafx.scene.text.Font.font("Courier New", javafx.scene.text.FontWeight.BOLD, 24));
        
        restartButton.getStyleClass().clear();
        restartButton.getStyleClass().add("gameover-button-1984");
        restartButton.setTextFill(javafx.scene.paint.Color.WHITE);
        restartButton.setFont(javafx.scene.text.Font.font("Courier New", javafx.scene.text.FontWeight.BOLD, 24));
        restartButton.setBackground(javafx.scene.layout.Background.EMPTY);
        
        mainMenuButton.getStyleClass().clear();
        mainMenuButton.getStyleClass().add("gameover-button-1984");
        mainMenuButton.setTextFill(javafx.scene.paint.Color.WHITE);
        mainMenuButton.setFont(javafx.scene.text.Font.font("Courier New", javafx.scene.text.FontWeight.BOLD, 24));
        mainMenuButton.setBackground(javafx.scene.layout.Background.EMPTY);
        
        leaderboardButton.setVisible(false);
        leaderboardButton.setManaged(false);
        
        backButton1984.setVisible(true);
        backButton1984.setManaged(true);
        backButton1984.getStyleClass().clear();
        backButton1984.getStyleClass().add("back-icon-1984");
        backButton1984.setTextFill(javafx.scene.paint.Color.WHITE);
        backButton1984.setFont(javafx.scene.text.Font.font("Courier New", javafx.scene.text.FontWeight.BOLD, 36));
        backButton1984.setBackground(javafx.scene.layout.Background.EMPTY);
        StackPane.setAlignment(backButton1984, Pos.TOP_LEFT);
        StackPane.setMargin(backButton1984, new Insets(15, 0, 0, 15));
        
        highScoreLabel.setVisible(false);
        highScoreHolderLabel.setVisible(false);
        newHighScoreLabel.setVisible(false);
        enterNameLabel.setVisible(false);
        nameInputBox.setVisible(false);
        savedLabel.setVisible(false);
        errorLabel.setVisible(false);
        timePlayedLabel.setVisible(false);
    }
    
    public Button getBackButton1984() {
        return backButton1984;
    }
    
    public Button getBackButtonInverted() {
        return backButtonInverted;
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
