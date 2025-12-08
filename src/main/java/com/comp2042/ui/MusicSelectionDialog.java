package com.comp2042.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;

/**
 * Dialog for selecting background music track.
 * Allows users to choose between available music tracks.
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 * @see MusicManager
 */
public class MusicSelectionDialog {
    private final Stage dialog;
    
    public MusicSelectionDialog(Stage parent) {
        dialog = new Stage();
        dialog.initOwner(parent);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("Background Music");
        
        Label titleLabel = new Label("Select Background Music");
        titleLabel.getStyleClass().add("menu-title");
        titleLabel.setStyle("-fx-font-size: 24px;");
        
        Button russiaMusicButton = new Button("Russia Music");
        russiaMusicButton.getStyleClass().add("menu-button");
        russiaMusicButton.setPrefWidth(200);
        russiaMusicButton.setOnAction(_ -> {
            SettingsManager.setSelectedMusicTrack("Russia Music");
            MusicManager.loadTrack("Russia Music");
            dialog.close();
        });
        
        Button christmasMusicButton = new Button("Christmas");
        christmasMusicButton.getStyleClass().add("menu-button");
        christmasMusicButton.setPrefWidth(200);
        christmasMusicButton.setOnAction(_ -> {
            SettingsManager.setSelectedMusicTrack("Christmas");
            MusicManager.loadTrack("Christmas");
            dialog.close();
        });
        
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.getChildren().addAll(titleLabel, russiaMusicButton, christmasMusicButton);
        layout.getStyleClass().add("gameover-panel");
        
        Scene scene = new Scene(layout, 400, 300);
        URL cssUrl = ResourceLoader.getResource("window_style.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }
        dialog.setScene(scene);
    }
    
    public void show() {
        dialog.showAndWait();
    }
}

