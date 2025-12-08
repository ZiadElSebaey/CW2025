package com.comp2042.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

/**
 * Utility class for navigating between scenes and loading FXML files.
 */
public final class SceneNavigator {
    
    private SceneNavigator() {}
    
    /**
     * Loads an FXML file and creates a scene.
     * 
     * @param fxmlFile The FXML file path (e.g., "mainMenu.fxml")
     * @param controllerSetup Optional consumer to configure the controller after loading
     * @return The created Scene, or null if loading failed
     */
    public static Scene loadScene(String fxmlFile, Consumer<FXMLLoader> controllerSetup) {
        try {
            URL location = ResourceLoader.getResource(fxmlFile);
            if (location == null) {
                System.err.println("FXML file not found: " + fxmlFile);
                return null;
            }
            
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            Parent root = fxmlLoader.load();
            
            if (controllerSetup != null) {
                controllerSetup.accept(fxmlLoader);
            }
            
            return new Scene(root, WindowConstants.WINDOW_WIDTH, WindowConstants.WINDOW_HEIGHT);
        } catch (IOException e) {
            System.err.println("Failed to load FXML file '" + fxmlFile + "': " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Loads an FXML file, creates a scene, and sets it on the stage.
     * 
     * @param stage The stage to set the scene on
     * @param fxmlFile The FXML file path (e.g., "mainMenu.fxml")
     * @param controllerSetup Optional consumer to configure the controller after loading
     * @return true if successful, false otherwise
     */
    public static boolean navigateToScene(Stage stage, String fxmlFile, Consumer<FXMLLoader> controllerSetup) {
        Scene scene = loadScene(fxmlFile, controllerSetup);
        if (scene != null && stage != null) {
            stage.setScene(scene);
            return true;
        }
        return false;
    }
    
    /**
     * Loads an FXML file and returns the controller.
     * 
     * @param fxmlFile The FXML file path
     * @param controllerClass The class of the controller
     * @param <T> The controller type
     * @return The controller instance, or null if loading failed
     */
    public static <T> T loadController(String fxmlFile, Class<T> controllerClass) {
        try {
            URL location = ResourceLoader.getResource(fxmlFile);
            if (location == null) {
                System.err.println("FXML file not found: " + fxmlFile);
                return null;
            }
            
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            fxmlLoader.load();
            
            return controllerClass.cast(fxmlLoader.getController());
        } catch (IOException e) {
            System.err.println("Failed to load FXML file '" + fxmlFile + "': " + e.getMessage());
            return null;
        }
    }
}

