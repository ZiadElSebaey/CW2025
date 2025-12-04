package com.comp2042.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;

public class LeaderboardPanel extends VBox {
    
    private final Button closeButton;
    private final Button clearButton;
    private final VBox entriesBox;
    private Runnable onClearCallback;
    
    public LeaderboardPanel() {
        setAlignment(Pos.CENTER);
        setSpacing(20);
        setMinWidth(400);
        setPrefWidth(400);
        getStyleClass().add("leaderboard-panel");
        
        Label titleLabel = new Label("🏆 LEADERBOARD 🏆");
        titleLabel.getStyleClass().add("leaderboard-title");
        
        entriesBox = new VBox(8);
        entriesBox.setAlignment(Pos.CENTER);
        entriesBox.getStyleClass().add("leaderboard-entries");
        
        clearButton = new Button("Clear");
        clearButton.getStyleClass().add("gameover-button");
        clearButton.setOnAction(_ -> clearLeaderboard());
        
        closeButton = new Button("Close");
        closeButton.getStyleClass().add("gameover-button");
        
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(clearButton, closeButton);
        
        getChildren().addAll(titleLabel, entriesBox, buttonBox);
        refreshEntries();
    }
    
    private void clearLeaderboard() {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Clear Leaderboard");
        confirmationDialog.setHeaderText(null);
        confirmationDialog.setContentText("Are you sure you want to clear all leaderboard entries and reset high score?");
        
        ButtonType acceptButton = new ButtonType("Accept");
        ButtonType closeButton = new ButtonType("Close");
        confirmationDialog.getButtonTypes().setAll(acceptButton, closeButton);
        
        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (result.isPresent() && result.get() == acceptButton) {
            LeaderboardManager.resetLeaderboard();
            HighScoreManager.resetHighScore();
            refreshEntries();
            if (onClearCallback != null) {
                onClearCallback.run();
            }
        }
    }
    
    public void refreshEntries() {
        entriesBox.getChildren().clear();
        List<LeaderboardManager.LeaderboardEntry> entries = LeaderboardManager.getEntries();
        
        if (entries.isEmpty()) {
            Label emptyLabel = new Label("No scores yet!");
            emptyLabel.getStyleClass().add("leaderboard-empty");
            entriesBox.getChildren().add(emptyLabel);
        } else {
            int rank = 1;
            for (LeaderboardManager.LeaderboardEntry entry : entries) {
                HBox row = new HBox(20);
                row.setAlignment(Pos.CENTER);
                
                Label rankLabel;
                if (rank == 1) {
                    rankLabel = new Label("🥇");
                    rankLabel.setStyle("-fx-font-size: 32px; -fx-text-fill: #FFD700; -fx-effect: dropshadow(gaussian, rgba(255, 215, 0, 0.8), 10, 0.5, 0, 0);");
                } else if (rank == 2) {
                    rankLabel = new Label("🥈");
                    rankLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: #C0C0C0; -fx-effect: dropshadow(gaussian, rgba(192, 192, 192, 0.8), 10, 0.5, 0, 0);");
                } else if (rank == 3) {
                    rankLabel = new Label("🥉");
                    rankLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: #CD7F32; -fx-effect: dropshadow(gaussian, rgba(205, 127, 50, 0.8), 10, 0.5, 0, 0);");
                } else {
                    rankLabel = new Label(rank + ".");
                    rankLabel.getStyleClass().add("leaderboard-rank");
                }
                rankLabel.setMinWidth(40);
                
                Label nameLabel = new Label(entry.name());
                nameLabel.getStyleClass().add("leaderboard-name");
                nameLabel.setMinWidth(150);
                
                Label scoreLabel = new Label(String.valueOf(entry.score()));
                scoreLabel.getStyleClass().add("leaderboard-score");
                scoreLabel.setMinWidth(80);
                
                row.getChildren().addAll(rankLabel, nameLabel, scoreLabel);
                entriesBox.getChildren().add(row);
                rank++;
            }
        }
    }
    
    public Button getCloseButton() {
        return closeButton;
    }
    
    public void setOnClearCallback(Runnable callback) {
        this.onClearCallback = callback;
    }
}

