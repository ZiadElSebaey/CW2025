package com.comp2042.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Panel displayed when the game is paused.
 * Provides options to resume, restart, access settings, view leaderboard,
 * or return to the main menu.
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 */
public class PausePanel extends VBox {

    private final Button resumeButton;
    private final Button restartButton;
    private final Button settingsButton;
    private final Button leaderboardButton;
    private final Button mainMenuButton;
    private final Label pauseLabel;

    public PausePanel() {
        setAlignment(Pos.CENTER);
        setSpacing(18);
        getStyleClass().add("pause-panel");

        pauseLabel = new Label("PAUSED");
        pauseLabel.getStyleClass().add("pause-title");

        resumeButton = new Button("Resume");
        resumeButton.getStyleClass().add("pause-button");

        restartButton = new Button("Restart");
        restartButton.getStyleClass().add("pause-button");

        settingsButton = new Button("Settings");
        settingsButton.getStyleClass().add("pause-button");

        leaderboardButton = new Button("Leaderboard");
        leaderboardButton.getStyleClass().add("pause-button");

        mainMenuButton = new Button("Main Menu");
        mainMenuButton.getStyleClass().add("pause-button");

        getChildren().addAll(pauseLabel, resumeButton, restartButton, settingsButton, leaderboardButton, mainMenuButton);
    }
    
    public void set1984Mode(boolean enabled) {
        if (enabled) {
            getStyleClass().clear();
            getStyleClass().add("pause-panel-1984");
            setBackground(javafx.scene.layout.Background.EMPTY);
            
            pauseLabel.getStyleClass().clear();
            pauseLabel.getStyleClass().add("pause-title-1984");
            pauseLabel.setTextFill(Color.WHITE);
            pauseLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 48));
            
            leaderboardButton.setVisible(false);
            leaderboardButton.setManaged(false);
            
            resumeButton.getStyleClass().clear();
            resumeButton.getStyleClass().add("pause-button-1984");
            resumeButton.setTextFill(Color.WHITE);
            resumeButton.setFont(Font.font("Courier New", FontWeight.BOLD, 24));
            resumeButton.setBackground(javafx.scene.layout.Background.EMPTY);
            
            restartButton.getStyleClass().clear();
            restartButton.getStyleClass().add("pause-button-1984");
            restartButton.setTextFill(Color.WHITE);
            restartButton.setFont(Font.font("Courier New", FontWeight.BOLD, 24));
            restartButton.setBackground(javafx.scene.layout.Background.EMPTY);
            
            settingsButton.getStyleClass().clear();
            settingsButton.getStyleClass().add("pause-button-1984");
            settingsButton.setTextFill(Color.WHITE);
            settingsButton.setFont(Font.font("Courier New", FontWeight.BOLD, 24));
            settingsButton.setBackground(javafx.scene.layout.Background.EMPTY);
            
            mainMenuButton.getStyleClass().clear();
            mainMenuButton.getStyleClass().add("pause-button-1984");
            mainMenuButton.setTextFill(Color.WHITE);
            mainMenuButton.setFont(Font.font("Courier New", FontWeight.BOLD, 24));
            mainMenuButton.setBackground(javafx.scene.layout.Background.EMPTY);
        }
    }

    public Button getResumeButton() {
        return resumeButton;
    }

    public Button getRestartButton() {
        return restartButton;
    }

    public Button getSettingsButton() {
        return settingsButton;
    }

    public Button getLeaderboardButton() {
        return leaderboardButton;
    }

    public Button getMainMenuButton() {
        return mainMenuButton;
    }
}

