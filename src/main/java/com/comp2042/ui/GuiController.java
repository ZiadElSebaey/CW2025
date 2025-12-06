package com.comp2042.ui;

import com.comp2042.logic.DownData;
import com.comp2042.logic.EventSource;
import com.comp2042.logic.EventType;
import com.comp2042.logic.InputEventListener;
import com.comp2042.logic.MoveEvent;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.application.Platform;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;
    private static final int BRICK_SIZE_1984 = 15;
    private static final int DEFAULT_DROP_INTERVAL_MS = 400;
    private static final int BRICK_PANEL_Y_OFFSET = -20;
    private static final int HIDDEN_ROWS = 2;
    private static final int BOARD_PADDING = 8;
    private static final int RECTANGLE_ARC_SIZE = 9;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GridPane ghostPanel;

    @FXML
    private GameOverPanel gameOverPanel;

    @FXML
    private PausePanel pausePanel;
    @FXML
    private LevelCompletePanel levelCompletePanel;
    @FXML
    private VBox levelCompleteContainer;
    @FXML
    private CountdownPanel countdownPanel;
    @FXML
    private VBox countdownContainer;

    @FXML
    private BorderPane gameBoard;

    @FXML
    private StackPane rootPane;

    @FXML
    private VBox pauseContainer;

    @FXML
    private VBox gameOverContainer;

    @FXML
    private Pane gamePane;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label highScoreLabel;
    @FXML
    private Label highScoreHolderLabel;
    @FXML
    private Label linesLabel;
    @FXML
    private Label timerLabel;
    @FXML
    private Label objectiveLabel;
    @FXML
    private VBox objectivePanel;
    @FXML
    private VBox rightPanel;
    @FXML
    private VBox nextBlockContainer;
    @FXML
    private StackPane nextBlockStackPane;
    @FXML
    private GridPane nextBlockPanel;
    @FXML
    private GridPane holdBlockPanel;
    @FXML
    private VBox holdBlockContainer;

    @FXML
    private VBox leftPanel;

    @FXML
    private VBox creatorPanel;

    @FXML
    private javafx.scene.control.Button backButton;

    @FXML
    private ImageView borderFrame;
    
    @FXML
    private VBox invertedCharactersPanel;
    
    @FXML
    private ImageView vecnaImage;
    
    private Label scoreLabel1984;
    private Label linesLabel1984;
    private Timeline glitchTimeline1984;
    private Rectangle filmGrainOverlay;
    private Timeline filmGrainTimeline;
    private Timeline flickerTimeline;

    private Rectangle[][] displayMatrix;
    private VBox leaderboardContainer;
    private LeaderboardPanel leaderboardPanel;
    private Rectangle[][] nextBlockRectangles;
    private Rectangle[][] holdBlockRectangles;
    private int[][] previousHoldData;
    private IntegerProperty currentScoreProperty;
    private InputEventListener eventListener;
    private Rectangle[][] rectangles;
    private Rectangle[][] ghostRectangles;
    private ViewData lastViewData;
    private Timeline timeLine;
    private Timeline timerUpdateLine;
    private Timeline freePlayTimerLine;
    private final BooleanProperty isPause = new SimpleBooleanProperty();
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();
    private Stage stage;
    private String playerName;
    private boolean isGuest;
    private String gameMode;
    private com.comp2042.logic.Level currentLevel;
    private int tripleClearsCount;
    private long levelStartTime;
    private long pauseStartTime;
    private long totalPauseDuration;
    private long freePlayStartTime;
    private long freePlayPauseStartTime;
    private long freePlayTotalPauseDuration;
    private int dropIntervalMs = DEFAULT_DROP_INTERVAL_MS;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void setPlayerName(String name, boolean guest) {
        this.playerName = name;
        this.isGuest = guest;
    }
    
    public com.comp2042.logic.Level getCurrentLevel() {
        return currentLevel;
    }
    
    public String getGameMode() {
        return gameMode;
    }
    
    public void setGameMode(String mode) {
        this.gameMode = mode;
        boolean isInvertedMode = mode != null && mode.equals("inverted");
        boolean is1984Mode = mode != null && mode.equals("1984");
        
        if (isInvertedMode && rootPane != null) {
            rootPane.getTransforms().removeIf(transform -> transform instanceof Rotate);
            Rotate rotate = new Rotate(180, 360, 340);
            rootPane.getTransforms().add(rotate);
        } else if (rootPane != null) {
            rootPane.getTransforms().removeIf(transform -> transform instanceof Rotate);
        }
        
        if (isInvertedMode) {
            if (objectivePanel != null) {
                objectivePanel.setVisible(false);
            }
            if (rightPanel != null) {
                rightPanel.setVisible(true);
                rightPanel.setManaged(true);
                rightPanel.getTransforms().removeIf(transform -> transform instanceof Rotate);
                Platform.runLater(() -> {
                    double width = rightPanel.getBoundsInLocal().getWidth();
                    double height = rightPanel.getBoundsInLocal().getHeight();
                    if (width > 0 && height > 0) {
                        Rotate rotate = new Rotate(180, width / 2, height / 2);
                        rightPanel.getTransforms().add(rotate);
                    } else {
                        Rotate rotate = new Rotate(180, 100, 200);
                        rightPanel.getTransforms().add(rotate);
                    }
                });
            }
            if (holdBlockContainer != null) {
                holdBlockContainer.setVisible(true);
                holdBlockContainer.setManaged(true);
            }
            if (leftPanel != null) {
                leftPanel.setLayoutX(500);
                leftPanel.setLayoutY(500 - 66);
                leftPanel.getTransforms().removeIf(transform -> transform instanceof Rotate);
                Platform.runLater(() -> {
                    double width = leftPanel.getBoundsInLocal().getWidth();
                    double height = leftPanel.getBoundsInLocal().getHeight();
                    if (width > 0 && height > 0) {
                        Rotate rotate = new Rotate(180, width / 2, height / 2);
                        leftPanel.getTransforms().add(rotate);
                    } else {
                        Rotate rotate = new Rotate(180, 75, 150);
                        leftPanel.getTransforms().add(rotate);
                    }
                });
            }
            if (creatorPanel != null) {
                creatorPanel.setVisible(false);
                creatorPanel.setManaged(false);
            }
            if (invertedCharactersPanel != null) {
                invertedCharactersPanel.setVisible(true);
                invertedCharactersPanel.setManaged(true);
                // Position on left side, accounting for the 180-degree rotation
                // Since the screen is rotated, we need to position from the rotated perspective
                // Use negative translateX to move it further left
                invertedCharactersPanel.setLayoutX(0);
                invertedCharactersPanel.setLayoutY(5);
                invertedCharactersPanel.setTranslateX(-285);
                invertedCharactersPanel.getTransforms().removeIf(transform -> transform instanceof Rotate);
                Platform.runLater(() -> {
                    double width = invertedCharactersPanel.getBoundsInLocal().getWidth();
                    double height = invertedCharactersPanel.getBoundsInLocal().getHeight();
                    if (width > 0 && height > 0) {
                        Rotate rotate = new Rotate(180, width / 2, height / 2);
                        invertedCharactersPanel.getTransforms().add(rotate);
                    } else {
                        Rotate rotate = new Rotate(180, 300, 300);
                        invertedCharactersPanel.getTransforms().add(rotate);
                    }
                });
            }
            if (backButton != null) {
                backButton.setLayoutX(720 - 100);
                backButton.setLayoutY(680 - 80);
                backButton.getTransforms().removeIf(transform -> transform instanceof Rotate);
                Platform.runLater(() -> {
                    double width = backButton.getBoundsInLocal().getWidth();
                    double height = backButton.getBoundsInLocal().getHeight();
                    if (width > 0 && height > 0) {
                        Rotate rotate = new Rotate(180, width / 2, height / 2);
                        backButton.getTransforms().add(rotate);
                    } else {
                        Rotate rotate = new Rotate(180, 20, 20);
                        backButton.getTransforms().add(rotate);
                    }
                });
            }
        } else {
            if (objectivePanel != null && currentLevel == null) {
                objectivePanel.setVisible(true);
            }
            if (rightPanel != null) {
                rightPanel.setVisible(true);
                rightPanel.setManaged(true);
                rightPanel.getTransforms().removeIf(transform -> transform instanceof Rotate);
            }
            if (holdBlockContainer != null) {
                holdBlockContainer.setVisible(true);
                holdBlockContainer.setManaged(true);
            }
            if (leftPanel != null) {
                leftPanel.setLayoutX(15);
                leftPanel.setLayoutY(180);
                leftPanel.getTransforms().removeIf(transform -> transform instanceof Rotate);
            }
            if (creatorPanel != null) {
                creatorPanel.setVisible(true);
                creatorPanel.setManaged(true);
            }
            if (invertedCharactersPanel != null) {
                invertedCharactersPanel.setVisible(false);
                invertedCharactersPanel.setManaged(false);
                invertedCharactersPanel.getTransforms().removeIf(transform -> transform instanceof Rotate);
            }
            if (backButton != null) {
                backButton.setLayoutX(15);
                backButton.setLayoutY(15);
                backButton.getTransforms().removeIf(transform -> transform instanceof Rotate);
            }
        }
        
        if (is1984Mode) {
            if (leftPanel != null) {
                leftPanel.setVisible(false);
                leftPanel.setManaged(false);
            }
            if (borderFrame != null) {
                borderFrame.setVisible(false);
            }
            if (rightPanel != null) {
                rightPanel.setVisible(false);
                rightPanel.setManaged(false);
            }
            if (creatorPanel != null) {
                creatorPanel.setVisible(true);
                creatorPanel.setManaged(true);
                creatorPanel.setLayoutX(560);
                creatorPanel.setLayoutY(480);
                javafx.animation.TranslateTransition floatUp = new javafx.animation.TranslateTransition(Duration.seconds(2), creatorPanel);
                floatUp.setByY(-10);
                floatUp.setCycleCount(Timeline.INDEFINITE);
                floatUp.setAutoReverse(true);
                floatUp.play();
            }
            if (backButton != null) {
                backButton.getStyleClass().clear();
                backButton.getStyleClass().add("back-icon-1984");
                backButton.setOnAction(_ -> returnToGamemodesMenu());
            }
            
            if (gamePane != null) {
                if (scoreLabel1984 == null) {
                    scoreLabel1984 = new Label("Score: 0");
                    scoreLabel1984.getStyleClass().add("score-label-1984");
                    scoreLabel1984.setLayoutX(50);
                    scoreLabel1984.setLayoutY(150);
                    gamePane.getChildren().add(scoreLabel1984);
                }
                if (linesLabel1984 == null) {
                    linesLabel1984 = new Label("Lines: 0");
                    linesLabel1984.getStyleClass().add("lines-label-1984");
                    linesLabel1984.setLayoutX(50);
                    linesLabel1984.setLayoutY(200);
                    gamePane.getChildren().add(linesLabel1984);
                }
                scoreLabel1984.setVisible(true);
                linesLabel1984.setVisible(true);
                startGlitchAnimation1984();
            }
            if (rootPane != null) {
                rootPane.getChildren().removeIf(node -> node instanceof AnimatedBackground);
                rootPane.setStyle("-fx-background-image: url('blank_gs1984.png'); " +
                                 "-fx-background-size: 100% 100%; " +
                                 "-fx-background-position: center; " +
                                 "-fx-background-repeat: no-repeat;");
                
                javafx.scene.effect.ColorAdjust darkenEffect = new javafx.scene.effect.ColorAdjust();
                darkenEffect.setBrightness(-0.15);
                rootPane.setEffect(darkenEffect);
                
                if (filmGrainOverlay == null) {
                    filmGrainOverlay = new Rectangle();
                    filmGrainOverlay.widthProperty().bind(rootPane.widthProperty());
                    filmGrainOverlay.heightProperty().bind(rootPane.heightProperty());
                    filmGrainOverlay.setFill(Color.BLACK);
                    filmGrainOverlay.setOpacity(0.03);
                    filmGrainOverlay.setMouseTransparent(true);
                    rootPane.getChildren().add(filmGrainOverlay);
                }
                filmGrainOverlay.setVisible(true);
                startFilmGrainAnimation();
                startFlickerAnimation();
            }
            if (gameBoard != null) {
                gameBoard.getStyleClass().remove("gameBoard");
                gameBoard.getStyleClass().add("gameBoard-1984");
                
                if (gamePanel != null) {
                    gamePanel.getStyleClass().add("gamePanel-1984");
                }
                
                int brickSize = BRICK_SIZE_1984;
                int columns = 13;
                int visibleRows = 25;
                int gap = 1;
                int padding = 0;
                
                double boardWidth = 220; 
                double boardHeight = 445;  
                
                double centerX = (720 - boardWidth) / 2;
                double centerY = (680 - boardHeight) / 2;
                
                gameBoard.setLayoutX(centerX);
                gameBoard.setLayoutY(centerY);
                gameBoard.setPrefWidth(boardWidth);
                gameBoard.setPrefHeight(boardHeight);
                gameBoard.setMinWidth(boardWidth);
                gameBoard.setMinHeight(boardHeight);
                gameBoard.setMaxWidth(boardWidth);
                gameBoard.setMaxHeight(boardHeight);
            }
            if (pausePanel != null) {
                pausePanel.set1984Mode(true);
            }
        } else {
            if (scoreLabel1984 != null) {
                scoreLabel1984.setVisible(false);
            }
            if (linesLabel1984 != null) {
                linesLabel1984.setVisible(false);
            }
            if (glitchTimeline1984 != null) {
                glitchTimeline1984.stop();
            }
            if (filmGrainOverlay != null) {
                filmGrainOverlay.setVisible(false);
            }
            if (filmGrainTimeline != null) {
                filmGrainTimeline.stop();
            }
            if (flickerTimeline != null) {
                flickerTimeline.stop();
            }
            if (rootPane != null) {
                rootPane.setEffect(null);
                rootPane.setOpacity(1.0);
            }
        }
    }
    
    private void startGlitchAnimation1984() {
        if (glitchTimeline1984 != null) {
            glitchTimeline1984.stop();
        }
        
        glitchTimeline1984 = new Timeline(
            new KeyFrame(Duration.ZERO, e -> {
                if (scoreLabel1984 != null) {
                    scoreLabel1984.setTranslateX(0);
                    scoreLabel1984.setOpacity(1.0);
                }
                if (linesLabel1984 != null) {
                    linesLabel1984.setTranslateX(0);
                    linesLabel1984.setOpacity(1.0);
                }
            }),
            new KeyFrame(Duration.millis(50), e -> {
                if (scoreLabel1984 != null) {
                    scoreLabel1984.setTranslateX(Math.random() * 2 - 1);
                    scoreLabel1984.setOpacity(0.9 + Math.random() * 0.1);
                }
                if (linesLabel1984 != null) {
                    linesLabel1984.setTranslateX(Math.random() * 2 - 1);
                    linesLabel1984.setOpacity(0.9 + Math.random() * 0.1);
                }
            }),
            new KeyFrame(Duration.millis(100), e -> {
                if (scoreLabel1984 != null) {
                    scoreLabel1984.setTranslateX(0);
                    scoreLabel1984.setOpacity(1.0);
                }
                if (linesLabel1984 != null) {
                    linesLabel1984.setTranslateX(0);
                    linesLabel1984.setOpacity(1.0);
                }
            }),
            new KeyFrame(Duration.millis(150), e -> {
                if (scoreLabel1984 != null) {
                    scoreLabel1984.setTranslateX(Math.random() * 3 - 1.5);
                    scoreLabel1984.setOpacity(0.85 + Math.random() * 0.15);
                }
                if (linesLabel1984 != null) {
                    linesLabel1984.setTranslateX(Math.random() * 3 - 1.5);
                    linesLabel1984.setOpacity(0.85 + Math.random() * 0.15);
                }
            }),
            new KeyFrame(Duration.millis(200), e -> {
                if (scoreLabel1984 != null) {
                    scoreLabel1984.setTranslateX(0);
                    scoreLabel1984.setOpacity(1.0);
                }
                if (linesLabel1984 != null) {
                    linesLabel1984.setTranslateX(0);
                    linesLabel1984.setOpacity(1.0);
                }
            })
        );
        glitchTimeline1984.setCycleCount(Timeline.INDEFINITE);
        glitchTimeline1984.play();
    }
    
    private void startFilmGrainAnimation() {
        if (filmGrainTimeline != null) {
            filmGrainTimeline.stop();
        }
        
        filmGrainTimeline = new Timeline(
            new KeyFrame(Duration.millis(50), e -> {
                if (filmGrainOverlay != null) {
                    filmGrainOverlay.setOpacity(0.02 + Math.random() * 0.04);
                }
            })
        );
        filmGrainTimeline.setCycleCount(Timeline.INDEFINITE);
        filmGrainTimeline.play();
    }
    
    private void startFlickerAnimation() {
        if (flickerTimeline != null) {
            flickerTimeline.stop();
        }
        
        flickerTimeline = new Timeline(
            new KeyFrame(Duration.ZERO, e -> {
                if (rootPane != null) {
                    rootPane.setOpacity(1.0);
                }
            }),
            new KeyFrame(Duration.millis(2000), e -> {
                if (rootPane != null && Math.random() < 0.1) {
                    rootPane.setOpacity(0.95 + Math.random() * 0.05);
                }
            }),
            new KeyFrame(Duration.millis(2100), e -> {
                if (rootPane != null) {
                    rootPane.setOpacity(1.0);
                }
            }),
            new KeyFrame(Duration.millis(3500), e -> {
                if (rootPane != null && Math.random() < 0.15) {
                    rootPane.setOpacity(0.92 + Math.random() * 0.08);
                }
            }),
            new KeyFrame(Duration.millis(3600), e -> {
                if (rootPane != null) {
                    rootPane.setOpacity(1.0);
                }
            })
        );
        flickerTimeline.setCycleCount(Timeline.INDEFINITE);
        flickerTimeline.play();
    }
    
    public void setLevel(com.comp2042.logic.Level level) {
        this.currentLevel = level;
        if (level != null) {
            this.dropIntervalMs = level.getDropSpeed();
            this.tripleClearsCount = 0;
            this.levelStartTime = System.currentTimeMillis();
            this.totalPauseDuration = 0;
            this.pauseStartTime = 0;
            stopTimerUpdate();
            highScoreLabel.setVisible(false);
            highScoreHolderLabel.setVisible(false);
            updateLevelObjectiveLabel();
            
            if (rightPanel != null) {
                rightPanel.setLayoutX(500);
                rightPanel.setSpacing(20);
            }
            if (nextBlockContainer != null) {
                nextBlockContainer.setSpacing(15);
                nextBlockContainer.setStyle("");
            }
            if (nextBlockStackPane != null) {
                nextBlockStackPane.setMinWidth(90);
                nextBlockStackPane.setMinHeight(90);
                nextBlockStackPane.setPrefWidth(90);
                nextBlockStackPane.setPrefHeight(90);
            }
            
            if (timerLabel != null) {
                boolean needsTimer = level.getLevelNumber() == 2 || level.getLevelNumber() == 5;
                timerLabel.setVisible(needsTimer);
                if (needsTimer) {
                    startTimerUpdate();
                }
            }
        } else {
            this.dropIntervalMs = DEFAULT_DROP_INTERVAL_MS;
            boolean isInvertedMode = gameMode != null && gameMode.equals("inverted");
            if (isInvertedMode) {
                highScoreLabel.setVisible(false);
                highScoreHolderLabel.setVisible(false);
                if (objectivePanel != null) {
                    objectivePanel.setVisible(false);
                }
            } else {
                highScoreLabel.setVisible(true);
                if (HighScoreManager.getHighScoreHolder() != null && !HighScoreManager.getHighScoreHolder().isEmpty()) {
                    highScoreHolderLabel.setVisible(true);
                }
                if (objectivePanel != null) {
                    objectivePanel.setVisible(true);
                }
            }
            updateLevelObjectiveLabel();
            
            if (rightPanel != null) {
                rightPanel.setLayoutX(500);
                rightPanel.setSpacing(20);
            }
            if (nextBlockContainer != null) {
                nextBlockContainer.setSpacing(15);
                nextBlockContainer.setStyle("");
            }
            if (nextBlockStackPane != null) {
                nextBlockStackPane.setMinWidth(90);
                nextBlockStackPane.setMinHeight(90);
                nextBlockStackPane.setPrefWidth(90);
                nextBlockStackPane.setPrefHeight(90);
            }
            stopTimerUpdate();
            startFreePlayTimer();
        }
    }
    
    private void startTimerUpdate() {
        stopTimerUpdate();
        if (currentLevel != null && (currentLevel.getLevelNumber() == 2 || currentLevel.getLevelNumber() == 5)) {
            updateTimerLabel();
            timerUpdateLine = new Timeline(new KeyFrame(
                Duration.seconds(1),
                _ -> updateTimerLabel()
            ));
            timerUpdateLine.setCycleCount(Timeline.INDEFINITE);
            timerUpdateLine.play();
        }
    }
    
    private void updateTimerLabel() {
        if (currentLevel != null && timerLabel != null && timerLabel.isVisible()) {
            long currentPauseTime = (pauseStartTime > 0) ? (System.currentTimeMillis() - pauseStartTime) : 0;
            long timeElapsed = (System.currentTimeMillis() - levelStartTime - totalPauseDuration - currentPauseTime) / 1000;
            int remainingTime = (int)(currentLevel.getTimeLimit() - timeElapsed);
            int minutes = Math.max(0, remainingTime) / 60;
            int seconds = Math.max(0, remainingTime) % 60;
            String timeStr = String.format("%d:%02d", minutes, seconds);
            timerLabel.setText("Time: " + timeStr);
        }
    }

    private void stopTimerUpdate() {
        if (timerUpdateLine != null) {
            timerUpdateLine.stop();
            timerUpdateLine = null;
        }
    }
    
    private void startFreePlayTimer() {
        stopFreePlayTimer();
        if (currentLevel == null && (gameMode == null || !gameMode.equals("inverted"))) {
            freePlayStartTime = System.currentTimeMillis();
            freePlayTotalPauseDuration = 0;
            freePlayPauseStartTime = 0;
            if (timerLabel != null) {
                timerLabel.setVisible(true);
                updateFreePlayTimer();
            }
            freePlayTimerLine = new Timeline(new KeyFrame(
                Duration.seconds(1),
                _ -> updateFreePlayTimer()
            ));
            freePlayTimerLine.setCycleCount(Timeline.INDEFINITE);
            freePlayTimerLine.play();
        }
    }
    
    private void updateFreePlayTimer() {
        if (currentLevel == null && timerLabel != null && 
            (gameMode == null || !gameMode.equals("inverted"))) {
            if (freePlayStartTime == 0) {
                timerLabel.setText("Time: 0:00");
                return;
            }
            long currentPauseTime = (freePlayPauseStartTime > 0) ? (System.currentTimeMillis() - freePlayPauseStartTime) : 0;
            long timeElapsed = (System.currentTimeMillis() - freePlayStartTime - freePlayTotalPauseDuration - currentPauseTime) / 1000;
            timeElapsed = Math.max(0, timeElapsed);
            int minutes = (int)(timeElapsed / 60);
            int seconds = (int)(timeElapsed % 60);
            String timeStr = String.format("%d:%02d", minutes, seconds);
            timerLabel.setText("Time: " + timeStr);
        }
    }
    
    private void stopFreePlayTimer() {
        if (freePlayTimerLine != null) {
            freePlayTimerLine.stop();
            freePlayTimerLine = null;
        }
    }
    
    private long getFreePlayElapsedTime() {
        if (currentLevel != null || (gameMode != null && gameMode.equals("inverted"))) {
            return 0;
        }
        if (freePlayStartTime == 0) {
            return 0;
        }
        long currentPauseTime = (freePlayPauseStartTime > 0) ? (System.currentTimeMillis() - freePlayPauseStartTime) : 0;
        long elapsed = (System.currentTimeMillis() - freePlayStartTime - freePlayTotalPauseDuration - currentPauseTime) / 1000;
        return Math.max(0, elapsed);
    }
    
    private void updateLevelObjectiveLabel() {
        if (objectiveLabel != null) {
            boolean isInvertedMode = gameMode != null && gameMode.equals("inverted");
            
            if (currentLevel == null) {
                if (isInvertedMode) {
                    objectiveLabel.setVisible(false);
                } else {
                    String playerDisplayName = "";
                    if (playerName != null && !playerName.isEmpty()) {
                        String capitalizedName = playerName.substring(0, 1).toUpperCase() + 
                                                (playerName.length() > 1 ? playerName.substring(1).toLowerCase() : "");
                        playerDisplayName = capitalizedName;
                    } else {
                        playerDisplayName = "Player";
                    }
                    
                    String highScoreHolder = HighScoreManager.getHighScoreHolder();
                    String message = "Keep Going " + playerDisplayName + "!";
                    if (highScoreHolder != null && !highScoreHolder.isEmpty()) {
                        String capitalizedHolder = highScoreHolder.substring(0, 1).toUpperCase() + 
                                                  (highScoreHolder.length() > 1 ? highScoreHolder.substring(1).toLowerCase() : "");
                        message += " You can beat " + capitalizedHolder + "'s Highscore!!!";
                    } else {
                        message += " Set a new Highscore!!!";
                    }
                    objectiveLabel.setText(message);
                    objectiveLabel.setVisible(true);
                }
            } else {
                String objectiveText = "Objective: " + currentLevel.getObjective();
                objectiveLabel.setText(objectiveText);
                objectiveLabel.setVisible(true);
            }
        }
        
        if (highScoreLabel != null && currentLevel != null) {
            highScoreLabel.setVisible(false);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        URL digitalFontUrl = getClass().getClassLoader().getResource("digital.ttf");
        if (digitalFontUrl != null) {
            Font.loadFont(digitalFontUrl.toExternalForm(), 38);
        }
        HighScoreManager.ensureDirectoryExists();
        LeaderboardManager.ensureDirectoryExists();
        LevelProgressManager.ensureDirectoryExists();
        LevelProgressManager.initialize();
        SettingsManager.ensureDirectoryExists();
        
        refreshHighScoreDisplay();
        
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(this::handleKeyPressed);
        gameOverPanel.setVisible(false);
        gameOverContainer.setVisible(false);
        pausePanel.setVisible(false);
        pauseContainer.setVisible(false);
        if (levelCompleteContainer != null) {
            levelCompleteContainer.setVisible(false);
        }
        if (countdownContainer != null) {
            countdownContainer.setVisible(false);
    }
        
        updateLevelObjectiveLabel();

        gameOverPanel.getRestartButton().setOnAction(_ -> newGame(null));
        gameOverPanel.getMainMenuButton().setOnAction(_ -> returnToMainMenu());
        gameOverPanel.getLeaderboardButton().setOnAction(_ -> showLeaderboard());
        
        boolean is1984ModeInit = gameMode != null && gameMode.equals("1984");
        if (is1984ModeInit && gameOverPanel.getBackButton1984() != null) {
            gameOverPanel.getBackButton1984().setOnAction(_ -> returnToGamemodesMenu());
        }

        pausePanel.getResumeButton().setOnAction(_ -> resumeGame());
        pausePanel.getRestartButton().setOnAction(_ -> newGame(null));
        pausePanel.getSettingsButton().setOnAction(_ -> openSettingsFromPause());
        pausePanel.getLeaderboardButton().setOnAction(_ -> showLeaderboard());
        pausePanel.getMainMenuButton().setOnAction(_ -> returnToMainMenu());
        
        boolean is1984Mode = gameMode != null && gameMode.equals("1984");
        if (is1984Mode && pausePanel != null) {
            pausePanel.set1984Mode(true);
        }

        if (!is1984Mode) {
            AnimatedBackground animatedBackground = new AnimatedBackground(720, 680);
            rootPane.getChildren().addFirst(animatedBackground);
        }

        setupLeaderboardPanel();
        setupHoldBlockAnimation();
        
        if (backButton != null) {
            boolean is1984ModeCheck = gameMode != null && gameMode.equals("1984");
            if (is1984ModeCheck) {
                backButton.setOnAction(_ -> returnToGamemodesMenu());
            } else {
                backButton.setOnAction(_ -> returnToPlayMenu());
            }
        }
    }
    
    private void setupHoldBlockAnimation() {
        if (holdBlockContainer != null) {
            ScaleTransition pulse = new ScaleTransition(Duration.seconds(3.0), holdBlockContainer);
            pulse.setFromX(1.0);
            pulse.setFromY(1.0);
            pulse.setToX(1.02);
            pulse.setToY(1.02);
            pulse.setCycleCount(Timeline.INDEFINITE);
            pulse.setAutoReverse(true);
            pulse.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);
            pulse.play();
            
            FadeTransition glow = new FadeTransition(Duration.seconds(3.0), holdBlockContainer);
            glow.setFromValue(0.95);
            glow.setToValue(1.0);
            glow.setCycleCount(Timeline.INDEFINITE);
            glow.setAutoReverse(true);
            glow.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);
            glow.play();
        }
    }

    private void setupLeaderboardPanel() {
        leaderboardPanel = new LeaderboardPanel();
        leaderboardContainer = new VBox(leaderboardPanel);
        leaderboardContainer.setAlignment(javafx.geometry.Pos.CENTER);
        leaderboardContainer.setVisible(false);
        leaderboardPanel.getCloseButton().setOnAction(_ -> {
            leaderboardContainer.setVisible(false);
            gamePanel.requestFocus();
        });
        rootPane.getChildren().add(leaderboardContainer);
    }

    private void showLeaderboard() {
        leaderboardPanel.refreshEntries();
        leaderboardContainer.setVisible(true);
    }

    private void resumeGame() {
        pausePanel.setVisible(false);
        pauseContainer.setVisible(false);
        startCountdown();
    }

    private void openSettingsFromPause() {
        try {
            URL location = getClass().getClassLoader().getResource("settings.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            Parent root = fxmlLoader.load();
            SettingsController settingsController = fxmlLoader.getController();
            settingsController.setStage(stage);
            settingsController.setReturnToGame(stage.getScene(), this);
            Scene scene = new Scene(root, 720, 680);
            stage.setScene(scene);
        } catch (IOException ignored) {
        }
    }

    private void returnToMainMenu() {
        if (timeLine != null) {
            timeLine.stop();
        }
        stopTimerUpdate();
        MainMenuController.clearActiveGame();
        try {
            URL location = getClass().getClassLoader().getResource("mainMenu.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            Parent root = fxmlLoader.load();
            MainMenuController menuController = fxmlLoader.getController();
            menuController.setStage(stage);
            Scene scene = new Scene(root, 720, 680);
            stage.setScene(scene);
        } catch (IOException ignored) {
        }
    }
    
    private void returnToPlayMenu() {
        if (timeLine != null) {
            timeLine.stop();
        }
        stopTimerUpdate();
        stopFreePlayTimer();
        MainMenuController.clearActiveGame();
        try {
            URL location = getClass().getClassLoader().getResource("mainMenu.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            Parent root = fxmlLoader.load();
            MainMenuController menuController = fxmlLoader.getController();
            menuController.setStage(stage);
            menuController.showPlayMenu();
            Scene scene = new Scene(root, 720, 680);
            stage.setScene(scene);
        } catch (IOException ignored) {
        }
    }
    private void handleKeyPressed(KeyEvent keyEvent) {
        if (isGameOver.get()) {
            keyEvent.consume();
            return;
        }
        
        boolean isInvertedMode = gameMode != null && gameMode.equals("inverted");
        
        if (!isPause.get() && eventListener != null) {
            if (isInvertedMode) {
                // Inverted mode: swap key mappings
                if (isRotateKey(keyEvent)) {
                    // UP/W now does DOWN (soft drop)
                    moveDown(userMove(EventType.DOWN));
                    keyEvent.consume();
                } else if (isDownKey(keyEvent)) {
                    // DOWN/S now does ROTATE
                    refreshBrick(eventListener.onRotateEvent(userMove(EventType.ROTATE)));
                    keyEvent.consume();
                } else if (isLeftKey(keyEvent)) {
                    // LEFT/A now does RIGHT
                    refreshBrick(eventListener.onRightEvent(userMove(EventType.RIGHT)));
                    keyEvent.consume();
                } else if (isRightKey(keyEvent)) {
                    // RIGHT/D now does LEFT
                    refreshBrick(eventListener.onLeftEvent(userMove(EventType.LEFT)));
                    keyEvent.consume();
                } else if (keyEvent.getCode() == KeyCode.SPACE) {
                    hardDrop();
                    keyEvent.consume();
                } else if (keyEvent.getCode() == KeyCode.H) {
                    holdBrick();
                    keyEvent.consume();
                }
            } else {
                // Normal mode: standard key mappings
            if (isLeftKey(keyEvent)) {
                refreshBrick(eventListener.onLeftEvent(userMove(EventType.LEFT)));
                keyEvent.consume();
            } else if (isRightKey(keyEvent)) {
                refreshBrick(eventListener.onRightEvent(userMove(EventType.RIGHT)));
                keyEvent.consume();
            } else if (isRotateKey(keyEvent)) {
                refreshBrick(eventListener.onRotateEvent(userMove(EventType.ROTATE)));
                keyEvent.consume();
            } else if (isDownKey(keyEvent)) {
                moveDown(userMove(EventType.DOWN));
                keyEvent.consume();
                } else if (keyEvent.getCode() == KeyCode.SPACE) {
                    hardDrop();
                    keyEvent.consume();
                } else if (keyEvent.getCode() == KeyCode.H) {
                    holdBrick();
                keyEvent.consume();
                }
            }
        }

        if (!isGameOver.get()) {
        if (keyEvent.getCode() == KeyCode.N) {
            newGame(null);
            } else if (keyEvent.getCode() == KeyCode.ESCAPE) {
            pauseGame(null);
            }
        }
    }


    public void initGameView(int[][] boardMatrix, ViewData brick) {
        int brickSize = getBrickSize();
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = HIDDEN_ROWS; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(brickSize, brickSize);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - HIDDEN_ROWS);
            }
        }

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        ghostRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(brickSize, brickSize);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
                
                Rectangle ghostRect = new Rectangle(brickSize, brickSize);
                ghostRect.setFill(Color.TRANSPARENT);
                ghostRectangles[i][j] = ghostRect;
                ghostPanel.add(ghostRect, j, i);
            }
        }
        updateBrickPanelPosition(brick);
        updateGhostPosition(brick);
        initNextBlockPanel(brick);
        initHoldBlockPanel(brick);

        timeLine = new Timeline(new KeyFrame(
                Duration.millis(dropIntervalMs),
                _ -> moveDown(threadMove())
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }
    private void updateBrickPanelPosition(ViewData brick) {
        boolean is1984Mode = gameMode != null && gameMode.equals("1984");
        int brickSize = getBrickSize();
        double cellSize = brickPanel.getVgap() + brickSize;
        double padding = is1984Mode ? 0 : BOARD_PADDING;
        double x = gameBoard.getLayoutX() + padding + brick.getxPosition() * cellSize;
        double y = BRICK_PANEL_Y_OFFSET + gameBoard.getLayoutY() + padding
                + (brick.getyPosition() - HIDDEN_ROWS) * cellSize;

        brickPanel.setLayoutX(x);
        brickPanel.setLayoutY(y);
    }

    private void updateGhostPosition(ViewData brick) {
        boolean is1984Mode = gameMode != null && gameMode.equals("1984");
        // Check if ghost block is enabled or if in 1984 mode (no ghost in retro mode)
        if (is1984Mode || !SettingsManager.isGhostBlockEnabled()) {
            // Hide all ghost rectangles
            if (ghostRectangles != null) {
                for (int i = 0; i < ghostRectangles.length; i++) {
                    for (int j = 0; j < ghostRectangles[i].length; j++) {
                        ghostRectangles[i][j].setFill(Color.TRANSPARENT);
                        ghostRectangles[i][j].setStroke(Color.TRANSPARENT);
                        ghostRectangles[i][j].setOpacity(0.0);
                    }
                }
            }
            return;
        }
        
        int brickSize = getBrickSize();
        double cellSize = ghostPanel.getVgap() + brickSize;
        double x = gameBoard.getLayoutX() + BOARD_PADDING + brick.getxPosition() * cellSize;
        double ghostY = gameBoard.getLayoutY() + BOARD_PADDING
                + (brick.getGhostY() - HIDDEN_ROWS) * cellSize;

        ghostPanel.setLayoutX(x);
        ghostPanel.setLayoutY(ghostY);

        // Update ghost block with improved performance and color matching
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle ghostRect = ghostRectangles[i][j];
                int colorIndex = brick.getBrickData()[i][j];
                boolean shouldShow = colorIndex != 0;
                boolean currentlyVisible = !ghostRect.getFill().equals(Color.TRANSPARENT) && ghostRect.getOpacity() > 0.01;
                
                if (shouldShow) {
                    // Get the actual color of the block and make it ghostly
                    Paint blockColor = getFillColor(colorIndex);
                    if (blockColor instanceof Color) {
                        Color originalColor = (Color) blockColor;
                        // Create a ghost version with reduced opacity and lighter appearance
                        Color ghostColor = Color.color(
                            originalColor.getRed(),
                            originalColor.getGreen(),
                            originalColor.getBlue(),
                            0.25 // Reduced opacity for ghost effect
                        );
                        ghostRect.setFill(ghostColor);
                    } else {
                        ghostRect.setFill(Color.rgb(255, 255, 255, 0.25));
                    }
                    
                    ghostRect.setArcHeight(RECTANGLE_ARC_SIZE);
                    ghostRect.setArcWidth(RECTANGLE_ARC_SIZE);
                    
                    // Cleaner border with matching color
                    if (blockColor instanceof Color) {
                        Color originalColor = (Color) blockColor;
                        Color borderColor = Color.color(
                            originalColor.getRed(),
                            originalColor.getGreen(),
                            originalColor.getBlue(),
                            0.4
                        );
                        ghostRect.setStroke(borderColor);
                    } else {
                        ghostRect.setStroke(Color.rgb(255, 255, 255, 0.4));
                    }
                    ghostRect.setStrokeWidth(1.5);
                    
                    // Smooth fade in only when appearing
                    if (!currentlyVisible) {
                        ghostRect.setOpacity(0.0);
                        FadeTransition fadeIn = new FadeTransition(Duration.millis(100), ghostRect);
                        fadeIn.setFromValue(0.0);
                        fadeIn.setToValue(1.0);
                        fadeIn.play();
                    } else {
                        ghostRect.setOpacity(1.0);
                    }
                } else {
                    // Smooth fade out when disappearing
                    if (currentlyVisible) {
                        FadeTransition fadeOut = new FadeTransition(Duration.millis(80), ghostRect);
                        fadeOut.setFromValue(ghostRect.getOpacity());
                        fadeOut.setToValue(0.0);
                        fadeOut.setOnFinished(_ -> {
                            ghostRect.setFill(Color.TRANSPARENT);
                            ghostRect.setStroke(Color.TRANSPARENT);
                            ghostRect.setOpacity(0.0);
                        });
                        fadeOut.play();
                    } else {
                        ghostRect.setFill(Color.TRANSPARENT);
                        ghostRect.setStroke(Color.TRANSPARENT);
                        ghostRect.setOpacity(0.0);
                    }
                }
            }
        }
    }
    
    public void updateGhostBlockVisibility() {
        // Update ghost block visibility immediately if we have current data
        if (lastViewData != null) {
            updateGhostPosition(lastViewData);
        } else if (ghostRectangles != null) {
            // Hide all ghost rectangles if no game is active
            for (int i = 0; i < ghostRectangles.length; i++) {
                for (int j = 0; j < ghostRectangles[i].length; j++) {
                    ghostRectangles[i][j].setFill(Color.TRANSPARENT);
                    ghostRectangles[i][j].setStroke(Color.TRANSPARENT);
                    ghostRectangles[i][j].setOpacity(0.0);
                }
            }
        }
    }

    private void initNextBlockPanel(ViewData brick) {
        int brickSize = getBrickSize();
        boolean is1984Mode = gameMode != null && gameMode.equals("1984");
        int[][] nextData = brick.getNextBrickData();
        nextBlockRectangles = new Rectangle[nextData.length][nextData[0].length];
        for (int i = 0; i < nextData.length; i++) {
            for (int j = 0; j < nextData[i].length; j++) {
                Rectangle rect = new Rectangle(brickSize, brickSize);
                if (is1984Mode) {
                    rect.setArcHeight(2);
                    rect.setArcWidth(2);
                } else {
                    rect.setArcHeight(RECTANGLE_ARC_SIZE);
                    rect.setArcWidth(RECTANGLE_ARC_SIZE);
                }
                nextBlockRectangles[i][j] = rect;
                nextBlockPanel.add(rect, j, i);
            }
        }
        updateNextBlock(brick);
    }

    private void updateNextBlock(ViewData brick) {
        int[][] nextData = brick.getNextBrickData();
        for (int i = 0; i < nextData.length; i++) {
            for (int j = 0; j < nextData[i].length; j++) {
                if (nextData[i][j] != 0) {
                    nextBlockRectangles[i][j].setFill(getFillColor(nextData[i][j]));
                } else {
                    nextBlockRectangles[i][j].setFill(Color.TRANSPARENT);
                }
            }
        }
    }
    
    private void initHoldBlockPanel(ViewData brick) {
        int brickSize = getBrickSize();
        boolean is1984Mode = gameMode != null && gameMode.equals("1984");
        int maxSize = 4;
        holdBlockRectangles = new Rectangle[maxSize][maxSize];
        previousHoldData = null;
        for (int i = 0; i < maxSize; i++) {
            for (int j = 0; j < maxSize; j++) {
                Rectangle rect = new Rectangle(brickSize, brickSize);
                if (is1984Mode) {
                    rect.setArcHeight(2);
                    rect.setArcWidth(2);
                } else {
                    rect.setArcHeight(RECTANGLE_ARC_SIZE);
                    rect.setArcWidth(RECTANGLE_ARC_SIZE);
                }
                rect.setFill(Color.TRANSPARENT);
                holdBlockRectangles[i][j] = rect;
                holdBlockPanel.add(rect, j, i);
            }
        }
        updateHoldBlock(brick);
    }
    
    private void updateHoldBlock(ViewData brick) {
        int[][] holdData = brick.getHoldBrickData();
        boolean holdChanged = false;
        
        if (previousHoldData == null && holdData != null) {
            holdChanged = true;
        } else if (previousHoldData != null && holdData != null) {
            if (previousHoldData.length != holdData.length || 
                (holdData.length > 0 && previousHoldData.length > 0 && 
                 (previousHoldData[0].length != holdData[0].length))) {
                holdChanged = true;
            } else {
                for (int i = 0; i < holdData.length && !holdChanged; i++) {
                    for (int j = 0; j < holdData[i].length && !holdChanged; j++) {
                        int prevVal = (i < previousHoldData.length && j < previousHoldData[i].length) ? previousHoldData[i][j] : 0;
                        int currVal = holdData[i][j];
                        if (prevVal != currVal) {
                            holdChanged = true;
                        }
                    }
                }
            }
        } else if (previousHoldData != null && holdData == null) {
            holdChanged = true;
        }
        
        for (int i = 0; i < holdBlockRectangles.length; i++) {
            for (int j = 0; j < holdBlockRectangles[i].length; j++) {
                if (holdData != null && i < holdData.length && j < holdData[i].length && holdData[i][j] != 0) {
                    holdBlockRectangles[i][j].setFill(getFillColor(holdData[i][j]));
                } else {
                    holdBlockRectangles[i][j].setFill(Color.TRANSPARENT);
                }
            }
        }
        
        if (holdChanged && holdData != null) {
            previousHoldData = new int[holdData.length][];
            for (int i = 0; i < holdData.length; i++) {
                previousHoldData[i] = holdData[i].clone();
            }
            spinHoldBlock();
        } else if (previousHoldData == null) {
            previousHoldData = holdData != null ? new int[holdData.length][] : null;
            if (previousHoldData != null) {
                for (int i = 0; i < holdData.length; i++) {
                    previousHoldData[i] = holdData[i].clone();
                }
            }
        }
    }
    
    private void holdBrick() {
        if (eventListener != null) {
            ViewData newData = eventListener.onHoldEvent();
            refreshBrick(newData);
            updateHoldBlock(newData);
        }
    }
    
    private void spinHoldBlock() {
        if (holdBlockContainer != null) {
            holdBlockContainer.setScaleX(1.0);
            holdBlockContainer.setScaleY(1.0);
            holdBlockContainer.setTranslateX(0);
            holdBlockContainer.setTranslateY(0);
            
            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), holdBlockContainer);
            scaleUp.setFromX(1.0);
            scaleUp.setFromY(1.0);
            scaleUp.setToX(1.15);
            scaleUp.setToY(1.15);
            scaleUp.setCycleCount(1);
            scaleUp.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
            
            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(300), holdBlockContainer);
            scaleDown.setFromX(1.15);
            scaleDown.setFromY(1.15);
            scaleDown.setToX(1.0);
            scaleDown.setToY(1.0);
            scaleDown.setCycleCount(1);
            scaleDown.setInterpolator(javafx.animation.Interpolator.EASE_IN);
            
            SequentialTransition sequence = new SequentialTransition(scaleUp, scaleDown);
            sequence.play();
        }
    }

    private boolean isLeftKey(KeyEvent event) {
        KeyCode code = event.getCode();
        return code == KeyCode.LEFT || code == KeyCode.A;
    }
    private boolean isRightKey(KeyEvent event) {
        KeyCode code = event.getCode();
        return code == KeyCode.RIGHT || code == KeyCode.D;
    }
    private boolean isRotateKey(KeyEvent event) {
        KeyCode code = event.getCode();
        return code == KeyCode.UP || code == KeyCode.W;
    }
    private boolean isDownKey(KeyEvent event) {
        KeyCode code = event.getCode();
        return code == KeyCode.DOWN || code == KeyCode.S;
    }
    private MoveEvent userMove(EventType type) {
        return new MoveEvent(type, EventSource.USER);
    }

    private MoveEvent threadMove() {
        return new MoveEvent(EventType.DOWN, EventSource.THREAD);
    }
    private Paint getFillColor(int i) {
        boolean is1984Mode = gameMode != null && gameMode.equals("1984");
        
        if (is1984Mode && i != 0) {
            return Color.WHITE;
        }
        
        return switch (i) {
            case 0 -> Color.TRANSPARENT;
            case 1 -> Color.AQUA;
            case 2 -> Color.BLUEVIOLET;
            case 3 -> Color.DARKGREEN;
            case 4 -> Color.YELLOW;
            case 5 -> Color.RED;
            case 6 -> Color.BEIGE;
            case 7 -> Color.BURLYWOOD;
            default -> Color.WHITE;
        };
    }
    
    private int getBrickSize() {
        boolean is1984Mode = gameMode != null && gameMode.equals("1984");
        return is1984Mode ? BRICK_SIZE_1984 : BRICK_SIZE;
    }


    private void refreshBrick(ViewData brick) {
        if (!isPause.get()) {
            lastViewData = brick; // Store for ghost block updates
            updateBrickPanelPosition(brick);
            updateGhostPosition(brick);
            updateNextBlock(brick);
            updateHoldBlock(brick);
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }
            }
        }
    }

    public void refreshGameBackground(int[][] board) {
        for (int i = HIDDEN_ROWS; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void setRectangleData(int colorIndex, Rectangle rectangle) {
        rectangle.setFill(getFillColor(colorIndex));
        boolean is1984Mode = gameMode != null && gameMode.equals("1984");
        if (is1984Mode) {
            rectangle.setArcHeight(2);
            rectangle.setArcWidth(2);
        } else {
            rectangle.setArcHeight(RECTANGLE_ARC_SIZE);
            rectangle.setArcWidth(RECTANGLE_ARC_SIZE);
        }
    }
    

    private void moveDown(MoveEvent event) {
        if (!isPause.get()) {
            DownData downData = eventListener.onDownEvent(event);
            if (shouldShowScoreNotification(downData)) {
                showScoreNotification(downData.getClearRow().getScoreBonus());
            }
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    private void hardDrop() {
        if (!isPause.get()) {
            DownData downData = eventListener.onHardDropEvent();
            if (shouldShowScoreNotification(downData)) {
                showScoreNotification(downData.getClearRow().getScoreBonus());
            }
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    private boolean shouldShowScoreNotification(DownData downData) {
        return downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0;
    }

    private void showScoreNotification(int scoreBonus) {
        NotificationPanel notificationPanel = new NotificationPanel("+" + scoreBonus);
        groupNotification.getChildren().add(notificationPanel);
        notificationPanel.showScore(groupNotification.getChildren());
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty scoreProperty) {
        this.currentScoreProperty = scoreProperty;
        scoreLabel.textProperty().bind(scoreProperty.asString("Score: %d"));
        boolean is1984Mode = gameMode != null && gameMode.equals("1984");
        if (is1984Mode && gamePane != null) {
            if (scoreLabel1984 == null) {
                scoreLabel1984 = new Label("Score: 0");
                scoreLabel1984.getStyleClass().add("score-label-1984");
                scoreLabel1984.setLayoutX(50);
                scoreLabel1984.setLayoutY(150);
                gamePane.getChildren().add(scoreLabel1984);
            }
            scoreLabel1984.textProperty().bind(scoreProperty.asString("Score: %d"));
            scoreLabel1984.setVisible(true);
            if (glitchTimeline1984 == null || glitchTimeline1984.getStatus() != javafx.animation.Animation.Status.RUNNING) {
                startGlitchAnimation1984();
            }
        }
        scoreProperty.addListener((_, oldVal, newVal) -> {
            if (newVal.intValue() > oldVal.intValue()) {
                animateLabel(scoreLabel);
            }
        });
    }

    public void bindLines(IntegerProperty linesProperty) {
        linesLabel.textProperty().bind(linesProperty.asString("Lines: %d"));
        boolean is1984Mode = gameMode != null && gameMode.equals("1984");
        if (is1984Mode && gamePane != null) {
            if (linesLabel1984 == null) {
                linesLabel1984 = new Label("Lines: 0");
                linesLabel1984.getStyleClass().add("lines-label-1984");
                linesLabel1984.setLayoutX(50);
                linesLabel1984.setLayoutY(200);
                gamePane.getChildren().add(linesLabel1984);
            }
            linesLabel1984.textProperty().bind(linesProperty.asString("Lines: %d"));
            linesLabel1984.setVisible(true);
        }
        linesProperty.addListener((_, oldVal, newVal) -> {
            if (newVal.intValue() > oldVal.intValue()) {
                animateLabel(linesLabel);
                if (currentLevel != null) {
                    checkLevelObjective();
                }
            }
        });
    }
    
    public void onLinesCleared(int linesCount) {
        if (currentLevel != null && linesCount == 3) {
            tripleClearsCount++;
        }
        if (currentLevel != null) {
            checkLevelObjective();
        }
    }
    
    private void checkLevelObjective() {
        if (currentLevel == null || eventListener == null) {
            return;
        }
        
        IntegerProperty linesProp = ((com.comp2042.logic.GameController) eventListener).getLinesProperty();
        int totalLines = linesProp != null ? linesProp.get() : 0;
        long currentPauseTime = (pauseStartTime > 0) ? (System.currentTimeMillis() - pauseStartTime) : 0;
        long timeElapsed = (System.currentTimeMillis() - levelStartTime - totalPauseDuration - currentPauseTime) / 1000;
        
        boolean objectiveMet = false;
        
        if (currentLevel.getLevelNumber() == 1) {
            objectiveMet = totalLines >= currentLevel.getTargetLines();
        } else if (currentLevel.getLevelNumber() == 2) {
            objectiveMet = totalLines >= currentLevel.getTargetLines() && 
                          timeElapsed <= currentLevel.getTimeLimit();
        } else if (currentLevel.getLevelNumber() == 3) {
            objectiveMet = tripleClearsCount >= 2;
        } else if (currentLevel.getLevelNumber() == 4) {
            int currentScore = currentScoreProperty != null ? currentScoreProperty.get() : 0;
            objectiveMet = currentScore >= currentLevel.getTargetScore();
        } else if (currentLevel.getLevelNumber() == 5) {
            objectiveMet = totalLines >= currentLevel.getTargetLines() && 
                          timeElapsed <= currentLevel.getTimeLimit();
        }
        
        updateLevelProgress(totalLines, timeElapsed);
        
        if (currentLevel.getLevelNumber() == 2 || currentLevel.getLevelNumber() == 5) {
            if (timeElapsed > currentLevel.getTimeLimit()) {
                if (timeLine != null) {
                    timeLine.stop();
                }
                gameOver();
                return;
            }
        }
        
        if (objectiveMet) {
            currentLevel.setCompleted(true);
            LevelProgressManager.saveLevelProgress();
            showLevelComplete();
        }
    }
    
    private void updateLevelProgress(int totalLines, long timeElapsedParam) {
        if (currentLevel == null) {
            return;
        }
        
        String progressText = "";
        
        if (currentLevel.getLevelNumber() == 1) {
            progressText = "Lines: " + totalLines + "/" + currentLevel.getTargetLines();
        } else if (currentLevel.getLevelNumber() == 2) {
            progressText = "Lines: " + totalLines + "/" + currentLevel.getTargetLines();
        } else if (currentLevel.getLevelNumber() == 3) {
            progressText = "Triple Clears: " + tripleClearsCount + "/2";
        } else if (currentLevel.getLevelNumber() == 4) {
            int currentScore = currentScoreProperty != null ? currentScoreProperty.get() : 0;
            progressText = "Score: " + currentScore + "/" + currentLevel.getTargetScore();
        } else if (currentLevel.getLevelNumber() == 5) {
            progressText = "Lines: " + totalLines + "/" + currentLevel.getTargetLines();
            if (timerLabel != null) {
                timerLabel.setVisible(true);
            }
        } else {
            if (timerLabel != null) {
                timerLabel.setVisible(false);
            }
        }
        
        if (objectiveLabel != null && !progressText.isEmpty()) {
            objectiveLabel.setText(progressText);
        }
    }
    
    private void showLevelComplete() {
        setPaused(true);
        if (levelCompletePanel != null && levelCompleteContainer != null && currentLevel != null) {
            if (gamePane != null) {
                gamePane.setVisible(false);
            }
            
            int nextLevelNumber = currentLevel.getLevelNumber() + 1;
            boolean hasNextLevel = LevelManager.getLevel(nextLevelNumber) != null;
            
            levelCompletePanel.showLevelComplete(
                currentLevel.getName(), 
                currentLevel.getLevelNumber(),
                hasNextLevel
            );
            
            levelCompleteContainer.setVisible(true);
            
            levelCompletePanel.getNextLevelButton().setOnAction(_ -> {
                com.comp2042.logic.Level nextLevel = LevelManager.getLevel(nextLevelNumber);
                if (nextLevel != null) {
                    levelCompleteContainer.setVisible(false);
                    if (gamePane != null) {
                        gamePane.setVisible(true);
                    }
                    setLevel(nextLevel);
                    startLevelGame();
                }
            });
            
            levelCompletePanel.getLevelsMenuButton().setOnAction(_ -> {
                levelCompleteContainer.setVisible(false);
                if (gamePane != null) {
                    gamePane.setVisible(true);
                }
                returnToLevelsMenu();
            });
            
            levelCompletePanel.getMainMenuButton().setOnAction(_ -> {
                levelCompleteContainer.setVisible(false);
                if (gamePane != null) {
                    gamePane.setVisible(true);
                }
                returnToMainMenu();
            });
        }
    }
    
    private void returnToLevelsMenu() {
        if (timeLine != null) {
            timeLine.stop();
        }
        try {
            URL location = getClass().getClassLoader().getResource("levels.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            Parent root = fxmlLoader.load();
            LevelsController levelsController = fxmlLoader.getController();
            levelsController.setStage(stage);
            Scene scene = new Scene(root, 720, 680);
            stage.setScene(scene);
        } catch (IOException ignored) {
        }
    }

    private void animateLabel(Label label) {
        ScaleTransition pulse = new ScaleTransition(Duration.millis(150), label);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.3);
        pulse.setToY(1.3);
        pulse.setCycleCount(2);
        pulse.setAutoReverse(true);
        pulse.play();
    }

    public void gameOver() {
        setPaused(true);
        gamePane.setVisible(false);
        
        int finalScore = currentScoreProperty != null ? currentScoreProperty.get() : 0;
        boolean isNewHighScore = false;
        boolean isInvertedMode = gameMode != null && gameMode.equals("inverted");
        boolean is1984Mode = gameMode != null && gameMode.equals("1984");
        
        if (!isGuest && playerName != null && !playerName.isEmpty() && !isInvertedMode && !is1984Mode && currentLevel == null) {
            isNewHighScore = HighScoreManager.updateHighScore(finalScore, playerName);
        }
        
        long timePlayed = 0;
        if (currentLevel == null && !isInvertedMode && !is1984Mode) {
            stopFreePlayTimer();
            timePlayed = getFreePlayElapsedTime();
        }
        
        int highScore = 0;
        String highScoreHolder = null;
        if (!isInvertedMode && !is1984Mode && currentLevel == null) {
            highScore = HighScoreManager.getHighScore();
            highScoreHolder = HighScoreManager.getHighScoreHolder();
            highScoreLabel.setText("High Score: " + highScore);
            if (highScoreHolder != null && !highScoreHolder.isEmpty()) {
                String capitalizedName = highScoreHolder.substring(0, 1).toUpperCase() + 
                                        (highScoreHolder.length() > 1 ? highScoreHolder.substring(1).toLowerCase() : "");
                highScoreHolderLabel.setText("by " + capitalizedName);
                highScoreHolderLabel.setVisible(true);
                animateHighScoreHolder();
            } else {
                highScoreHolderLabel.setVisible(false);
            }
        } else {
            highScoreLabel.setVisible(false);
            highScoreHolderLabel.setVisible(false);
        }
        
        gameOverPanel.setGameMode(gameMode);
        gameOverPanel.showFinalScore(finalScore, highScore, isNewHighScore, playerName, isGuest, timePlayed);
        
        if (is1984Mode && gameOverPanel.getBackButton1984() != null) {
            gameOverPanel.getBackButton1984().setOnAction(_ -> returnToGamemodesMenu());
        }
        
        gameOverPanel.setVisible(true);
        gameOverContainer.setVisible(true);
        
        if (gameMode != null && gameMode.equals("inverted") && gameOverPanel != null) {
            if (gameOverPanel.getBackButtonInverted() != null) {
                gameOverPanel.getBackButtonInverted().setOnAction(_ -> returnToGamemodesMenu());
                // Rotate back button 180 degrees
                Button backBtnInverted = gameOverPanel.getBackButtonInverted();
                backBtnInverted.getTransforms().removeIf(transform -> transform instanceof Rotate);
                Platform.runLater(() -> {
                    double width = backBtnInverted.getBoundsInLocal().getWidth();
                    double height = backBtnInverted.getBoundsInLocal().getHeight();
                    if (width > 0 && height > 0) {
                        Rotate rotate = new Rotate(180, width / 2, height / 2);
                        backBtnInverted.getTransforms().add(rotate);
                    } else {
                        Rotate rotate = new Rotate(180, 25, 25);
                        backBtnInverted.getTransforms().add(rotate);
                    }
                });
            }
            gameOverPanel.applyInvertedLayout();
            Button restartBtn = gameOverPanel.getRestartButton();
            Button mainMenuBtn = gameOverPanel.getMainMenuButton();
            Label gameOverLbl = gameOverPanel.getGameOverLabel();
            Label scoreLbl = gameOverPanel.getScoreLabel();
            
            if (restartBtn != null) {
                restartBtn.getTransforms().removeIf(transform -> transform instanceof Rotate);
                Platform.runLater(() -> {
                    double width = restartBtn.getBoundsInLocal().getWidth();
                    double height = restartBtn.getBoundsInLocal().getHeight();
                    if (width > 0 && height > 0) {
                        Rotate rotate = new Rotate(180, width / 2, height / 2);
                        restartBtn.getTransforms().add(rotate);
                    } else {
                        Rotate rotate = new Rotate(180, 120, 30);
                        restartBtn.getTransforms().add(rotate);
                    }
                });
            }
            
            if (mainMenuBtn != null) {
                mainMenuBtn.getTransforms().removeIf(transform -> transform instanceof Rotate);
                Platform.runLater(() -> {
                    double width = mainMenuBtn.getBoundsInLocal().getWidth();
                    double height = mainMenuBtn.getBoundsInLocal().getHeight();
                    if (width > 0 && height > 0) {
                        Rotate rotate = new Rotate(180, width / 2, height / 2);
                        mainMenuBtn.getTransforms().add(rotate);
                    } else {
                        Rotate rotate = new Rotate(180, 120, 30);
                        mainMenuBtn.getTransforms().add(rotate);
                    }
                });
            }
            
            if (gameOverLbl != null) {
                gameOverLbl.getTransforms().removeIf(transform -> transform instanceof Rotate);
                Platform.runLater(() -> {
                    double width = gameOverLbl.getBoundsInLocal().getWidth();
                    double height = gameOverLbl.getBoundsInLocal().getHeight();
                    if (width > 0 && height > 0) {
                        Rotate rotate = new Rotate(180, width / 2, height / 2);
                        gameOverLbl.getTransforms().add(rotate);
                    } else {
                        Rotate rotate = new Rotate(180, 360, 55);
                        gameOverLbl.getTransforms().add(rotate);
                    }
                });
            }
            
            if (scoreLbl != null) {
                scoreLbl.getTransforms().removeIf(transform -> transform instanceof Rotate);
                Platform.runLater(() -> {
                    double width = scoreLbl.getBoundsInLocal().getWidth();
                    double height = scoreLbl.getBoundsInLocal().getHeight();
                    if (width > 0 && height > 0) {
                        Rotate rotate = new Rotate(180, width / 2, height / 2);
                        scoreLbl.getTransforms().add(rotate);
                    } else {
                        Rotate rotate = new Rotate(180, 360, 30);
                        scoreLbl.getTransforms().add(rotate);
                    }
                });
            }
        } else if (gameOverPanel != null) {
            Button restartBtn = gameOverPanel.getRestartButton();
            Button mainMenuBtn = gameOverPanel.getMainMenuButton();
            Label gameOverLbl = gameOverPanel.getGameOverLabel();
            Label scoreLbl = gameOverPanel.getScoreLabel();
            
            if (restartBtn != null) {
                restartBtn.getTransforms().removeIf(transform -> transform instanceof Rotate);
            }
            if (mainMenuBtn != null) {
                mainMenuBtn.getTransforms().removeIf(transform -> transform instanceof Rotate);
            }
            if (gameOverLbl != null) {
                gameOverLbl.getTransforms().removeIf(transform -> transform instanceof Rotate);
            }
            if (scoreLbl != null) {
                scoreLbl.getTransforms().removeIf(transform -> transform instanceof Rotate);
            }
            Button backBtnInverted = gameOverPanel.getBackButtonInverted();
            if (backBtnInverted != null) {
                backBtnInverted.getTransforms().removeIf(transform -> transform instanceof Rotate);
            }
            gameOverPanel.resetLayout();
        }
        
        isGameOver.set(true);
        
        gamePanel.setFocusTraversable(false);
        gamePanel.setDisable(true);
    }

    public void newGame(ActionEvent actionEvent) {
        if (actionEvent != null) {
            actionEvent.consume();
        }
        boolean is1984Mode = gameMode != null && gameMode.equals("1984");
        
        if (currentLevel != null) {
            tripleClearsCount = 0;
            levelStartTime = System.currentTimeMillis();
            totalPauseDuration = 0;
            pauseStartTime = 0;
            stopTimerUpdate();
            stopFreePlayTimer();
            updateLevelObjectiveLabel();
            if (is1984Mode) {
                setPlayerName("1984 Player", false);
                startGameInstantly();
            } else {
                startCountdown();
            }
        } else {
            stopFreePlayTimer();
            boolean isInvertedMode = gameMode != null && gameMode.equals("inverted");
            if (isInvertedMode || is1984Mode) {
                if (is1984Mode) {
                    setPlayerName("1984 Player", false);
                } else {
                    setPlayerName("Inverted Player", false);
                }
                updateLevelObjectiveLabel();
                if (is1984Mode) {
                    startGameInstantly();
                } else {
                    startCountdown();
                }
            } else {
                requestPlayerNameAndStart();
            }
        }
    }
    
    private void requestPlayerNameAndStart() {
        PlayerNameDialog nameDialog = new PlayerNameDialog(stage);
        String name = nameDialog.getPlayerName();
        boolean guest = nameDialog.isGuest();
        
        if (!guest && name == null) {
            return;
        }
        
        setPlayerName(name, guest);
        updateLevelObjectiveLabel();
        startCountdown();
    }
    
    public void startCountdown() {
        gamePane.setVisible(true);
        gameOverPanel.setVisible(false);
        gameOverContainer.setVisible(false);
        pausePanel.setVisible(false);
        pauseContainer.setVisible(false);
        if (levelCompleteContainer != null) {
            levelCompleteContainer.setVisible(false);
        }
        if (countdownContainer != null) {
            countdownContainer.setVisible(true);
            
            // Rotate countdown 180 degrees in inverted mode
            if (gameMode != null && gameMode.equals("inverted")) {
                countdownContainer.getTransforms().removeIf(transform -> transform instanceof Rotate);
                Platform.runLater(() -> {
                    double width = countdownContainer.getBoundsInLocal().getWidth();
                    double height = countdownContainer.getBoundsInLocal().getHeight();
                    if (width > 0 && height > 0) {
                        Rotate rotate = new Rotate(180, width / 2, height / 2);
                        countdownContainer.getTransforms().add(rotate);
                    } else {
                        Rotate rotate = new Rotate(180, 360, 340);
                        countdownContainer.getTransforms().add(rotate);
                    }
                });
            } else {
                // Remove rotation when not in inverted mode
                countdownContainer.getTransforms().removeIf(transform -> transform instanceof Rotate);
            }
        }
        isGameOver.set(false);
        setPaused(true);
        
        boolean is1984Mode = gameMode != null && gameMode.equals("1984");
        if (countdownPanel != null) {
            if (is1984Mode) {
                countdownPanel.set1984Mode(true);
            }
            countdownPanel.startCountdown(() -> {
                if (countdownContainer != null) {
                    countdownContainer.setVisible(false);
                }
                if (currentLevel != null && (currentLevel.getLevelNumber() == 2 || currentLevel.getLevelNumber() == 5)) {
                    if (pauseStartTime > 0) {
                        totalPauseDuration += (System.currentTimeMillis() - pauseStartTime);
                        pauseStartTime = 0;
                    }
                }
                startNewGame();
            });
        } else {
        startNewGame();
    }
    }
    
    public void startNewGame() {
        gamePane.setVisible(true);
        gameOverPanel.setVisible(false);
        gameOverContainer.setVisible(false);
        pausePanel.setVisible(false);
        pauseContainer.setVisible(false);
        if (levelCompleteContainer != null) {
            levelCompleteContainer.setVisible(false);
        }
        if (countdownContainer != null) {
            countdownContainer.setVisible(false);
        }
        isGameOver.set(false);
        setPaused(false);
        
        if (currentLevel != null && (currentLevel.getLevelNumber() == 2 || currentLevel.getLevelNumber() == 5)) {
            levelStartTime = System.currentTimeMillis();
            totalPauseDuration = 0;
            pauseStartTime = 0;
            stopTimerUpdate();
            startTimerUpdate();
        } else if (currentLevel == null && (gameMode == null || !gameMode.equals("inverted"))) {
            startFreePlayTimer();
        }
        
        gamePanel.setFocusTraversable(true);
        gamePanel.setDisable(false);

        if (eventListener != null) {
            ViewData newBrickData = eventListener.createNewGame();
            refreshBrick(newBrickData);
        }
        gamePanel.requestFocus();
    }
    
    private void startGameInstantly() {
        gamePane.setVisible(true);
        gameOverPanel.setVisible(false);
        gameOverContainer.setVisible(false);
        pausePanel.setVisible(false);
        pauseContainer.setVisible(false);
        if (levelCompleteContainer != null) {
            levelCompleteContainer.setVisible(false);
        }
        if (countdownContainer != null) {
            countdownContainer.setVisible(false);
        }
        
        isPause.set(false);
        isGameOver.set(false);
        gamePanel.setFocusTraversable(true);
        gamePanel.setDisable(false);
        
        if (eventListener != null) {
            ViewData newBrickData = eventListener.createNewGame();
            refreshBrick(newBrickData);
        }
        
        gamePanel.requestFocus();
        
        if (timeLine != null) {
            timeLine.stop();
        }
        
        if (currentLevel == null && (gameMode == null || (!gameMode.equals("inverted") && !gameMode.equals("1984")))) {
            startFreePlayTimer();
        }
        
        timeLine = new Timeline(new KeyFrame(
                Duration.millis(dropIntervalMs),
                _ -> moveDown(threadMove())
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }
    
    private void returnToGamemodesMenu() {
        try {
            if (stage == null) return;
            URL location = getClass().getClassLoader().getResource("gamemodes.fxml");
            if (location == null) {
                return;
            }
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();
            GamemodesController controller = loader.getController();
            if (controller != null) {
                controller.setStage(stage);
            }
            Scene scene = new Scene(root, 720, 680);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void startLevelGame() {
        if (currentLevel != null && eventListener == null) {
            eventListener = new com.comp2042.logic.GameController(this);
        }
        if (currentLevel != null) {
            startCountdown();
        }
    }
    private void setPaused(boolean paused) {
        isPause.set(paused);

        if (timeLine == null) {
            return;
        }

        if (paused) {
            timeLine.stop();
            stopTimerUpdate();
            if (currentLevel != null && (currentLevel.getLevelNumber() == 2 || currentLevel.getLevelNumber() == 5)) {
                pauseStartTime = System.currentTimeMillis();
            }
            if (currentLevel == null && (gameMode == null || !gameMode.equals("inverted"))) {
                if (freePlayTimerLine != null) {
                    freePlayTimerLine.pause();
                }
                freePlayPauseStartTime = System.currentTimeMillis();
            }
        } else {
            timeLine.play();
            if (currentLevel != null && (currentLevel.getLevelNumber() == 2 || currentLevel.getLevelNumber() == 5)) {
                if (pauseStartTime > 0) {
                    totalPauseDuration += System.currentTimeMillis() - pauseStartTime;
                    pauseStartTime = 0;
                }
                startTimerUpdate();
            }
            if (currentLevel == null && (gameMode == null || !gameMode.equals("inverted"))) {
                if (freePlayPauseStartTime > 0) {
                    freePlayTotalPauseDuration += (System.currentTimeMillis() - freePlayPauseStartTime);
                    freePlayPauseStartTime = 0;
                }
                if (freePlayTimerLine != null) {
                    freePlayTimerLine.play();
                }
            }
        }
    }
    public void pauseGame(ActionEvent actionEvent) {
        if (actionEvent != null) {
            actionEvent.consume();
        }

        if (isGameOver.get()) {
            return;
        }

        boolean newPauseState = !isPause.get();
        setPaused(newPauseState);
        pausePanel.setVisible(newPauseState);
        pauseContainer.setVisible(newPauseState);
        
        if (newPauseState) {
            positionPauseMenu();
        }
        
        gamePanel.requestFocus();
    }

    public boolean isPaused() {
        return isPause.get();
    }
    
    private void positionPauseMenu() {
        gameBoard.applyCss();
        gameBoard.layout();
        
        double gameBoardX = gameBoard.getLayoutX();
        double gameBoardY = gameBoard.getLayoutY();
        double gameBoardWidth = gameBoard.getWidth() > 0 ? gameBoard.getWidth() : 276;
        double gameBoardHeight = gameBoard.getHeight() > 0 ? gameBoard.getHeight() : 556;
        
        double gameBoardCenterX = gameBoardX + gameBoardWidth / 2;
        double gameBoardCenterY = gameBoardY + gameBoardHeight / 2;
        double rootCenterX = rootPane.getWidth() > 0 ? rootPane.getWidth() / 2 : 360;
        double rootCenterY = rootPane.getHeight() > 0 ? rootPane.getHeight() / 2 : 340;
        
        double translateX = gameBoardCenterX - rootCenterX + 5;
        double translateY = gameBoardCenterY - rootCenterY - 10;
        
        pauseContainer.setTranslateX(translateX);
        pauseContainer.setTranslateY(translateY);
        
        // Rotate pause menu 180 degrees in inverted mode
        if (gameMode != null && gameMode.equals("inverted") && pauseContainer != null) {
            pauseContainer.getTransforms().removeIf(transform -> transform instanceof Rotate);
            Platform.runLater(() -> {
                double width = pauseContainer.getBoundsInLocal().getWidth();
                double height = pauseContainer.getBoundsInLocal().getHeight();
                if (width > 0 && height > 0) {
                    Rotate rotate = new Rotate(180, width / 2, height / 2);
                    pauseContainer.getTransforms().add(rotate);
                } else {
                    Rotate rotate = new Rotate(180, 200, 300);
                    pauseContainer.getTransforms().add(rotate);
                }
            });
        } else if (pauseContainer != null) {
            // Remove rotation when not in inverted mode
            pauseContainer.getTransforms().removeIf(transform -> transform instanceof Rotate);
        }
    }

    public boolean isGameOver() {
        return isGameOver.get();
    }

    public void requestFocus() {
        gamePanel.requestFocus();
    }
    
    public void refreshHighScoreDisplay() {
        if (currentLevel != null) {
            return;
        }
        boolean isInvertedMode = gameMode != null && gameMode.equals("inverted");
        if (isInvertedMode) {
            highScoreLabel.setVisible(false);
            highScoreHolderLabel.setVisible(false);
            return;
        }
        int highScore = HighScoreManager.getHighScore();
        String highScoreHolder = HighScoreManager.getHighScoreHolder();
        highScoreLabel.setText("High Score: " + highScore);
        if (highScoreHolder != null && !highScoreHolder.isEmpty()) {
            String capitalizedName = highScoreHolder.substring(0, 1).toUpperCase() + 
                                    (highScoreHolder.length() > 1 ? highScoreHolder.substring(1).toLowerCase() : "");
            highScoreHolderLabel.setText("by " + capitalizedName);
            highScoreHolderLabel.setVisible(true);
            animateHighScoreHolder();
        } else {
            highScoreHolderLabel.setVisible(false);
        }
    }
    
    private void animateHighScoreHolder() {
        if (highScoreHolderLabel != null && highScoreHolderLabel.isVisible()) {
            ScaleTransition pulse = new ScaleTransition(Duration.seconds(1.2), highScoreHolderLabel);
            pulse.setFromX(1.0);
            pulse.setFromY(1.0);
            pulse.setToX(1.1);
            pulse.setToY(1.1);
            pulse.setCycleCount(Timeline.INDEFINITE);
            pulse.setAutoReverse(true);
            pulse.play();
            
            FadeTransition glow = new FadeTransition(Duration.seconds(1.2), highScoreHolderLabel);
            glow.setFromValue(0.8);
            glow.setToValue(1.0);
            glow.setCycleCount(Timeline.INDEFINITE);
            glow.setAutoReverse(true);
            glow.play();
        }
    }
}
