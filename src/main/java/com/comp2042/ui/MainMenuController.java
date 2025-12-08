package com.comp2042.ui;

import com.comp2042.logic.GameController;
import com.comp2042.ui.GameMode;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controller for the main menu screen.
 * Handles navigation to different game modes, settings, leaderboard, and other menu options.
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 */
@SuppressWarnings("unused")
public class MainMenuController {

    private Stage stage;

    public static Scene activeGameScene;
    public static GuiController activeGuiController;

    @FXML
    private Button resumeButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button leaderboardButton;

    @FXML
    private Button musicButton;

    @FXML
    private VBox creatorPanel;

    @FXML
    private StackPane rootPane;

    @FXML
    private HBox playSubmenu;

    @FXML
    private Button playButton;

    @FXML
    private VBox otherButtons;

    @FXML
    private Label titleLabel;

    @FXML
    private Button backButton;
    
    @FXML
    private StackPane dialogueContainer;
    
    @FXML
    private ImageView dialogueImage;
    
    @FXML
    private Label dialogueText;
    
    @FXML
    private HBox dialogueButtons;
    
    @FXML
    private Button acceptButton;
    
    @FXML
    private Button ignoreButton;

    private VBox leaderboardContainer;
    private LeaderboardPanel leaderboardPanel;
    private Timeline dialogueTimeline;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        updateResumeButtonVisibility();
        AnimatedBackground animatedBackground = new AnimatedBackground(WindowConstants.WINDOW_WIDTH, WindowConstants.WINDOW_HEIGHT);
        rootPane.getChildren().addFirst(animatedBackground);
        
        RotateTransition rotate = new RotateTransition(Duration.seconds(4), settingsButton);
        rotate.setByAngle(360);
        rotate.setCycleCount(Timeline.INDEFINITE);
        rotate.play();

        javafx.animation.ScaleTransition pulse = new javafx.animation.ScaleTransition(Duration.seconds(1), leaderboardButton);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.15);
        pulse.setToY(1.15);
        pulse.setCycleCount(Timeline.INDEFINITE);
        pulse.setAutoReverse(true);
        pulse.play();

        javafx.animation.ScaleTransition musicPulse = new javafx.animation.ScaleTransition(Duration.seconds(0.8), musicButton);
        musicPulse.setFromX(1.0);
        musicPulse.setFromY(1.0);
        musicPulse.setToX(1.2);
        musicPulse.setToY(1.2);
        musicPulse.setCycleCount(Timeline.INDEFINITE);
        musicPulse.setAutoReverse(true);
        musicPulse.play();
        
        javafx.animation.FadeTransition musicFade = new javafx.animation.FadeTransition(Duration.seconds(0.8), musicButton);
        musicFade.setFromValue(0.8);
        musicFade.setToValue(1.0);
        musicFade.setCycleCount(Timeline.INDEFINITE);
        musicFade.setAutoReverse(true);
        musicFade.play();

        javafx.animation.TranslateTransition floatUp = new javafx.animation.TranslateTransition(Duration.seconds(2), creatorPanel);
        floatUp.setByY(-10);
        floatUp.setCycleCount(Timeline.INDEFINITE);
        floatUp.setAutoReverse(true);
        floatUp.play();

        setupLeaderboardPanel();
        showDialogue();
        
        // Add click handler to Alexey character
        if (creatorPanel != null) {
            creatorPanel.setOnMouseClicked(e -> {
                // Show dialogue immediately when Alexey is clicked
                if (dialogueContainer != null && dialogueContainer.getScene() != null) {
                    showDialogueImmediately();
                }
            });
            creatorPanel.setCursor(javafx.scene.Cursor.HAND);
        }
    }
    
    private void showDialogue() {
        // Cancel any existing dialogue animation
        hideDialogue();
        
        if (dialogueContainer != null && dialogueText != null) {
            // Hide buttons initially
            if (dialogueButtons != null) {
                dialogueButtons.setVisible(false);
                dialogueButtons.setManaged(false);
                dialogueButtons.setOpacity(0);
            }
            
            // Hide initially
            dialogueContainer.setVisible(false);
            dialogueContainer.setManaged(false);
            dialogueContainer.setOpacity(0);
            
            // Set first sentence
            dialogueText.setText("Long ago, in a room not far from here in moscow… i created this game.");
            
            // Show after 3.8 seconds with fade in animation
            dialogueTimeline = new Timeline(
                new KeyFrame(Duration.seconds(3.8), e -> {
                    // Only show if still on main menu (dialogueContainer is still in the scene)
                    if (dialogueContainer != null && dialogueContainer.getScene() != null) {
                        dialogueContainer.setVisible(true);
                        dialogueContainer.setManaged(true);
                        
                        // Fade in animation
                        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), dialogueContainer);
                        fadeIn.setFromValue(0);
                        fadeIn.setToValue(1);
                        fadeIn.play();
                    }
                }),
                // Change to second sentence after 8.2 seconds (3.8 + 4.4 seconds display)
                new KeyFrame(Duration.seconds(8.2), e -> {
                    if (dialogueText != null && dialogueContainer != null && dialogueContainer.getScene() != null) {
                        FadeTransition fadeOut = new FadeTransition(Duration.millis(400), dialogueText);
                        fadeOut.setFromValue(1);
                        fadeOut.setToValue(0);
                        fadeOut.setOnFinished(event -> {
                            dialogueText.setText("Back then, it was just me, the cold Moscow night, and an idea.");
                            FadeTransition fadeIn = new FadeTransition(Duration.millis(400), dialogueText);
                            fadeIn.setFromValue(0);
                            fadeIn.setToValue(1);
                            fadeIn.play();
                        });
                        fadeOut.play();
                    }
                }),
                // Change to third sentence after 12.6 seconds (8.2 + 4.4 seconds display)
                new KeyFrame(Duration.seconds(12.6), e -> {
                    if (dialogueText != null && dialogueContainer != null && dialogueContainer.getScene() != null) {
                        FadeTransition fadeOut = new FadeTransition(Duration.millis(400), dialogueText);
                        fadeOut.setFromValue(1);
                        fadeOut.setToValue(0);
                        fadeOut.setOnFinished(event -> {
                            dialogueText.setText("I never imagined that idea would one day reach players all over the world.");
                            FadeTransition fadeIn = new FadeTransition(Duration.millis(400), dialogueText);
                            fadeIn.setFromValue(0);
                            fadeIn.setToValue(1);
                            fadeIn.play();
                        });
                        fadeOut.play();
                    }
                }),
                // Change to fourth sentence after 17 seconds (12.6 + 4.4 seconds display)
                new KeyFrame(Duration.seconds(17.0), e -> {
                    if (dialogueText != null && dialogueContainer != null && dialogueContainer.getScene() != null) {
                        FadeTransition fadeOut = new FadeTransition(Duration.millis(400), dialogueText);
                        fadeOut.setFromValue(1);
                        fadeOut.setToValue(0);
                        fadeOut.setOnFinished(event -> {
                            dialogueText.setText("If you'd like, I can show you how it all works.");
                            FadeTransition fadeIn = new FadeTransition(Duration.millis(400), dialogueText);
                            fadeIn.setFromValue(0);
                            fadeIn.setToValue(1);
                            fadeIn.setOnFinished(event2 -> {
                                // Show buttons after fourth sentence fades in
                                if (dialogueButtons != null) {
                                    dialogueButtons.setVisible(true);
                                    dialogueButtons.setManaged(true);
                                    dialogueButtons.setOpacity(0);
                                    FadeTransition buttonsFadeIn = new FadeTransition(Duration.millis(400), dialogueButtons);
                                    buttonsFadeIn.setFromValue(0);
                                    buttonsFadeIn.setToValue(1);
                                    buttonsFadeIn.play();
                                }
                            });
                            fadeIn.play();
                        });
                        fadeOut.play();
                    }
                })
            );
            dialogueTimeline.play();
        }
    }
    
    private void showDialogueImmediately() {
        // Cancel any existing dialogue animation
        hideDialogue();
        
        if (dialogueContainer != null && dialogueText != null) {
            // Hide buttons initially
            if (dialogueButtons != null) {
                dialogueButtons.setVisible(false);
                dialogueButtons.setManaged(false);
                dialogueButtons.setOpacity(0);
            }
            
            // Set first sentence
            dialogueText.setText("Long ago, in a room not far from here in moscow… i created this game.");
            
            // Show immediately
            dialogueContainer.setVisible(true);
            dialogueContainer.setManaged(true);
            dialogueContainer.setOpacity(1);
            dialogueText.setOpacity(1);
            
            // Start timeline immediately (no delay)
            dialogueTimeline = new Timeline(
                // Change to second sentence after 4.4 seconds
                new KeyFrame(Duration.seconds(4.4), e -> {
                    if (dialogueText != null && dialogueContainer != null && dialogueContainer.getScene() != null) {
                        FadeTransition fadeOut = new FadeTransition(Duration.millis(400), dialogueText);
                        fadeOut.setFromValue(1);
                        fadeOut.setToValue(0);
                        fadeOut.setOnFinished(event -> {
                            dialogueText.setText("Back then, it was just me, the cold Moscow night, and an idea.");
                            FadeTransition fadeIn = new FadeTransition(Duration.millis(400), dialogueText);
                            fadeIn.setFromValue(0);
                            fadeIn.setToValue(1);
                            fadeIn.play();
                        });
                        fadeOut.play();
                    }
                }),
                // Change to third sentence after 8.8 seconds (4.4 + 4.4)
                new KeyFrame(Duration.seconds(8.8), e -> {
                    if (dialogueText != null && dialogueContainer != null && dialogueContainer.getScene() != null) {
                        FadeTransition fadeOut = new FadeTransition(Duration.millis(400), dialogueText);
                        fadeOut.setFromValue(1);
                        fadeOut.setToValue(0);
                        fadeOut.setOnFinished(event -> {
                            dialogueText.setText("I never imagined that idea would one day reach players all over the world.");
                            FadeTransition fadeIn = new FadeTransition(Duration.millis(400), dialogueText);
                            fadeIn.setFromValue(0);
                            fadeIn.setToValue(1);
                            fadeIn.play();
                        });
                        fadeOut.play();
                    }
                }),
                // Change to fourth sentence after 13.2 seconds (8.8 + 4.4)
                new KeyFrame(Duration.seconds(13.2), e -> {
                    if (dialogueText != null && dialogueContainer != null && dialogueContainer.getScene() != null) {
                        FadeTransition fadeOut = new FadeTransition(Duration.millis(400), dialogueText);
                        fadeOut.setFromValue(1);
                        fadeOut.setToValue(0);
                        fadeOut.setOnFinished(event -> {
                            dialogueText.setText("If you'd like, I can show you how it all works.");
                            FadeTransition fadeIn = new FadeTransition(Duration.millis(400), dialogueText);
                            fadeIn.setFromValue(0);
                            fadeIn.setToValue(1);
                            fadeIn.setOnFinished(event2 -> {
                                // Show buttons after fourth sentence fades in
                                if (dialogueButtons != null) {
                                    dialogueButtons.setVisible(true);
                                    dialogueButtons.setManaged(true);
                                    dialogueButtons.setOpacity(0);
                                    FadeTransition buttonsFadeIn = new FadeTransition(Duration.millis(400), dialogueButtons);
                                    buttonsFadeIn.setFromValue(0);
                                    buttonsFadeIn.setToValue(1);
                                    buttonsFadeIn.play();
                                }
                            });
                            fadeIn.play();
                        });
                        fadeOut.play();
                    }
                })
            );
            dialogueTimeline.play();
        }
    }
    
    private void hideDialogue() {
        // Stop any pending dialogue animation
        if (dialogueTimeline != null) {
            dialogueTimeline.stop();
            dialogueTimeline = null;
        }
        
        // Hide buttons
        if (dialogueButtons != null) {
            dialogueButtons.setVisible(false);
            dialogueButtons.setManaged(false);
            dialogueButtons.setOpacity(0);
        }
        
        // Hide dialogue immediately
        if (dialogueContainer != null) {
            dialogueContainer.setVisible(false);
            dialogueContainer.setManaged(false);
            dialogueContainer.setOpacity(0);
        }
    }
    
    @FXML
    private void onAcceptClicked() {
        // Hide dialogue and navigate to How To Play
        hideDialogue();
        onHowToPlayClicked();
    }
    
    @FXML
    private void onIgnoreClicked() {
        // Hide dialogue
        hideDialogue();
    }

    private void setupLeaderboardPanel() {
        leaderboardPanel = new LeaderboardPanel();
        leaderboardContainer = new VBox(leaderboardPanel);
        leaderboardContainer.setAlignment(javafx.geometry.Pos.CENTER);
        leaderboardContainer.setVisible(false);
        leaderboardPanel.getCloseButton().setOnAction(_ -> leaderboardContainer.setVisible(false));
        leaderboardPanel.setOnClearCallback(() -> {
            if (activeGuiController != null) {
                activeGuiController.refreshHighScoreDisplay();
            }
        });
        rootPane.getChildren().add(leaderboardContainer);
    }

    private void updateResumeButtonVisibility() {
        boolean canResume = activeGuiController != null
                && activeGuiController.isPaused()
                && !activeGuiController.isGameOver();
        resumeButton.setVisible(canResume);
        resumeButton.setManaged(canResume);
    }

    @FXML
    private void onPlayClicked() {
        PlayerNameDialog nameDialog = new PlayerNameDialog(stage);
        String playerName = nameDialog.getPlayerName();
        boolean isGuest = nameDialog.isGuest();
        
        if (!isGuest && playerName == null) {
            return;
        }
        
        // Hide dialogue before leaving main menu
        hideDialogue();
        
        resetMainMenu();
        
        Scene gameScene = SceneNavigator.loadScene("gameLayout.fxml", loader -> {
            GuiController guiController = loader.getController();
            guiController.setStage(stage);
            guiController.setPlayerName(playerName, isGuest);
            guiController.setLevel(null);
            activeGuiController = guiController;
            new GameController(guiController);
            
            if (guiController.getCurrentLevel() == null && !GameMode.INVERTED.matches(guiController.getGameMode())) {
                guiController.startCountdown();
            } else {
                guiController.startNewGame();
            }
        });
        
        if (gameScene != null) {
            activeGameScene = gameScene;
            stage.setScene(gameScene);
        }
    }
    
    private void resetMainMenu() {
        playSubmenu.setVisible(false);
        playSubmenu.setManaged(false);
        playButton.setVisible(true);
        playButton.setManaged(true);
        otherButtons.setVisible(true);
        otherButtons.setManaged(true);
        leaderboardButton.setVisible(true);
        leaderboardButton.setManaged(true);
        musicButton.setVisible(true);
        musicButton.setManaged(true);
        titleLabel.setVisible(true);
        titleLabel.setManaged(true);
        creatorPanel.setVisible(true);
        creatorPanel.setManaged(true);
        // Only show dialogue if we're actually on the main menu scene
        if (dialogueContainer != null && dialogueContainer.getScene() != null && rootPane.getScene() == dialogueContainer.getScene()) {
            showDialogue();
        }
        backButton.setVisible(false);
        backButton.setManaged(false);
    }

    @FXML
    private void onResumeClicked() {
        if (activeGameScene != null && activeGuiController != null) {
            stage.setScene(activeGameScene);
            activeGuiController.requestFocus();
        }
    }

    @FXML
    private void onPlayMenuClicked() {
        showPlayMenu();
    }
    
    public void showPlayMenu() {
        playButton.setVisible(false);
        playButton.setManaged(false);
        otherButtons.setVisible(false);
        otherButtons.setManaged(false);
        leaderboardButton.setVisible(false);
        leaderboardButton.setManaged(false);
        musicButton.setVisible(false);
        musicButton.setManaged(false);
        titleLabel.setVisible(false);
        titleLabel.setManaged(false);
        creatorPanel.setVisible(false);
        creatorPanel.setManaged(false);
        hideDialogue();
        backButton.setVisible(true);
        backButton.setManaged(true);
        playSubmenu.setVisible(true);
        playSubmenu.setManaged(true);
    }

    @FXML
    private void onLevelsClicked() {
        // Hide dialogue before leaving main menu
        hideDialogue();
        resetMainMenu();
        SceneNavigator.navigateToScene(stage, "levels.fxml", loader -> {
            LevelsController levelsController = loader.getController();
            levelsController.setStage(stage);
        });
    }

    @FXML
    private void onGamemodesClicked() {
        // Hide dialogue before leaving main menu
        hideDialogue();
        resetMainMenu();
        SceneNavigator.navigateToScene(stage, "gamemodes.fxml", loader -> {
            GamemodesController gamemodesController = loader.getController();
            gamemodesController.setStage(stage);
        });
    }

    @FXML
    private void onPlayMenuBackClicked() {
        resetMainMenu();
    }

    @FXML
    private void onHowToPlayClicked() {
        // Hide dialogue before leaving main menu
        hideDialogue();
        loadScene("howToPlay.fxml");
    }

    @FXML
    private void onSettingsClicked() {
        // Hide dialogue before leaving main menu
        hideDialogue();
        loadScene("settings.fxml");
    }

    @FXML
    private void onLeaderboardClicked() {
        // Hide dialogue when showing leaderboard
        hideDialogue();
        leaderboardPanel.refreshEntries();
        leaderboardContainer.setVisible(true);
    }

    @FXML
    private void onMusicClicked() {
        MusicSelectionDialog musicDialog = new MusicSelectionDialog(stage);
        musicDialog.show();
    }

    private void loadScene(String fxmlFile) {
        SceneNavigator.navigateToScene(stage, fxmlFile, loader -> {
            Object controller = loader.getController();
            if (controller instanceof HowToPlayController howToPlayController) {
                howToPlayController.setStage(stage);
            } else if (controller instanceof SettingsController settingsController) {
                settingsController.setStage(stage);
            } else if (controller instanceof GamemodesController gamemodesController) {
                gamemodesController.setStage(stage);
            }
        });
    }

    @FXML
    private void onQuitClicked() {
        stage.close();
    }

    public static void clearActiveGame() {
        activeGameScene = null;
        activeGuiController = null;
    }
}

