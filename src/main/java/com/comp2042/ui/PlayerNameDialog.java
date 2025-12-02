package com.comp2042.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PlayerNameDialog {
    
    private String playerName;
    private boolean isGuest;
    
    public PlayerNameDialog(Stage parentStage) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(parentStage);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setTitle("Enter Player Name");
        
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: rgba(20, 40, 60, 0.95); " +
                     "-fx-background-radius: 15; " +
                     "-fx-border-color: rgba(150, 220, 255, 0.5); " +
                     "-fx-border-width: 2; " +
                     "-fx-border-radius: 15;");
        
        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(30));
        
        Button backButton = new Button("✕");
        backButton.setStyle("-fx-font-size: 20px; " +
                           "-fx-font-weight: bold; " +
                           "-fx-background-color: rgba(180, 60, 60, 0.7); " +
                           "-fx-text-fill: #ffffff; " +
                           "-fx-min-width: 35px; " +
                           "-fx-min-height: 35px; " +
                           "-fx-background-radius: 17; " +
                           "-fx-border-color: rgba(255, 100, 100, 0.6); " +
                           "-fx-border-width: 2; " +
                           "-fx-border-radius: 17; " +
                           "-fx-cursor: hand;");
        backButton.setOnAction(_ -> {
            this.playerName = null;
            this.isGuest = false;
            dialog.close();
        });
        
        Label titleLabel = new Label("Enter Your Name");
        titleLabel.setStyle("-fx-font-family: 'Frozenland'; " +
                           "-fx-font-size: 32px; " +
                           "-fx-text-fill: #a0e8ff; " +
                           "-fx-effect: dropshadow(gaussian, rgba(80, 200, 255, 0.6), 10, 0.4, 0, 0);");
        
        TextField nameField = new TextField();
        nameField.setPromptText("Your name");
        nameField.setMaxWidth(250);
        nameField.setStyle("-fx-font-family: 'Frozenland'; " +
                          "-fx-font-size: 18px; " +
                          "-fx-background-color: rgba(30, 60, 90, 0.8); " +
                          "-fx-text-fill: #e0f8ff; " +
                          "-fx-background-radius: 8; " +
                          "-fx-border-color: rgba(150, 220, 255, 0.4); " +
                          "-fx-border-width: 2; " +
                          "-fx-border-radius: 8; " +
                          "-fx-padding: 8 12;");
        nameField.requestFocus();
        
        Button playButton = new Button("Play");
        playButton.setStyle("-fx-font-family: 'Frozenland'; " +
                           "-fx-font-size: 20px; " +
                           "-fx-background-color: rgba(30, 80, 120, 0.6); " +
                           "-fx-text-fill: #e0f8ff; " +
                           "-fx-min-width: 120px; " +
                           "-fx-padding: 10 20; " +
                           "-fx-background-radius: 10; " +
                           "-fx-border-color: rgba(150, 220, 255, 0.5); " +
                           "-fx-border-width: 2; " +
                           "-fx-border-radius: 10; " +
                           "-fx-cursor: hand;");
        
        Button guestButton = new Button("Continue as Guest");
        guestButton.setStyle("-fx-font-family: 'Frozenland'; " +
                            "-fx-font-size: 18px; " +
                            "-fx-background-color: rgba(60, 40, 80, 0.6); " +
                            "-fx-text-fill: #e0f8ff; " +
                            "-fx-min-width: 200px; " +
                            "-fx-padding: 8 15; " +
                            "-fx-background-radius: 10; " +
                            "-fx-border-color: rgba(180, 150, 200, 0.5); " +
                            "-fx-border-width: 2; " +
                            "-fx-border-radius: 10; " +
                            "-fx-cursor: hand;");
        
        HBox buttonBox = new HBox(15, playButton, guestButton);
        buttonBox.setAlignment(Pos.CENTER);
        
        playButton.setOnAction(_ -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                this.playerName = name;
                this.isGuest = false;
                dialog.close();
            }
        });
        
        guestButton.setOnAction(_ -> {
            this.playerName = null;
            this.isGuest = true;
            dialog.close();
        });
        
        nameField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                playButton.fire();
            }
        });
        
        backButton.setOnAction(_ -> {
            this.playerName = null;
            this.isGuest = false;
            dialog.close();
        });
        
        contentBox.getChildren().addAll(titleLabel, nameField, buttonBox);
        
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);
        StackPane.setMargin(backButton, new Insets(10, 0, 0, 10));
        
        root.getChildren().addAll(contentBox, backButton);
        
        Scene scene = new Scene(root, 400, 200);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("window_style.css").toExternalForm());
        dialog.setScene(scene);
        dialog.setX(parentStage.getX() + (parentStage.getWidth() - 400) / 2);
        dialog.setY(parentStage.getY() + (parentStage.getHeight() - 200) / 2);
        dialog.showAndWait();
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public boolean isGuest() {
        return isGuest;
    }
}

