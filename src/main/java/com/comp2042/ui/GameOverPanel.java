package com.comp2042.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GameOverPanel extends VBox {

    private Button restartButton;
    private Button mainMenuButton;

    public GameOverPanel() {
        setAlignment(Pos.CENTER);
        setSpacing(30);
        setMinWidth(450);
        setPrefWidth(450);
        setMaxWidth(500);
        getStyleClass().add("gameover-panel");

        Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameover-title");
        gameOverLabel.setAlignment(Pos.CENTER);

        restartButton = new Button("Restart");
        restartButton.getStyleClass().add("gameover-button");

        mainMenuButton = new Button("Main Menu");
        mainMenuButton.getStyleClass().add("gameover-button");

        getChildren().addAll(gameOverLabel, restartButton, mainMenuButton);
    }

    public Button getRestartButton() {
        return restartButton;
    }

    public Button getMainMenuButton() {
        return mainMenuButton;
    }
}
