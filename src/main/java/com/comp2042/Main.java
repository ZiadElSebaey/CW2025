package com.comp2042;

import com.comp2042.ui.MainMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            java.io.InputStream fontStream = getClass().getClassLoader().getResourceAsStream("FROZENLAND.otf");
            if (fontStream != null) {
                Font font = Font.loadFont(fontStream, 40);
                if (font != null) {
                    System.out.println("SUCCESS! Font loaded: " + font.getName() + " | Family: " + font.getFamily());
                } else {
                    System.out.println("Font.loadFont returned null - OTF may be incompatible");
                }
                fontStream.close();
            } else {
                System.out.println("Font file not found!");
            }
        } catch (Exception e) {
            System.out.println("Error loading font: " + e.getMessage());
        }

        URL location = getClass().getClassLoader().getResource("mainMenu.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();
        MainMenuController menuController = fxmlLoader.getController();
        menuController.setStage(primaryStage);

        primaryStage.setTitle("TetrisJFX");
        Scene scene = new Scene(root, 720, 680);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
