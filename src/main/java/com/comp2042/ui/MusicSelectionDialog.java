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
        
        Button music1Button = new Button("Music Track 1");
        music1Button.getStyleClass().add("menu-button");
        music1Button.setPrefWidth(200);
        music1Button.setOnAction(_ -> {
            System.out.println("Selected: Music Track 1");
            dialog.close();
        });
        
        Button music2Button = new Button("Music Track 2");
        music2Button.getStyleClass().add("menu-button");
        music2Button.setPrefWidth(200);
        music2Button.setOnAction(_ -> {
            System.out.println("Selected: Music Track 2");
            dialog.close();
        });
        
        Button music3Button = new Button("Music Track 3");
        music3Button.getStyleClass().add("menu-button");
        music3Button.setPrefWidth(200);
        music3Button.setOnAction(_ -> {
            System.out.println("Selected: Music Track 3");
            dialog.close();
        });
        
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.getChildren().addAll(titleLabel, music1Button, music2Button, music3Button);
        layout.getStyleClass().add("gameover-panel");
        
        Scene scene = new Scene(layout, 400, 400);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("window_style.css").toExternalForm());
        dialog.setScene(scene);
    }
    
    public void show() {
        dialog.showAndWait();
    }
}

