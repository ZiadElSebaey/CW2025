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
        setSpacing(15);
        setMaxWidth(200);
        setPrefWidth(200);

        Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");

        restartButton = new Button("Restart");
        restartButton.getStyleClass().add("gameover-button");
        restartButton.setPrefWidth(150);

        mainMenuButton = new Button("Main Menu");
        mainMenuButton.getStyleClass().add("gameover-button");
        mainMenuButton.setPrefWidth(150);

        getChildren().addAll(gameOverLabel, restartButton, mainMenuButton);
    }

    public Button getRestartButton() {
        return restartButton;
    }

    public Button getMainMenuButton() {
        return mainMenuButton;
    }
}
