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
    private static final double INVERTED_ROTATION_ANGLE = 180.0;
    private static final double HOLD_SCALE_UP = 1.15;
    private static final int HOLD_SCALE_DURATION_UP_MS = 200;
    private static final int HOLD_SCALE_DURATION_DOWN_MS = 300;
    private static final int WINDOW_WIDTH = 720;
    private static final int WINDOW_HEIGHT = 680;
    private static final int BACK_BUTTON_OFFSET_X = 100;
    private static final int BACK_BUTTON_OFFSET_Y = 80;
    private static final double GHOST_COLOR_OPACITY = 0.25;
    private static final double GHOST_BORDER_OPACITY = 0.4;
    private static final int GHOST_FADE_IN_DURATION_MS = 100;
    private static final int GHOST_FADE_OUT_DURATION_MS = 80;
    private static final double GHOST_STROKE_WIDTH = 1.5;
    private static final double GHOST_VISIBILITY_THRESHOLD = 0.01;
    private static final double DEFAULT_GAME_BOARD_WIDTH = 276.0;
    private static final double DEFAULT_GAME_BOARD_HEIGHT = 556.0;
    private static final double DEFAULT_ROOT_CENTER_X = 360.0;
    private static final double DEFAULT_ROOT_CENTER_Y = 340.0;
    private static final double PAUSE_MENU_OFFSET_X = 5.0;
    private static final double PAUSE_MENU_OFFSET_Y = -10.0;
    private static final double PAUSE_MENU_FALLBACK_X = 200.0;
    private static final double PAUSE_MENU_FALLBACK_Y = 300.0;

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
    private StackPane nextBlockStackPane2;
    @FXML
    private GridPane nextBlockPanel2;
    @FXML
    private StackPane nextBlockStackPane3;
    @FXML
    private GridPane nextBlockPanel3;
    @FXML
    private GridPane holdBlockPanel;
    @FXML
    private VBox holdBlockContainer;

    @FXML
    private VBox leftPanel;

    @FXML
    private VBox creatorPanel;
    
    @FXML
    private StackPane dialogueContainerGame;
    
    @FXML
    private ImageView dialogueImageGame;
    
    @FXML
    private Label dialogueTextGame;

    @FXML
    private javafx.scene.control.Button backButton;

    @FXML
    private ImageView borderFrame;
    
    @FXML
    private ImageView background1984;
    
    @FXML
    private ImageView whiteBox1984;
    
    @FXML
    private StackPane whiteBoxContainer1984;
    
    @FXML
    private Label whiteBoxText1984;
    
    private Label scoreLabel1984;
    private Label linesLabel1984;
    private Timeline glitchTimeline1984;
    private Rectangle filmGrainOverlay;
    private Timeline filmGrainTimeline;
    private Timeline flickerTimeline;
    private Timeline backgroundFlickerTimeline;

    private Rectangle[][] displayMatrix;
    private VBox leaderboardContainer;
    private LeaderboardPanel leaderboardPanel;
    private Rectangle[][] nextBlockRectangles;
    private Rectangle[][] nextBlockRectangles2;
    private Rectangle[][] nextBlockRectangles3;
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
    private Timeline dialogueTextTimeline;

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
        boolean isInvertedMode = isInvertedMode();
        boolean is1984Mode = is1984Mode();
        
        setupRootPaneRotation(isInvertedMode);
        
        if (isInvertedMode) {
            setupInvertedModeUI();
        } else {
            setupNormalModeUI(is1984Mode);
        }
        
        if (is1984Mode) {
            setup1984ModeUI();
        } else {
            cleanup1984ModeUI();
        }
    }
    
    private void update1984Labels(double translateX, double opacity) {
        if (scoreLabel1984 != null) {
            scoreLabel1984.setTranslateX(translateX);
            scoreLabel1984.setOpacity(opacity);
        }
        if (linesLabel1984 != null) {
            linesLabel1984.setTranslateX(translateX);
            linesLabel1984.setOpacity(opacity);
        }
    }
    
    private void reset1984Labels() {
        update1984Labels(0, 1.0);
    }
    
    private void startGlitchAnimation1984() {
        stopTimelineSafely(glitchTimeline1984);
        
        glitchTimeline1984 = new Timeline(
            new KeyFrame(Duration.ZERO, e -> reset1984Labels()),
            new KeyFrame(Duration.millis(50), e -> {
                update1984Labels(Math.random() * 2 - 1, 0.9 + Math.random() * 0.1);
            }),
            new KeyFrame(Duration.millis(100), e -> reset1984Labels()),
            new KeyFrame(Duration.millis(150), e -> {
                update1984Labels(Math.random() * 3 - 1.5, 0.85 + Math.random() * 0.15);
            }),
            new KeyFrame(Duration.millis(200), e -> reset1984Labels())
        );
        glitchTimeline1984.setCycleCount(Timeline.INDEFINITE);
        glitchTimeline1984.play();
    }
    
    private void startFilmGrainAnimation() {
        stopTimelineSafely(filmGrainTimeline);
        
        filmGrainTimeline = createIndefiniteTimeline(
            new KeyFrame(Duration.millis(50), e -> {
                if (filmGrainOverlay != null) {
                    filmGrainOverlay.setOpacity(0.02 + Math.random() * 0.04);
                }
            })
        );
    }
    
    private void setRootPaneOpacity(double opacity) {
        if (rootPane != null) {
            rootPane.setOpacity(opacity);
        }
    }
    
    private void startFlickerAnimation() {
        stopTimelineSafely(flickerTimeline);
        
        flickerTimeline = new Timeline(
            new KeyFrame(Duration.ZERO, e -> setRootPaneOpacity(1.0)),
            new KeyFrame(Duration.millis(2000), e -> {
                if (rootPane != null && Math.random() < 0.1) {
                    setRootPaneOpacity(0.95 + Math.random() * 0.05);
                }
            }),
            new KeyFrame(Duration.millis(2100), e -> setRootPaneOpacity(1.0)),
            new KeyFrame(Duration.millis(3500), e -> {
                if (rootPane != null && Math.random() < 0.15) {
                    setRootPaneOpacity(0.92 + Math.random() * 0.08);
                }
            }),
            new KeyFrame(Duration.millis(3600), e -> setRootPaneOpacity(1.0))
        );
        flickerTimeline.setCycleCount(Timeline.INDEFINITE);
        flickerTimeline.play();
    }
    
    private void setBackground1984Opacity(double opacity) {
        if (background1984 != null) {
            background1984.setOpacity(opacity);
        }
    }
    
    private void startBackgroundFlickerAnimation() {
        stopTimelineSafely(backgroundFlickerTimeline);
        
        if (background1984 == null) {
            return;
        }
        
        backgroundFlickerTimeline = new Timeline(
            new KeyFrame(Duration.ZERO, e -> setBackground1984Opacity(1.0)),
            new KeyFrame(Duration.millis(50), e -> setBackground1984Opacity(0.0)),
            new KeyFrame(Duration.millis(80), e -> setBackground1984Opacity(1.0)),
            new KeyFrame(Duration.millis(120), e -> setBackground1984Opacity(0.0)),
            new KeyFrame(Duration.millis(150), e -> setBackground1984Opacity(0.9)),
            new KeyFrame(Duration.millis(180), e -> setBackground1984Opacity(0.0)),
            new KeyFrame(Duration.millis(220), e -> setBackground1984Opacity(1.0)),
            new KeyFrame(Duration.millis(280), e -> setBackground1984Opacity(0.05)),
            new KeyFrame(Duration.millis(320), e -> setBackground1984Opacity(0.8)),
            new KeyFrame(Duration.millis(380), e -> setBackground1984Opacity(0.1)),
            new KeyFrame(Duration.millis(450), e -> setBackground1984Opacity(0.75)),
            new KeyFrame(Duration.seconds(1.5), e -> {
                if (background1984 != null) {
                    startOngoingFlicker();
                }
            })
        );
        backgroundFlickerTimeline.setCycleCount(1);
        backgroundFlickerTimeline.play();
    }
    
    private void startOngoingFlicker() {
        stopTimelineSafely(backgroundFlickerTimeline);
        
        if (background1984 == null) {
            return;
        }
        
        backgroundFlickerTimeline = new Timeline(
            new KeyFrame(Duration.ZERO, e -> setBackground1984Opacity(0.75)),
            new KeyFrame(Duration.millis(2000), e -> {
                if (background1984 != null && Math.random() < 0.15) {
                    setBackground1984Opacity(0.5 + Math.random() * 0.3);
                }
            }),
            new KeyFrame(Duration.millis(2100), e -> setBackground1984Opacity(0.75)),
            new KeyFrame(Duration.millis(4500), e -> {
                if (background1984 != null && Math.random() < 0.2) {
                    setBackground1984Opacity(0.4 + Math.random() * 0.4);
                }
            }),
            new KeyFrame(Duration.millis(4600), e -> setBackground1984Opacity(0.75))
        );
        backgroundFlickerTimeline.setCycleCount(Timeline.INDEFINITE);
        backgroundFlickerTimeline.play();
    }
    
    public void setLevel(com.comp2042.logic.Level level) {
        this.currentLevel = level;
        if (level != null) {
            setupLevelUI(level);
        } else {
            setupFreePlayUI();
        }
    }
    
    private void startTimerUpdate() {
        stopTimerUpdate();
        if (isTimedLevel()) {
            updateTimerLabel();
            timerUpdateLine = createIndefiniteTimeline(
                new KeyFrame(Duration.seconds(1), _ -> updateTimerLabel())
            );
        }
    }
    
    private void updateTimerLabel() {
        if (currentLevel != null && timerLabel != null && timerLabel.isVisible()) {
            long timeElapsed = calculateElapsedTime(levelStartTime, totalPauseDuration, pauseStartTime);
            int remainingTime = (int)(currentLevel.getTimeLimit() - timeElapsed);
            
            if (remainingTime <= 0 && isTimedLevel()) {
                stopTimelineSafely(timeLine);
                stopTimerUpdate();
                gameOver();
                return;
            }
            
            timerLabel.setText(formatTimeWithPrefix(Math.max(0, remainingTime)));
        }
    }

    private void stopTimerUpdate() {
        stopTimelineSafely(timerUpdateLine);
        timerUpdateLine = null;
    }
    
    private void startFreePlayTimer() {
        stopFreePlayTimer();
        if (currentLevel == null && !isInvertedMode()) {
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
        if (currentLevel == null && !isInvertedMode() && timerLabel != null) {
            if (freePlayStartTime == 0) {
                timerLabel.setText("Time: 0:00");
                return;
            }
            long timeElapsed = Math.max(0, calculateElapsedTime(freePlayStartTime, freePlayTotalPauseDuration, freePlayPauseStartTime));
            timerLabel.setText(formatTimeWithPrefix(timeElapsed));
        }
    }
    
    private void stopFreePlayTimer() {
        if (freePlayTimerLine != null) {
            freePlayTimerLine.stop();
            freePlayTimerLine = null;
        }
    }
    
    private long getFreePlayElapsedTime() {
        if (currentLevel != null) {
            return 0;
        }
        if (freePlayStartTime == 0) {
            return 0;
        }
        return Math.max(0, calculateElapsedTime(freePlayStartTime, freePlayTotalPauseDuration, freePlayPauseStartTime));
    }
    
    private void updateLevelObjectiveLabel() {
        if (objectiveLabel != null) {
            if (currentLevel == null) {
                if (isInvertedMode()) {
                    objectiveLabel.setVisible(false);
                } else {
                    String playerDisplayName = (playerName != null && !playerName.isEmpty()) ? capitalizeName(playerName) : "Player";
                    
                    String highScoreHolder = HighScoreManager.getHighScoreHolder();
                    String message = "Keep Going " + playerDisplayName + "!";
                    if (highScoreHolder != null && !highScoreHolder.isEmpty()) {
                        message += " You can beat " + capitalizeName(highScoreHolder) + "'s Highscore!!!";
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

    private void setupInitialResources() {
        URL digitalFontUrl = getClass().getClassLoader().getResource("digital.ttf");
        if (digitalFontUrl != null) {
            Font.loadFont(digitalFontUrl.toExternalForm(), 38);
        }
        HighScoreManager.ensureDirectoryExists();
        LeaderboardManager.ensureDirectoryExists();
        LevelProgressManager.ensureDirectoryExists();
        LevelProgressManager.initialize();
        SettingsManager.ensureDirectoryExists();
    }
    
    private void setupUIVisibility() {
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
    }
    
    private void setupGamePanel() {
        gamePanel.setFocusTraversable(true);
        resetGamePanelFocus();
        gamePanel.setOnKeyPressed(this::handleKeyPressed);
    }
    
    private void setupGameOverButtonHandlers() {
        gameOverPanel.getRestartButton().setOnAction(_ -> newGame(null));
        gameOverPanel.getMainMenuButton().setOnAction(_ -> returnToMainMenu());
        gameOverPanel.getLeaderboardButton().setOnAction(_ -> showLeaderboard());
        
        if (is1984Mode() && gameOverPanel.getBackButton1984() != null) {
            gameOverPanel.getBackButton1984().setOnAction(_ -> returnToGamemodesMenu());
        }
    }
    
    private void setupPauseButtonHandlers() {
        pausePanel.getResumeButton().setOnAction(_ -> resumeGame());
        pausePanel.getRestartButton().setOnAction(_ -> newGame(null));
        pausePanel.getSettingsButton().setOnAction(_ -> openSettingsFromPause());
        pausePanel.getLeaderboardButton().setOnAction(_ -> showLeaderboard());
        pausePanel.getMainMenuButton().setOnAction(_ -> returnToMainMenu());
        
        if (is1984Mode() && pausePanel != null) {
            pausePanel.set1984Mode(true);
        }
    }
    
    private void setupBackground() {
        if (!is1984Mode()) {
            AnimatedBackground animatedBackground = new AnimatedBackground(720, 680);
            rootPane.getChildren().addFirst(animatedBackground);
        }
    }
    
    private void setupBackButton() {
        if (backButton != null) {
            if (is1984Mode()) {
                backButton.setVisible(true);
                backButton.setManaged(true);
                backButton.toFront();
                backButton.setOnAction(_ -> returnToGamemodesMenu());
            } else {
                backButton.setOnAction(_ -> returnToPlayMenu());
            }
        }
    }
    
    public void initialize(URL location, ResourceBundle resources) {
        setupInitialResources();
        refreshHighScoreDisplay();
        setupGamePanel();
        setupUIVisibility();
        updateLevelObjectiveLabel();
        setupGameOverButtonHandlers();
        setupPauseButtonHandlers();
        setupBackground();
        setupLeaderboardPanel();
        setupHoldBlockAnimation();
        setupBackButton();
    }
    
    private void setupHoldBlockAnimation() {
        if (holdBlockContainer != null) {
            ScaleTransition pulse = createScaleTransition(holdBlockContainer, Duration.seconds(3.0), 1.0, 1.0, 1.02, 1.02);
            pulse.setCycleCount(Timeline.INDEFINITE);
            pulse.setAutoReverse(true);
            pulse.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);
            pulse.play();
            
            FadeTransition glow = createFadeTransition(holdBlockContainer, Duration.seconds(3.0), 0.95, 1.0);
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
            resetGamePanelFocus();
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
        navigateToScene("settings.fxml", loader -> {
            SettingsController settingsController = loader.getController();
            if (settingsController != null) {
                settingsController.setStage(stage);
                settingsController.setReturnToGame(stage.getScene(), this);
            }
        });
    }

    private void returnToMainMenu() {
        if (timeLine != null) {
            timeLine.stop();
        }
        stopTimerUpdate();
        MainMenuController.clearActiveGame();
        navigateToScene("mainMenu.fxml", loader -> {
            MainMenuController menuController = loader.getController();
            if (menuController != null) {
                menuController.setStage(stage);
            }
        });
    }
    
    private void returnToPlayMenu() {
        if (timeLine != null) {
            timeLine.stop();
        }
        stopTimerUpdate();
        stopFreePlayTimer();
        MainMenuController.clearActiveGame();
        navigateToScene("mainMenu.fxml", loader -> {
            MainMenuController menuController = loader.getController();
            if (menuController != null) {
                menuController.setStage(stage);
                menuController.showPlayMenu();
            }
        });
    }
    private void handleKeyPressed(KeyEvent keyEvent) {
        if (isGameOver.get()) {
                keyEvent.consume();
            return;
        }
        
        if (!isPause.get() && eventListener != null) {
            if (isInvertedMode()) {
                handleInvertedKeyInput(keyEvent);
            } else {
                handleNormalKeyInput(keyEvent);
            }
        }

        if (!isGameOver.get()) {
            handleGameControlKeys(keyEvent);
        }
    }


    public void initGameView(int[][] boardMatrix, ViewData brick) {
        initializeGameBoard(boardMatrix);
        initializeBrickPanels(brick);
        updateBrickPanelPosition(brick);
        updateGhostPosition(brick);
        initNextBlockPanel(brick);
        initHoldBlockPanel(brick);
        setupDialogueForGame();

        timeLine = createGameTimeline(dropIntervalMs);
        timeLine.play();
    }
    private void updateBrickPanelPosition(ViewData brick) {
        int brickSize = getBrickSize();
        double cellSize = brickPanel.getVgap() + brickSize;
        double padding = is1984Mode() ? 0 : BOARD_PADDING;
        double x = gameBoard.getLayoutX() + padding + brick.getxPosition() * cellSize;
        double y = BRICK_PANEL_Y_OFFSET + gameBoard.getLayoutY() + padding
                + (brick.getyPosition() - HIDDEN_ROWS) * cellSize;

        brickPanel.setLayoutX(x);
        brickPanel.setLayoutY(y);
    }

    private void clearAllGhostRectangles() {
        if (ghostRectangles != null) {
            for (int i = 0; i < ghostRectangles.length; i++) {
                for (int j = 0; j < ghostRectangles[i].length; j++) {
                    clearGhostRectangle(ghostRectangles[i][j]);
                }
            }
        }
    }
    
    private void updateGhostPanelPosition(ViewData brick) {
        int brickSize = getBrickSize();
        double cellSize = ghostPanel.getVgap() + brickSize;
        double x = gameBoard.getLayoutX() + BOARD_PADDING + brick.getxPosition() * cellSize;
        double ghostY = gameBoard.getLayoutY() + BOARD_PADDING
                + (brick.getGhostY() - HIDDEN_ROWS) * cellSize;
        ghostPanel.setLayoutX(x);
        ghostPanel.setLayoutY(ghostY);
    }
    
    private void updateGhostPosition(ViewData brick) {
        if (is1984Mode() || !SettingsManager.isGhostBlockEnabled()) {
            clearAllGhostRectangles();
            return;
        }
        
        updateGhostPanelPosition(brick);

        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle ghostRect = ghostRectangles[i][j];
                int colorIndex = brick.getBrickData()[i][j];
                boolean shouldShow = colorIndex != 0;
                boolean currentlyVisible = !ghostRect.getFill().equals(Color.TRANSPARENT) && ghostRect.getOpacity() > GHOST_VISIBILITY_THRESHOLD;
                
                if (shouldShow) {
                    Paint blockColor = getFillColor(colorIndex);
                    setupGhostRectangle(ghostRect, colorIndex, blockColor);
                }
                
                handleGhostRectangleVisibility(ghostRect, shouldShow, currentlyVisible);
            }
        }
    }
    
    public void updateGhostBlockVisibility() {
        if (lastViewData != null) {
            updateGhostPosition(lastViewData);
        } else {
            clearAllGhostRectangles();
        }
    }

    private void initNextBlockPanel(ViewData brick) {
        int brickSize = getBrickSize();
        
        int[][] nextData = brick.getNextBrickData();
        nextBlockRectangles = initializeBlockPanel(nextData, nextBlockPanel, brickSize);
        
        int[][] nextData2 = brick.getNextBrickData2();
        if (nextData2 != null && nextBlockPanel2 != null) {
            nextBlockRectangles2 = initializeBlockPanel(nextData2, nextBlockPanel2, brickSize);
        }
        
        int[][] nextData3 = brick.getNextBrickData3();
        if (nextData3 != null && nextBlockPanel3 != null) {
            nextBlockRectangles3 = initializeBlockPanel(nextData3, nextBlockPanel3, brickSize);
        }
        
        updateNextBlock(brick);
    }
    
    private Rectangle[][] initializeBlockPanel(int[][] data, GridPane panel, int brickSize) {
        if (data == null || panel == null) {
            return null;
        }
        Rectangle[][] rectangles = new Rectangle[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                Rectangle rect = createBrickRectangle(brickSize);
                rectangles[i][j] = rect;
                panel.add(rect, j, i);
            }
        }
        return rectangles;
    }

    private void updateNextBlock(ViewData brick) {
        int[][] nextData = brick.getNextBrickData();
        if (nextData != null && nextBlockRectangles != null) {
            updateRectangleGrid(nextData, nextBlockRectangles);
        }
        
        int[][] nextData2 = brick.getNextBrickData2();
        if (nextData2 != null && nextBlockRectangles2 != null) {
            updateRectangleGrid(nextData2, nextBlockRectangles2);
        }
        
        int[][] nextData3 = brick.getNextBrickData3();
        if (nextData3 != null && nextBlockRectangles3 != null) {
            updateRectangleGrid(nextData3, nextBlockRectangles3);
        }
    }
    
    private void initHoldBlockPanel(ViewData brick) {
        int brickSize = getBrickSize();
        int maxSize = 4;
        holdBlockRectangles = new Rectangle[maxSize][maxSize];
        previousHoldData = null;
        for (int i = 0; i < maxSize; i++) {
            for (int j = 0; j < maxSize; j++) {
                Rectangle rect = createBrickRectangle(brickSize);
                rect.setFill(Color.TRANSPARENT);
                holdBlockRectangles[i][j] = rect;
                holdBlockPanel.add(rect, j, i);
            }
        }
        updateHoldBlock(brick);
    }
    
    private boolean hasHoldDataChanged(int[][] previousHoldData, int[][] holdData) {
        if (previousHoldData == null && holdData != null) {
            return true;
        }
        if (previousHoldData != null && holdData == null) {
            return true;
        }
        if (previousHoldData != null && holdData != null) {
            if (previousHoldData.length != holdData.length || 
                (holdData.length > 0 && previousHoldData.length > 0 && 
                 (previousHoldData[0].length != holdData[0].length))) {
                return true;
            }
            for (int i = 0; i < holdData.length; i++) {
                for (int j = 0; j < holdData[i].length; j++) {
                    int prevVal = (i < previousHoldData.length && j < previousHoldData[i].length) ? previousHoldData[i][j] : 0;
                    int currVal = holdData[i][j];
                    if (prevVal != currVal) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private void cloneHoldData(int[][] holdData) {
        if (holdData != null) {
            previousHoldData = new int[holdData.length][];
            for (int i = 0; i < holdData.length; i++) {
                previousHoldData[i] = holdData[i].clone();
            }
        } else {
            previousHoldData = null;
        }
    }
    
    private void updateHoldBlock(ViewData brick) {
        int[][] holdData = brick.getHoldBrickData();
        boolean holdChanged = hasHoldDataChanged(previousHoldData, holdData);
        
        updateRectangleGrid(holdData, holdBlockRectangles);
        
        if (holdChanged && holdData != null) {
            cloneHoldData(holdData);
            spinHoldBlock();
        } else if (previousHoldData == null) {
            cloneHoldData(holdData);
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
            
            ScaleTransition scaleUp = createScaleTransition(holdBlockContainer, Duration.millis(HOLD_SCALE_DURATION_UP_MS), 1.0, 1.0, HOLD_SCALE_UP, HOLD_SCALE_UP);
            scaleUp.setCycleCount(1);
            scaleUp.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
            
            ScaleTransition scaleDown = createScaleTransition(holdBlockContainer, Duration.millis(HOLD_SCALE_DURATION_DOWN_MS), HOLD_SCALE_UP, HOLD_SCALE_UP, 1.0, 1.0);
            scaleDown.setCycleCount(1);
            scaleDown.setInterpolator(javafx.animation.Interpolator.EASE_IN);
            
            SequentialTransition sequence = createSequentialTransition(scaleUp, scaleDown);
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
        if (is1984Mode() && i != 0) {
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
        return is1984Mode() ? BRICK_SIZE_1984 : BRICK_SIZE;
    }


    public void refreshBrick(ViewData brick) {
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
        if (is1984Mode()) {
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
        resetGamePanelFocus();
    }

    private void hardDrop() {
        if (!isPause.get()) {
            DownData downData = eventListener.onHardDropEvent();
            if (shouldShowScoreNotification(downData)) {
                showScoreNotification(downData.getClearRow().getScoreBonus());
            }
            refreshBrick(downData.getViewData());
        }
        resetGamePanelFocus();
    }

    private boolean shouldShowScoreNotification(DownData downData) {
        return downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0;
    }

    private void showScoreNotification(int scoreBonus) {
        NotificationPanel notificationPanel = new NotificationPanel("+" + scoreBonus);
        groupNotification.getChildren().add(notificationPanel);
        
        boolean isInvertedMode = gameMode != null && gameMode.equals("inverted");
        if (isInvertedMode) {
            applyInvertedRotation(notificationPanel, 110.0, 100.0);
        }
        
        notificationPanel.showScore(groupNotification.getChildren());
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty scoreProperty) {
        this.currentScoreProperty = scoreProperty;
        scoreLabel.textProperty().bind(scoreProperty.asString("Score: %d"));
        if (is1984Mode() && gamePane != null) {
            if (scoreLabel1984 == null) {
                scoreLabel1984 = create1984Label("Score: 0", "score-label-1984", 50, 150);
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
        if (is1984Mode() && gamePane != null) {
            if (linesLabel1984 == null) {
                linesLabel1984 = create1984Label("Lines: 0", "lines-label-1984", 50, 200);
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
        long timeElapsed = calculateElapsedTime(levelStartTime, totalPauseDuration, pauseStartTime);
        
        int currentScore = currentScoreProperty != null ? currentScoreProperty.get() : 0;
        boolean objectiveMet = checkLevelObjectiveMet(
            currentLevel.getLevelNumber(), 
            totalLines, 
            timeElapsed, 
            tripleClearsCount, 
            currentScore, 
            currentLevel
        );
        
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
        
        int currentScore = currentScoreProperty != null ? currentScoreProperty.get() : 0;
        String progressText = generateLevelProgressText(
            currentLevel.getLevelNumber(),
            totalLines,
            timeElapsedParam,
            tripleClearsCount,
            currentScore,
            currentLevel
        );
        
        if (currentLevel.getLevelNumber() == 5) {
            if (timerLabel != null) {
                timerLabel.setVisible(true);
            }
        } else if (currentLevel.getLevelNumber() < 1 || currentLevel.getLevelNumber() > 5) {
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
                hideLevelCompleteAndShowGamePane();
                returnToLevelsMenu();
            });
            
            levelCompletePanel.getMainMenuButton().setOnAction(_ -> {
                hideLevelCompleteAndShowGamePane();
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
        ScaleTransition pulse = createScaleTransition(label, Duration.millis(150), 1.0, 1.0, 1.3, 1.3);
        pulse.setCycleCount(2);
        pulse.setAutoReverse(true);
        pulse.play();
    }

    public void gameOver() {
        stopDialogueTextTimeline();
        setPaused(true);
        gamePane.setVisible(false);
        
        int finalScore = currentScoreProperty != null ? currentScoreProperty.get() : 0;
        boolean isNewHighScore = setupGameOverHighScore(finalScore, playerName);
        
        long timePlayed = 0;
        if (currentLevel == null && !isInvertedMode()) {
            stopFreePlayTimer();
            timePlayed = getFreePlayElapsedTime();
        }
        
        int highScore = 0;
        String highScoreHolder = null;
        if (!isInvertedMode() && !is1984Mode() && currentLevel == null) {
            HighScoreInfo highScoreInfo = getHighScoreInfo();
            highScore = highScoreInfo.highScore;
            highScoreHolder = highScoreInfo.highScoreHolder;
        }
        
        setupHighScoreDisplay(highScore, highScoreHolder);
        setupGameOverPanel(finalScore, highScore, isNewHighScore, timePlayed);
        
        gameOverPanel.setVisible(true);
        gameOverContainer.setVisible(true);
        
        if (isInvertedMode() && gameOverPanel != null) {
            setupInvertedGameOverUI();
        } else if (gameOverPanel != null) {
            resetNormalGameOverUI();
        }
        
        isGameOver.set(true);
        
        disableGamePanel();
    }

    public void newGame(ActionEvent actionEvent) {
        if (actionEvent != null) {
            actionEvent.consume();
        }
        if (currentLevel != null) {
            startLevelGameMode();
        } else {
            if (isInvertedMode() || is1984Mode()) {
                startSpecialModeGame();
            } else {
                startFreePlayGame();
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
    
    private void setupCountdownUI() {
        gamePane.setVisible(true);
        gameOverPanel.setVisible(false);
        gameOverContainer.setVisible(false);
        pausePanel.setVisible(false);
        pauseContainer.setVisible(false);
        if (levelCompleteContainer != null) {
            levelCompleteContainer.setVisible(false);
        }
    }
    
    private void setupCountdownRotation() {
        if (countdownContainer != null) {
            countdownContainer.setVisible(true);
            if (isInvertedMode()) {
                applyInvertedRotation(countdownContainer, 360.0, 340.0);
            } else {
                removeRotateTransforms(countdownContainer);
            }
        }
    }
    
    private void handleCountdownFinished() {
        if (countdownContainer != null) {
            countdownContainer.setVisible(false);
        }
        if (isTimedLevel()) {
            if (pauseStartTime > 0) {
                totalPauseDuration += (System.currentTimeMillis() - pauseStartTime);
                pauseStartTime = 0;
            }
        }
        startNewGame();
    }
    
    public void startCountdown() {
        setupCountdownUI();
        setupCountdownRotation();
        isGameOver.set(false);
        setPaused(true);
        
        if (countdownPanel != null) {
            if (is1984Mode()) {
                countdownPanel.set1984Mode(true);
            }
            countdownPanel.startCountdown(this::handleCountdownFinished);
        } else {
            startNewGame();
        }
    }
    
    public void startNewGame() {
        resetGameState();
        setPaused(false);
        
        initializeGameTimersAndDialogue();

        if (eventListener != null) {
            ViewData newBrickData = eventListener.createNewGame();
            refreshBrick(newBrickData);
        }
    }
    
    private void startGameInstantly() {
        resetGameState();
        
        if (eventListener != null) {
            ViewData newBrickData = eventListener.createNewGame();
            refreshBrick(newBrickData);
        }
        
        if (timeLine != null) {
            timeLine.stop();
        }
        
        if (currentLevel == null && !isInvertedMode() && !is1984Mode()) {
            startFreePlayTimer();
            updateFreePlayDialogueText();
        } else if (currentLevel != null) {
            updateLevelsDialogueText();
        }
        
        timeLine = createGameTimeline(dropIntervalMs);
    }
    
    private void returnToGamemodesMenu() {
        navigateToScene("gamemodes.fxml", loader -> {
            GamemodesController controller = loader.getController();
            if (controller != null) {
                controller.setStage(stage);
            }
        });
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
            if (currentLevel == null) {
                stopDialogueTextTimeline();
                pauseFreePlayTimer();
            } else {
                pauseLevelTimer();
            }
        } else {
            timeLine.play();
            if (currentLevel != null) {
                resumeLevelTimer();
            } else {
                resumeFreePlayTimer();
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
        
        resetGamePanelFocus();
    }

    public boolean isPaused() {
        return isPause.get();
    }
    
    private void positionPauseMenu() {
        gameBoard.applyCss();
        gameBoard.layout();
        
        double gameBoardX = gameBoard.getLayoutX();
        double gameBoardY = gameBoard.getLayoutY();
        double gameBoardWidth = gameBoard.getWidth() > 0 ? gameBoard.getWidth() : DEFAULT_GAME_BOARD_WIDTH;
        double gameBoardHeight = gameBoard.getHeight() > 0 ? gameBoard.getHeight() : DEFAULT_GAME_BOARD_HEIGHT;
        
        double gameBoardCenterX = gameBoardX + gameBoardWidth / 2;
        double gameBoardCenterY = gameBoardY + gameBoardHeight / 2;
        double rootCenterX = rootPane.getWidth() > 0 ? rootPane.getWidth() / 2 : DEFAULT_ROOT_CENTER_X;
        double rootCenterY = rootPane.getHeight() > 0 ? rootPane.getHeight() / 2 : DEFAULT_ROOT_CENTER_Y;
        
        double translateX = gameBoardCenterX - rootCenterX + PAUSE_MENU_OFFSET_X;
        double translateY = gameBoardCenterY - rootCenterY + PAUSE_MENU_OFFSET_Y;
        
        pauseContainer.setTranslateX(translateX);
        pauseContainer.setTranslateY(translateY);
        
        if (isInvertedMode() && pauseContainer != null) {
            pauseContainer.getTransforms().removeIf(transform -> transform instanceof Rotate);
            Platform.runLater(() -> {
                double width = pauseContainer.getBoundsInLocal().getWidth();
                double height = pauseContainer.getBoundsInLocal().getHeight();
                if (width > 0 && height > 0) {
                    Rotate rotate = new Rotate(INVERTED_ROTATION_ANGLE, width / 2, height / 2);
                    pauseContainer.getTransforms().add(rotate);
                } else {
                    Rotate rotate = new Rotate(INVERTED_ROTATION_ANGLE, PAUSE_MENU_FALLBACK_X, PAUSE_MENU_FALLBACK_Y);
                    pauseContainer.getTransforms().add(rotate);
                }
            });
        } else if (pauseContainer != null) {
            pauseContainer.getTransforms().removeIf(transform -> transform instanceof Rotate);
        }
    }

    public boolean isGameOver() {
        return isGameOver.get();
    }

    public void requestFocus() {
        resetGamePanelFocus();
    }
    
    public void refreshHighScoreDisplay() {
        if (currentLevel != null) {
            return;
        }
        if (isInvertedMode()) {
            highScoreLabel.setVisible(false);
            highScoreHolderLabel.setVisible(false);
            return;
        }
        HighScoreInfo highScoreInfo = getHighScoreInfo();
        int highScore = highScoreInfo.highScore;
        String highScoreHolder = highScoreInfo.highScoreHolder;
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
            ScaleTransition pulse = createScaleTransition(highScoreHolderLabel, Duration.seconds(1.2), 1.0, 1.0, 1.1, 1.1);
            pulse.setCycleCount(Timeline.INDEFINITE);
            pulse.setAutoReverse(true);
            pulse.play();
            
            FadeTransition glow = createFadeTransition(highScoreHolderLabel, Duration.seconds(1.2), 0.8, 1.0);
            glow.setCycleCount(Timeline.INDEFINITE);
            glow.setAutoReverse(true);
            glow.play();
        }
    }
    
    private void updateFreePlayDialogueText() {
        if (dialogueTextGame == null || currentLevel != null) {
            return;
        }
        
        stopDialogueTextTimeline();
        
        String currentPlayer = playerName != null && !playerName.isEmpty() ? playerName : "Player";
        String capitalizedPlayer = capitalizeName(currentPlayer);
        HighScoreInfo highScoreInfo = getHighScoreInfo();
        
        String message1;
        if (highScoreInfo.highScoreHolder != null && !highScoreInfo.highScoreHolder.isEmpty()) {
            message1 = "Come on " + capitalizedPlayer + "! You can beat " + capitalizeName(highScoreInfo.highScoreHolder) + "'s highscore";
        } else {
            message1 = "Come on " + capitalizedPlayer + "! Set a new highscore!";
        }
        
        String message2 = "Keep going!! I Believe in you " + capitalizedPlayer;
        String message3 = "Prove them wrong " + capitalizedPlayer;
        String message4 = "I created the game.. YOU MASTER IT";
        
        java.util.List<String> messages = new java.util.ArrayList<>();
        messages.add(message1);
        messages.add(message2);
        messages.add(message3);
        messages.add(message4);
        
        createDialogueTextTimeline(messages, () -> currentLevel == null);
    }
    
    private void stopDialogueTextTimeline() {
        if (dialogueTextTimeline != null) {
            dialogueTextTimeline.stop();
            dialogueTextTimeline = null;
        }
    }
    
    private void updateLevelsDialogueText() {
        if (dialogueTextGame == null || currentLevel == null) {
            return;
        }
        
        stopDialogueTextTimeline();
        
        String message1 = "You can do this";
        String message2 = "cant wait to travel to the next level with you!!";
        String message3 = "I know it may be tough but i believe in you!!";
        String message4 = "Im so proud of you!!";
        
        java.util.List<String> messages = new java.util.ArrayList<>();
        messages.add(message1);
        messages.add(message2);
        messages.add(message3);
        messages.add(message4);
        
        createDialogueTextTimeline(messages, () -> currentLevel != null);
    }
    
    private boolean is1984Mode() {
        return gameMode != null && gameMode.equals("1984");
    }
    
    private boolean isInvertedMode() {
        return gameMode != null && gameMode.equals("inverted");
    }
    
    private Rectangle createBrickRectangle(double size) {
        Rectangle rect = new Rectangle(size, size);
        if (is1984Mode()) {
            rect.setArcHeight(2);
            rect.setArcWidth(2);
        } else {
            rect.setArcHeight(RECTANGLE_ARC_SIZE);
            rect.setArcWidth(RECTANGLE_ARC_SIZE);
        }
        return rect;
    }
    
    private void applyInvertedRotation(javafx.scene.Node node, double fallbackX, double fallbackY) {
        if (node == null) {
            return;
        }
        node.getTransforms().removeIf(transform -> transform instanceof Rotate);
        Platform.runLater(() -> {
            double width = node.getBoundsInLocal().getWidth();
            double height = node.getBoundsInLocal().getHeight();
            if (width > 0 && height > 0) {
                Rotate rotate = new Rotate(INVERTED_ROTATION_ANGLE, width / 2, height / 2);
                node.getTransforms().add(rotate);
            } else {
                Rotate rotate = new Rotate(INVERTED_ROTATION_ANGLE, fallbackX, fallbackY);
                node.getTransforms().add(rotate);
            }
        });
    }
    
    private void updateRectangleGrid(int[][] data, Rectangle[][] rectangles) {
        if (data == null || rectangles == null) {
            return;
        }
        for (int i = 0; i < rectangles.length && i < data.length; i++) {
            for (int j = 0; j < rectangles[i].length && j < data[i].length; j++) {
                if (data[i][j] != 0) {
                    rectangles[i][j].setFill(getFillColor(data[i][j]));
                } else {
                    rectangles[i][j].setFill(Color.TRANSPARENT);
                }
            }
        }
    }
    
    private void setNodeVisibility(javafx.scene.Node node, boolean visible) {
        if (node != null) {
            node.setVisible(visible);
            node.setManaged(visible);
        }
    }
    
    private void stopTimelineSafely(Timeline timeline) {
        if (timeline != null) {
            timeline.stop();
        }
    }
    
    private boolean isTimedLevel() {
        return currentLevel != null && (currentLevel.getLevelNumber() == 2 || currentLevel.getLevelNumber() == 5);
    }
    
    private void initializeGameTimersAndDialogue() {
        if (isTimedLevel()) {
            levelStartTime = System.currentTimeMillis();
            totalPauseDuration = 0;
            pauseStartTime = 0;
            stopTimerUpdate();
            startTimerUpdate();
        } else if (currentLevel == null && !isInvertedMode()) {
            startFreePlayTimer();
            updateFreePlayDialogueText();
        } else if (currentLevel != null) {
            updateLevelsDialogueText();
        }
    }
    
    private void navigateToScene(String fxmlFile, java.util.function.Consumer<FXMLLoader> controllerSetup) {
        try {
            if (stage == null) {
                return;
            }
            URL location = getClass().getClassLoader().getResource(fxmlFile);
            if (location == null) {
                return;
            }
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();
            if (controllerSetup != null) {
                controllerSetup.accept(loader);
            }
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private Color createGhostColor(Color originalColor, double opacity) {
        return Color.color(originalColor.getRed(), originalColor.getGreen(), originalColor.getBlue(), opacity);
    }
    
    private void clearGhostRectangle(Rectangle rect) {
        if (rect != null) {
            rect.setFill(Color.TRANSPARENT);
            rect.setStroke(Color.TRANSPARENT);
            rect.setOpacity(0.0);
        }
    }
    
    private void resetGamePanelFocus() {
        if (gamePanel != null) {
            gamePanel.setFocusTraversable(true);
            gamePanel.setDisable(false);
        gamePanel.requestFocus();
        }
    }
    
    private void disableGamePanel() {
        if (gamePanel != null) {
            gamePanel.setFocusTraversable(false);
            gamePanel.setDisable(true);
        }
    }
    
    private void resetGameState() {
        if (gamePane != null) {
            gamePane.setVisible(true);
        }
        if (gameOverPanel != null) {
            gameOverPanel.setVisible(false);
        }
        if (gameOverContainer != null) {
            gameOverContainer.setVisible(false);
        }
        if (pausePanel != null) {
            pausePanel.setVisible(false);
        }
        if (pauseContainer != null) {
            pauseContainer.setVisible(false);
        }
        if (levelCompleteContainer != null) {
            levelCompleteContainer.setVisible(false);
        }
        if (countdownContainer != null) {
            countdownContainer.setVisible(false);
        }
        isPause.set(false);
        isGameOver.set(false);
        resetGamePanelFocus();
    }
    
    private Label create1984Label(String text, String styleClass, double layoutX, double layoutY) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        label.setLayoutX(layoutX);
        label.setLayoutY(layoutY);
        if (gamePane != null) {
            gamePane.getChildren().add(label);
        }
        return label;
    }
    
    private long calculateElapsedTime(long startTime, long totalPauseDuration, long pauseStartTime) {
        long currentPauseTime = (pauseStartTime > 0) ? (System.currentTimeMillis() - pauseStartTime) : 0;
        return (System.currentTimeMillis() - startTime - totalPauseDuration - currentPauseTime) / 1000;
    }
    
    private String capitalizeName(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        return name.substring(0, 1).toUpperCase() + 
               (name.length() > 1 ? name.substring(1).toLowerCase() : "");
    }
    
    private void createDialogueTextTimeline(java.util.List<String> messages, java.util.function.BooleanSupplier shouldContinue) {
        if (dialogueTextGame == null) {
            return;
        }
        
        dialogueTextGame.setOpacity(1.0);
        int randomIndex = (int)(Math.random() * messages.size());
        dialogueTextGame.setText(messages.get(randomIndex));
        
        dialogueTextTimeline = new Timeline(
            new KeyFrame(Duration.seconds(6.5), e -> {
                if (dialogueTextGame != null && dialogueTextGame.getScene() != null && shouldContinue.getAsBoolean()) {
                    FadeTransition fadeOut = createFadeTransition(dialogueTextGame, Duration.millis(500), 1.0, 0.0);
                    fadeOut.setOnFinished(event -> {
                        int nextIndex = (int)(Math.random() * messages.size());
                        String nextMessage = messages.get(nextIndex);
                        dialogueTextGame.setText(nextMessage);
                        FadeTransition fadeIn = createFadeTransition(dialogueTextGame, Duration.millis(500), 0.0, 1.0);
                        fadeIn.play();
                    });
                    fadeOut.play();
                }
            })
        );
        
        dialogueTextTimeline.setCycleCount(Timeline.INDEFINITE);
        dialogueTextTimeline.play();
    }
    
    private String formatTime(long totalSeconds) {
        int minutes = (int)(totalSeconds / 60);
        int seconds = (int)(totalSeconds % 60);
        return String.format("%d:%02d", minutes, seconds);
    }
    
    private String formatTimeWithPrefix(long totalSeconds) {
        return "Time: " + formatTime(totalSeconds);
    }
    
    private boolean checkLevelObjectiveMet(int levelNumber, int totalLines, long timeElapsed, int tripleClearsCount, int currentScore, com.comp2042.logic.Level level) {
        switch (levelNumber) {
            case 1:
                return totalLines >= level.getTargetLines();
            case 2:
            case 5:
                return totalLines >= level.getTargetLines() && timeElapsed <= level.getTimeLimit();
            case 3:
                return tripleClearsCount >= 2;
            case 4:
                return currentScore >= level.getTargetScore();
            default:
                return false;
        }
    }
    
    private String generateLevelProgressText(int levelNumber, int totalLines, long timeElapsed, int tripleClearsCount, int currentScore, com.comp2042.logic.Level level) {
        switch (levelNumber) {
            case 1:
            case 2:
            case 5:
                return "Lines: " + totalLines + "/" + level.getTargetLines();
            case 3:
                return "Triple Clears: " + tripleClearsCount + "/2";
            case 4:
                return "Score: " + currentScore + "/" + level.getTargetScore();
            default:
                return "";
        }
    }
    
    private void removeRotateTransforms(javafx.scene.Node node) {
        if (node != null) {
            node.getTransforms().removeIf(transform -> transform instanceof Rotate);
        }
    }
    
    private boolean setupGameOverHighScore(int finalScore, String playerName) {
        if (!isGuest && playerName != null && !playerName.isEmpty() && !isInvertedMode() && !is1984Mode() && currentLevel == null) {
            return HighScoreManager.updateHighScore(finalScore, playerName);
        }
        return false;
    }
    
    private HighScoreInfo getHighScoreInfo() {
        int highScore = HighScoreManager.getHighScore();
        String highScoreHolder = HighScoreManager.getHighScoreHolder();
        return new HighScoreInfo(highScore, highScoreHolder);
    }
    
    private static class HighScoreInfo {
        final int highScore;
        final String highScoreHolder;
        
        HighScoreInfo(int highScore, String highScoreHolder) {
            this.highScore = highScore;
            this.highScoreHolder = highScoreHolder;
        }
    }
    
    private void setupInvertedGameOverUI() {
        if (gameOverPanel == null) {
            return;
        }
        
        if (gameOverPanel.getBackButtonInverted() != null) {
            gameOverPanel.getBackButtonInverted().setOnAction(_ -> returnToGamemodesMenu());
            applyInvertedRotation(gameOverPanel.getBackButtonInverted(), 25.0, 25.0);
        }
        
        gameOverPanel.applyInvertedLayout();
        
        Button restartBtn = gameOverPanel.getRestartButton();
        Button mainMenuBtn = gameOverPanel.getMainMenuButton();
        Label gameOverLbl = gameOverPanel.getGameOverLabel();
        Label scoreLbl = gameOverPanel.getScoreLabel();
        Label timeLbl = gameOverPanel.getTimePlayedLabel();
        
        if (timeLbl != null && timeLbl.isVisible()) {
            applyInvertedRotation(timeLbl, 180.0, 15.0);
        }
        
        if (restartBtn != null) {
            applyInvertedRotation(restartBtn, 120.0, 30.0);
        }
        
        if (mainMenuBtn != null) {
            applyInvertedRotation(mainMenuBtn, 120.0, 30.0);
        }
        
        if (gameOverLbl != null) {
            removeRotateTransforms(gameOverLbl);
            Platform.runLater(() -> {
                double width = gameOverLbl.getBoundsInLocal().getWidth();
                double height = gameOverLbl.getBoundsInLocal().getHeight();
                if (width > 0 && height > 0) {
                    Rotate rotate = new Rotate(INVERTED_ROTATION_ANGLE, width / 2, height / 2);
                    gameOverLbl.getTransforms().add(rotate);
                } else {
                    Rotate rotate = new Rotate(INVERTED_ROTATION_ANGLE, 360, 55);
                    gameOverLbl.getTransforms().add(rotate);
                }
            });
        }
        
        if (scoreLbl != null) {
            removeRotateTransforms(scoreLbl);
            Platform.runLater(() -> {
                double width = scoreLbl.getBoundsInLocal().getWidth();
                double height = scoreLbl.getBoundsInLocal().getHeight();
                if (width > 0 && height > 0) {
                    Rotate rotate = new Rotate(INVERTED_ROTATION_ANGLE, width / 2, height / 2);
                    scoreLbl.getTransforms().add(rotate);
                } else {
                    Rotate rotate = new Rotate(INVERTED_ROTATION_ANGLE, 360, 30);
                    scoreLbl.getTransforms().add(rotate);
                }
            });
        }
    }
    
    private void resetNormalGameOverUI() {
        if (gameOverPanel == null) {
            return;
        }
        
        Button restartBtn = gameOverPanel.getRestartButton();
        Button mainMenuBtn = gameOverPanel.getMainMenuButton();
        Label gameOverLbl = gameOverPanel.getGameOverLabel();
        Label scoreLbl = gameOverPanel.getScoreLabel();
        
        removeRotateTransforms(restartBtn);
        removeRotateTransforms(mainMenuBtn);
        removeRotateTransforms(gameOverLbl);
        removeRotateTransforms(scoreLbl);
    }
    
    private Timeline createIndefiniteTimeline(KeyFrame... keyFrames) {
        Timeline timeline = new Timeline(keyFrames);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        return timeline;
    }
    
    private Timeline createGameTimeline(int dropIntervalMs) {
        return createIndefiniteTimeline(
            new KeyFrame(Duration.millis(dropIntervalMs), _ -> moveDown(threadMove()))
        );
    }
    
    private void hideLevelCompleteAndShowGamePane() {
        if (levelCompleteContainer != null) {
            levelCompleteContainer.setVisible(false);
        }
        if (gamePane != null) {
            gamePane.setVisible(true);
        }
    }
    
    private FadeTransition createFadeTransition(javafx.scene.Node node, Duration duration, double fromValue, double toValue) {
        FadeTransition fade = new FadeTransition(duration, node);
        fade.setFromValue(fromValue);
        fade.setToValue(toValue);
        return fade;
    }
    
    private ScaleTransition createScaleTransition(javafx.scene.Node node, Duration duration, double fromX, double fromY, double toX, double toY) {
        ScaleTransition scale = new ScaleTransition(duration, node);
        scale.setFromX(fromX);
        scale.setFromY(fromY);
        scale.setToX(toX);
        scale.setToY(toY);
        return scale;
    }
    
    private SequentialTransition createSequentialTransition(javafx.animation.Transition... transitions) {
        return new SequentialTransition(transitions);
    }
    
    private void setupHighScoreDisplay(int highScore, String highScoreHolder) {
        if (!isInvertedMode() && !is1984Mode() && currentLevel == null) {
            highScoreLabel.setText("High Score: " + highScore);
            if (highScoreHolder != null && !highScoreHolder.isEmpty()) {
                highScoreHolderLabel.setText("by " + capitalizeName(highScoreHolder));
                highScoreHolderLabel.setVisible(true);
                animateHighScoreHolder();
            } else {
                highScoreHolderLabel.setVisible(false);
            }
        } else {
            highScoreLabel.setVisible(false);
            highScoreHolderLabel.setVisible(false);
        }
    }
    
    private void setupGameOverPanel(int finalScore, int highScore, boolean isNewHighScore, long timePlayed) {
        gameOverPanel.setGameMode(gameMode);
        gameOverPanel.showFinalScore(finalScore, highScore, isNewHighScore, playerName, isGuest, timePlayed);
        
        if (currentLevel != null && gameOverPanel.getLeaderboardButton() != null) {
            gameOverPanel.getLeaderboardButton().setVisible(false);
            gameOverPanel.getLeaderboardButton().setManaged(false);
        }
        
        if (is1984Mode() && gameOverPanel.getBackButton1984() != null) {
            gameOverPanel.getBackButton1984().setOnAction(_ -> returnToGamemodesMenu());
        }
    }
    
    private void handleInvertedKeyInput(KeyEvent keyEvent) {
        if (isRotateKey(keyEvent)) {
            moveDown(userMove(EventType.DOWN));
            keyEvent.consume();
        } else if (isDownKey(keyEvent)) {
            refreshBrick(eventListener.onRotateEvent(userMove(EventType.ROTATE)));
            keyEvent.consume();
        } else if (isLeftKey(keyEvent)) {
            refreshBrick(eventListener.onRightEvent(userMove(EventType.RIGHT)));
            keyEvent.consume();
        } else if (isRightKey(keyEvent)) {
            refreshBrick(eventListener.onLeftEvent(userMove(EventType.LEFT)));
            keyEvent.consume();
        } else if (keyEvent.getCode() == KeyCode.SPACE) {
            hardDrop();
            keyEvent.consume();
        } else if (keyEvent.getCode() == KeyCode.H) {
            holdBrick();
            keyEvent.consume();
        }
    }
    
    private void handleNormalKeyInput(KeyEvent keyEvent) {
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
    
    private void handleGameControlKeys(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.N) {
            newGame(null);
        } else if (keyEvent.getCode() == KeyCode.ESCAPE) {
            pauseGame(null);
        }
    }
    
    private static final int NEXT_BLOCK_PANE_SIZE = 90;
    
    private void configureNextBlockPane(StackPane pane, int size) {
        if (pane != null) {
            pane.setMinWidth(size);
            pane.setMinHeight(size);
            pane.setPrefWidth(size);
            pane.setPrefHeight(size);
        }
    }
    
    private void setupNextBlockPanes() {
        configureNextBlockPane(nextBlockStackPane, NEXT_BLOCK_PANE_SIZE);
        configureNextBlockPane(nextBlockStackPane2, NEXT_BLOCK_PANE_SIZE);
        configureNextBlockPane(nextBlockStackPane3, NEXT_BLOCK_PANE_SIZE);
    }
    
    private void setupLevelUI(com.comp2042.logic.Level level) {
        this.dropIntervalMs = level.getDropSpeed();
        this.tripleClearsCount = 0;
        this.levelStartTime = System.currentTimeMillis();
        this.totalPauseDuration = 0;
        this.pauseStartTime = 0;
        stopTimerUpdate();
        setNodeVisibility(highScoreLabel, false);
        setNodeVisibility(highScoreHolderLabel, false);
        updateLevelObjectiveLabel();
        
        if (rightPanel != null) {
            rightPanel.setLayoutX(520);
            rightPanel.setSpacing(20);
        }
        if (nextBlockContainer != null) {
            nextBlockContainer.setSpacing(5);
            nextBlockContainer.setStyle("");
        }
        setupNextBlockPanes();
        
        if (timerLabel != null) {
            boolean needsTimer = isTimedLevel();
            setNodeVisibility(timerLabel, needsTimer);
            if (needsTimer) {
                startTimerUpdate();
            }
        }
    }
    
    private void setupFreePlayUI() {
        this.dropIntervalMs = DEFAULT_DROP_INTERVAL_MS;
        if (isInvertedMode()) {
            setNodeVisibility(highScoreLabel, false);
            setNodeVisibility(highScoreHolderLabel, false);
            setNodeVisibility(objectivePanel, false);
        } else {
            setNodeVisibility(highScoreLabel, true);
            HighScoreInfo highScoreInfo = getHighScoreInfo();
            if (highScoreInfo.highScoreHolder != null && !highScoreInfo.highScoreHolder.isEmpty()) {
                setNodeVisibility(highScoreHolderLabel, true);
            }
            setNodeVisibility(objectivePanel, false);
        }
        updateLevelObjectiveLabel();
        
        if (rightPanel != null) {
            rightPanel.setLayoutX(520);
            rightPanel.setSpacing(20);
        }
        if (nextBlockContainer != null) {
            nextBlockContainer.setSpacing(5);
            nextBlockContainer.setStyle("");
        }
        setupNextBlockPanes();
        stopTimerUpdate();
        startFreePlayTimer();
    }
    
    private void pauseLevelTimer() {
        if (isTimedLevel()) {
            pauseStartTime = System.currentTimeMillis();
        }
    }
    
    private void resumeLevelTimer() {
        if (isTimedLevel()) {
            if (pauseStartTime > 0) {
                totalPauseDuration += System.currentTimeMillis() - pauseStartTime;
                pauseStartTime = 0;
            }
            startTimerUpdate();
        }
    }
    
    private void pauseFreePlayTimer() {
        if (freePlayTimerLine != null) {
            freePlayTimerLine.pause();
        }
        freePlayPauseStartTime = System.currentTimeMillis();
    }
    
    private void resumeFreePlayTimer() {
        if (freePlayPauseStartTime > 0) {
            freePlayTotalPauseDuration += (System.currentTimeMillis() - freePlayPauseStartTime);
            freePlayPauseStartTime = 0;
        }
        if (freePlayTimerLine != null) {
            freePlayTimerLine.play();
        }
        if (currentLevel == null) {
            updateFreePlayDialogueText();
        } else {
            updateLevelsDialogueText();
        }
    }
    
    private void initializeGameBoard(int[][] boardMatrix) {
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
    }
    
    private void initializeBrickPanels(ViewData brick) {
        int brickSize = getBrickSize();
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
    }
    
    private void setupDialogueForGame() {
        if (dialogueContainerGame != null && !is1984Mode() && !isInvertedMode()) {
            setNodeVisibility(dialogueContainerGame, true);
            dialogueContainerGame.setLayoutX(490);
            dialogueContainerGame.setLayoutY(390);
            if (currentLevel == null) {
                updateFreePlayDialogueText();
            } else {
                updateLevelsDialogueText();
            }
            Platform.runLater(() -> dialogueContainerGame.toFront());
        }
    }
    
    private void setupGhostRectangle(Rectangle ghostRect, int colorIndex, Paint blockColor) {
        if (blockColor instanceof Color) {
            Color originalColor = (Color) blockColor;
            ghostRect.setFill(createGhostColor(originalColor, GHOST_COLOR_OPACITY));
            ghostRect.setStroke(createGhostColor(originalColor, GHOST_BORDER_OPACITY));
        } else {
            ghostRect.setFill(Color.rgb(255, 255, 255, GHOST_COLOR_OPACITY));
            ghostRect.setStroke(Color.rgb(255, 255, 255, GHOST_BORDER_OPACITY));
        }
        ghostRect.setArcHeight(RECTANGLE_ARC_SIZE);
        ghostRect.setArcWidth(RECTANGLE_ARC_SIZE);
        ghostRect.setStrokeWidth(GHOST_STROKE_WIDTH);
    }
    
    private void handleGhostRectangleVisibility(Rectangle ghostRect, boolean shouldShow, boolean currentlyVisible) {
        if (shouldShow) {
            if (!currentlyVisible) {
                ghostRect.setOpacity(0.0);
                FadeTransition fadeIn = createFadeTransition(ghostRect, Duration.millis(GHOST_FADE_IN_DURATION_MS), 0.0, 1.0);
                fadeIn.play();
            } else {
                ghostRect.setOpacity(1.0);
            }
        } else {
            if (currentlyVisible) {
                FadeTransition fadeOut = createFadeTransition(ghostRect, Duration.millis(GHOST_FADE_OUT_DURATION_MS), ghostRect.getOpacity(), 0.0);
                fadeOut.setOnFinished(_ -> clearGhostRectangle(ghostRect));
                fadeOut.play();
            } else {
                clearGhostRectangle(ghostRect);
            }
        }
    }
    
    private void startLevelGameMode() {
        tripleClearsCount = 0;
        levelStartTime = System.currentTimeMillis();
        totalPauseDuration = 0;
        pauseStartTime = 0;
        stopTimerUpdate();
        updateLevelObjectiveLabel();
        if (is1984Mode()) {
            setPlayerName("1984 Player", false);
            startGameInstantly();
        } else {
            startCountdown();
        }
    }
    
    private void startSpecialModeGame() {
        stopFreePlayTimer();
        if (is1984Mode()) {
            setPlayerName("1984 Player", false);
        } else {
            setPlayerName("Inverted Player", false);
        }
        updateLevelObjectiveLabel();
        if (is1984Mode()) {
            startGameInstantly();
        } else {
            startCountdown();
        }
    }
    
    private void startFreePlayGame() {
        requestPlayerNameAndStart();
    }
    
    private void cleanup1984ModeUI() {
        if (scoreLabel1984 != null) {
            scoreLabel1984.setVisible(false);
        }
        if (linesLabel1984 != null) {
            linesLabel1984.setVisible(false);
        }
        stopTimelineSafely(glitchTimeline1984);
        if (filmGrainOverlay != null) {
            filmGrainOverlay.setVisible(false);
        }
        stopTimelineSafely(filmGrainTimeline);
        stopTimelineSafely(flickerTimeline);
        if (rootPane != null) {
            rootPane.setEffect(null);
            rootPane.setOpacity(1.0);
        }
    }
    
    private void setupRootPaneRotation(boolean isInvertedMode) {
        if (isInvertedMode && rootPane != null) {
            rootPane.getTransforms().removeIf(transform -> transform instanceof Rotate);
            Rotate rotate = new Rotate(INVERTED_ROTATION_ANGLE, 360, 340);
            rootPane.getTransforms().add(rotate);
        } else if (rootPane != null) {
            rootPane.getTransforms().removeIf(transform -> transform instanceof Rotate);
        }
    }
    
    private void setupInvertedModeUI() {
        if (objectivePanel != null) {
            objectivePanel.setVisible(false);
        }
        if (rightPanel != null) {
            rightPanel.setVisible(true);
            rightPanel.setManaged(true);
            rightPanel.setLayoutX(480);
            rightPanel.setLayoutY(130);
            applyInvertedRotation(rightPanel, 115.0, 125.0);
        }
        setNodeVisibility(holdBlockContainer, true);
        if (leftPanel != null) {
            leftPanel.setLayoutX(0);
            leftPanel.setLayoutY(100);
            applyInvertedRotation(leftPanel, 75.0, 150.0);
        }
        setNodeVisibility(creatorPanel, false);
        setNodeVisibility(dialogueContainerGame, false);
        setNodeVisibility(whiteBoxContainer1984, false);
        if (backButton != null) {
            backButton.setLayoutX(WINDOW_WIDTH - BACK_BUTTON_OFFSET_X);
            backButton.setLayoutY(WINDOW_HEIGHT - BACK_BUTTON_OFFSET_Y);
            applyInvertedRotation(backButton, 20.0, 20.0);
        }
    }
    
    private void setupNormalModeUI(boolean is1984Mode) {
        stopTimelineSafely(backgroundFlickerTimeline);
        if (background1984 != null) {
            setNodeVisibility(background1984, false);
            background1984.setOpacity(1.0);
        }
        if (objectivePanel != null) {
            objectivePanel.setVisible(false);
        }
        if (rightPanel != null) {
            rightPanel.setVisible(true);
            rightPanel.setManaged(true);
            rightPanel.getTransforms().removeIf(transform -> transform instanceof Rotate);
        }
        setNodeVisibility(holdBlockContainer, true);
        setNodeVisibility(nextBlockContainer, true);
        if (leftPanel != null) {
            leftPanel.setLayoutX(30);
            leftPanel.setLayoutY(120);
            leftPanel.getTransforms().removeIf(transform -> transform instanceof Rotate);
        }
        setNodeVisibility(creatorPanel, true);
        if (dialogueContainerGame != null && !is1984Mode && !isInvertedMode()) {
            setNodeVisibility(dialogueContainerGame, true);
            dialogueContainerGame.setLayoutX(490);
            dialogueContainerGame.setLayoutY(390);
            if (currentLevel == null) {
                updateFreePlayDialogueText();
            } else {
                updateLevelsDialogueText();
            }
            Platform.runLater(() -> dialogueContainerGame.toFront());
        } else {
            setNodeVisibility(dialogueContainerGame, false);
        }
        setNodeVisibility(whiteBoxContainer1984, false);
        if (backButton != null) {
            backButton.setLayoutX(15);
            backButton.setLayoutY(15);
            backButton.getTransforms().removeIf(transform -> transform instanceof Rotate);
        }
    }
    
    private void setup1984ModeUI() {
        if (background1984 != null) {
            background1984.setVisible(true);
            background1984.setManaged(true);
            background1984.setOpacity(1.0);
            rootPane.getChildren().remove(background1984);
            rootPane.getChildren().add(0, background1984);
            startBackgroundFlickerAnimation();
        }
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
            creatorPanel.setLayoutX(590);
            creatorPanel.setLayoutY(480);
            javafx.animation.TranslateTransition floatUp = new javafx.animation.TranslateTransition(Duration.seconds(2), creatorPanel);
            floatUp.setByY(-10);
            floatUp.setCycleCount(Timeline.INDEFINITE);
            floatUp.setAutoReverse(true);
            floatUp.play();
        }
        if (whiteBoxContainer1984 != null) {
            whiteBoxContainer1984.setVisible(true);
            whiteBoxContainer1984.setManaged(true);
            whiteBoxContainer1984.setLayoutX(480);
            whiteBoxContainer1984.setLayoutY(420);
            Platform.runLater(() -> whiteBoxContainer1984.toFront());
        }
        if (backButton != null) {
            backButton.getStyleClass().clear();
            backButton.getStyleClass().add("back-icon-1984");
            backButton.setVisible(true);
            backButton.setManaged(true);
            backButton.toFront();
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
            
            double boardWidth = 220; 
            double boardHeight = 445;  
            
            double centerX = (WINDOW_WIDTH - boardWidth) / 2;
            double centerY = (WINDOW_HEIGHT - boardHeight) / 2;
            
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
    }
}
