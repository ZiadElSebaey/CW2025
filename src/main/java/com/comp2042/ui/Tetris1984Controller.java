package com.comp2042.ui;

import com.comp2042.ui.GameMode;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.application.Platform;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controller for the 1984-themed game mode.
 * Handles the special visual style and gameplay mechanics for the 1984 mode.
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 */
public class Tetris1984Controller {

    @FXML
    private StackPane rootPane;

    @FXML
    private ImageView mainMenuImage;

    @FXML
    private Rectangle scanlineOverlay;

    @FXML
    private Button backButton;

    @FXML
    private Label subtitleLabel;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        loadFonts();
        
        if (mainMenuImage != null) {
            mainMenuImage.setOpacity(1.0);
        }
        
        if (backButton != null) {
            backButton.setOpacity(0.0);
        }
        
        if (subtitleLabel != null) {
            subtitleLabel.setLayoutY(550);
            Platform.runLater(() -> {
                if (subtitleLabel != null) {
                    double labelWidth = subtitleLabel.getWidth();
                    subtitleLabel.setLayoutX(360 - labelWidth / 2);
                }
            });
        }
        
        createScanlineEffect();
        startGlowingFlashAnimation();
        
        if (rootPane != null) {
            rootPane.setOnMouseClicked(this::onAnyKeyPressed);
            rootPane.setOnKeyPressed(this::onKeyPressed);
            rootPane.setFocusTraversable(true);
            rootPane.requestFocus();
        }
    }
    
    private void loadFonts() {
        if (subtitleLabel != null) {
            Font digitalFont = FontLoader.loadFontFromUrl("digital.ttf", 24);
            if (digitalFont != null) {
                subtitleLabel.setFont(digitalFont);
            }
        }
    }
    
    private void createScanlineEffect() {
        if (scanlineOverlay != null) {
            scanlineOverlay.setFill(javafx.scene.paint.Color.rgb(0, 0, 0, 0.08));
            scanlineOverlay.setOpacity(1.0);
        }
    }
    
    private void startGlowingFlashAnimation() {
        if (mainMenuImage == null) return;
        
        Glow glowEffect = new Glow();
        glowEffect.setLevel(0.0);
        mainMenuImage.setEffect(glowEffect);
        
        Timeline glowTimeline = new Timeline(
            new KeyFrame(Duration.seconds(0.0), e -> glowEffect.setLevel(0.0)),
            new KeyFrame(Duration.seconds(0.3), e -> glowEffect.setLevel(1.0)),
            new KeyFrame(Duration.seconds(0.6), e -> glowEffect.setLevel(0.0)),
            new KeyFrame(Duration.seconds(0.9), e -> glowEffect.setLevel(0.8)),
            new KeyFrame(Duration.seconds(1.2), e -> glowEffect.setLevel(0.0))
        );
        glowTimeline.setCycleCount(1);
        
        glowTimeline.setOnFinished(e -> {
            ColorAdjust retroColor = new ColorAdjust();
            retroColor.setContrast(0.1);
            retroColor.setSaturation(-0.1);
            retroColor.setBrightness(-0.05);
            mainMenuImage.setEffect(retroColor);
            
            FadeTransition buttonFadeIn = new FadeTransition(Duration.seconds(0.8), backButton);
            buttonFadeIn.setFromValue(0.0);
            buttonFadeIn.setToValue(1.0);
            buttonFadeIn.play();
            
            startSubtitleBlinkAnimation();
        });
        
        glowTimeline.play();
    }
    
    private void startSubtitleBlinkAnimation() {
        if (subtitleLabel == null) return;
        
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.8), subtitleLabel);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.8), subtitleLabel);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        
        SequentialTransition blinkSequence = new SequentialTransition(
            fadeIn,
            fadeOut
        );
        blinkSequence.setCycleCount(Timeline.INDEFINITE);
        blinkSequence.play();
    }

    private void onAnyKeyPressed(MouseEvent event) {
        start1984Game();
    }
    
    private void onKeyPressed(KeyEvent event) {
        start1984Game();
    }
    
    private void start1984Game() {
        SceneNavigator.navigateToScene(stage, "gameLayout.fxml", loader -> {
            GuiController guiController = loader.getController();
            if (guiController == null) return;
            guiController.setStage(stage);
            guiController.setGameMode(GameMode.MODE_1984.getValue());
            guiController.setLevel(null);
            guiController.setPlayerName("1984 Player", false);
            com.comp2042.logic.GameController gameController = new com.comp2042.logic.GameController(guiController);
            guiController.setEventListener(gameController);
            Scene scene = stage.getScene();
            if (scene != null) {
                MainMenuController.activeGameScene = scene;
            }
            MainMenuController.activeGuiController = guiController;
            guiController.startCountdown();
        });
    }

    @FXML
    private void onBackClicked() {
        SceneNavigator.navigateToScene(stage, "gamemodes.fxml", loader -> {
            GamemodesController gamemodesController = loader.getController();
            gamemodesController.setStage(stage);
        });
    }
}

