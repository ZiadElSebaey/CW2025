package com.comp2042.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PausePanel extends VBox {

    private final Button resumeButton;
    private final Button restartButton;
    private final Button settingsButton;
    private final Button leaderboardButton;
    private final Button mainMenuButton;

    public PausePanel() {
        setAlignment(Pos.CENTER);
        setSpacing(18);
        getStyleClass().add("pause-panel");

        Label pauseLabel = new Label("PAUSED");
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

