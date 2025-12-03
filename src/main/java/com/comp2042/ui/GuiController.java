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
import javafx.scene.control.Label;
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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;
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
    private javafx.scene.control.Button backButton;

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

    private Timeline timeLine;
    private Timeline timerUpdateLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private Stage stage;
    
    private String playerName;
    private boolean isGuest;
    private com.comp2042.logic.Level currentLevel;
    private int tripleClearsCount;
    private long levelStartTime;
    private long pauseStartTime;
    private long totalPauseDuration;
    private int dropIntervalMs = DEFAULT_DROP_INTERVAL_MS;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void setPlayerName(String name, boolean guest) {
        this.playerName = name;
        this.isGuest = guest;
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
            highScoreLabel.setVisible(true);
            if (HighScoreManager.getHighScoreHolder() != null && !HighScoreManager.getHighScoreHolder().isEmpty()) {
                highScoreHolderLabel.setVisible(true);
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
            if (timerLabel != null) {
                timerLabel.setVisible(false);
            }
            stopTimerUpdate();
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
    
    private void updateLevelObjectiveLabel() {
        if (objectiveLabel != null) {
            if (currentLevel == null) {
                objectiveLabel.setText("PLAY FREELY!!");
                objectiveLabel.setVisible(true);
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
        
        updateLevelObjectiveLabel();

        gameOverPanel.getRestartButton().setOnAction(_ -> newGame(null));
        gameOverPanel.getMainMenuButton().setOnAction(_ -> returnToMainMenu());
        gameOverPanel.getLeaderboardButton().setOnAction(_ -> showLeaderboard());

        pausePanel.getResumeButton().setOnAction(_ -> resumeGame());
        pausePanel.getRestartButton().setOnAction(_ -> newGame(null));
        pausePanel.getSettingsButton().setOnAction(_ -> openSettingsFromPause());
        pausePanel.getLeaderboardButton().setOnAction(_ -> showLeaderboard());
        pausePanel.getMainMenuButton().setOnAction(_ -> returnToMainMenu());

        AnimatedBackground animatedBackground = new AnimatedBackground(720, 680);
        rootPane.getChildren().addFirst(animatedBackground);

        backButton.setOnAction(_ -> returnToMainMenu());
        setupLeaderboardPanel();
        setupHoldBlockAnimation();
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
        setPaused(false);
        pausePanel.setVisible(false);
        pauseContainer.setVisible(false);
        gamePanel.requestFocus();
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
    private void handleKeyPressed(KeyEvent keyEvent) {
        if (isGameOver.get()) {
            keyEvent.consume();
            return;
        }
        
        if (!isPause.get() && eventListener != null) {
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
        
        if (!isGameOver.get()) {
            if (keyEvent.getCode() == KeyCode.N) {
                newGame(null);
            } else if (keyEvent.getCode() == KeyCode.ESCAPE) {
                pauseGame(null);
            }
        }
    }


    public void initGameView(int[][] boardMatrix, ViewData brick) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = HIDDEN_ROWS; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - HIDDEN_ROWS);
            }
        }

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        ghostRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
                
                Rectangle ghostRect = new Rectangle(BRICK_SIZE, BRICK_SIZE);
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
        double cellSize = brickPanel.getVgap() + BRICK_SIZE;
        double x = gameBoard.getLayoutX() + BOARD_PADDING + brick.getxPosition() * cellSize;
        double y = BRICK_PANEL_Y_OFFSET + gameBoard.getLayoutY() + BOARD_PADDING
                + (brick.getyPosition() - HIDDEN_ROWS) * cellSize;

        brickPanel.setLayoutX(x);
        brickPanel.setLayoutY(y);
    }

    private void updateGhostPosition(ViewData brick) {
        double cellSize = ghostPanel.getVgap() + BRICK_SIZE;
        double x = gameBoard.getLayoutX() + BOARD_PADDING + brick.getxPosition() * cellSize;
        double ghostY = gameBoard.getLayoutY() + BOARD_PADDING
                + (brick.getGhostY() - HIDDEN_ROWS) * cellSize;

        ghostPanel.setLayoutX(x);
        ghostPanel.setLayoutY(ghostY);

        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                final int row = i;
                final int col = j;
                Rectangle ghostRect = ghostRectangles[i][j];
                boolean wasTransparent = ghostRect.getFill().equals(Color.TRANSPARENT);
                
                if (brick.getBrickData()[i][j] != 0) {
                    ghostRect.setFill(Color.rgb(255, 255, 255, 0.3));
                    ghostRect.setArcHeight(RECTANGLE_ARC_SIZE);
                    ghostRect.setArcWidth(RECTANGLE_ARC_SIZE);
                    ghostRect.setStroke(Color.rgb(255, 255, 255, 0.5));
                    ghostRect.setStrokeWidth(1);
                    
                    if (wasTransparent) {
                        ghostRect.setOpacity(0.0);
                        FadeTransition fadeIn = new FadeTransition(Duration.millis(80), ghostRect);
                        fadeIn.setFromValue(0.0);
                        fadeIn.setToValue(1.0);
                        fadeIn.play();
                    }
                } else {
                    if (!wasTransparent) {
                        FadeTransition fadeOut = new FadeTransition(Duration.millis(60), ghostRect);
                        fadeOut.setFromValue(ghostRect.getOpacity());
                        fadeOut.setToValue(0.0);
                        fadeOut.setOnFinished(_ -> {
                            ghostRectangles[row][col].setFill(Color.TRANSPARENT);
                            ghostRectangles[row][col].setStroke(Color.TRANSPARENT);
                            ghostRectangles[row][col].setOpacity(1.0);
                        });
                        fadeOut.play();
                    } else {
                        ghostRect.setFill(Color.TRANSPARENT);
                        ghostRect.setStroke(Color.TRANSPARENT);
                    }
                }
            }
        }
    }

    private void initNextBlockPanel(ViewData brick) {
        int[][] nextData = brick.getNextBrickData();
        nextBlockRectangles = new Rectangle[nextData.length][nextData[0].length];
        for (int i = 0; i < nextData.length; i++) {
            for (int j = 0; j < nextData[i].length; j++) {
                Rectangle rect = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rect.setArcHeight(RECTANGLE_ARC_SIZE);
                rect.setArcWidth(RECTANGLE_ARC_SIZE);
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
        int maxSize = 4;
        holdBlockRectangles = new Rectangle[maxSize][maxSize];
        previousHoldData = null;
        for (int i = 0; i < maxSize; i++) {
            for (int j = 0; j < maxSize; j++) {
                Rectangle rect = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rect.setArcHeight(RECTANGLE_ARC_SIZE);
                rect.setArcWidth(RECTANGLE_ARC_SIZE);
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


    private void refreshBrick(ViewData brick) {
        if (!isPause.get()) {
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
        rectangle.setArcHeight(RECTANGLE_ARC_SIZE);
        rectangle.setArcWidth(RECTANGLE_ARC_SIZE);
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
        scoreProperty.addListener((_, oldVal, newVal) -> {
            if (newVal.intValue() > oldVal.intValue()) {
                animateLabel(scoreLabel);
            }
        });
    }

    public void bindLines(IntegerProperty linesProperty) {
        linesLabel.textProperty().bind(linesProperty.asString("Lines: %d"));
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
        if (!isGuest && playerName != null && !playerName.isEmpty()) {
            isNewHighScore = HighScoreManager.updateHighScore(finalScore, playerName);
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
        
        gameOverPanel.showFinalScore(finalScore, highScore, isNewHighScore, playerName, isGuest);
        
        gameOverPanel.setVisible(true);
        gameOverContainer.setVisible(true);
        isGameOver.set(true);
        
        gamePanel.setFocusTraversable(false);
        gamePanel.setDisable(true);
    }

    public void newGame(ActionEvent actionEvent) {
        if (actionEvent != null) {
            actionEvent.consume();
        }
        if (currentLevel != null) {
            tripleClearsCount = 0;
            levelStartTime = System.currentTimeMillis();
            totalPauseDuration = 0;
            pauseStartTime = 0;
            stopTimerUpdate();
            updateLevelObjectiveLabel();
            startNewGame();
        } else {
            requestPlayerNameAndStart();
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
        startNewGame();
    }
    
    private void startNewGame() {
        gamePane.setVisible(true);
        gameOverPanel.setVisible(false);
        gameOverContainer.setVisible(false);
        pausePanel.setVisible(false);
        pauseContainer.setVisible(false);
        if (levelCompleteContainer != null) {
            levelCompleteContainer.setVisible(false);
        }
        isGameOver.set(false);
        setPaused(false);
        
        if (currentLevel != null && (currentLevel.getLevelNumber() == 2 || currentLevel.getLevelNumber() == 5)) {
            levelStartTime = System.currentTimeMillis();
            totalPauseDuration = 0;
            pauseStartTime = 0;
            stopTimerUpdate();
            startTimerUpdate();
        }
        
        gamePanel.setFocusTraversable(true);
        gamePanel.setDisable(false);

        if (eventListener != null) {
            ViewData newBrickData = eventListener.createNewGame();
            refreshBrick(newBrickData);
        }
        gamePanel.requestFocus();
    }
    
    public void startLevelGame() {
        if (currentLevel != null && eventListener == null) {
            eventListener = new com.comp2042.logic.GameController(this);
        }
        if (currentLevel != null) {
            startNewGame();
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
        } else {
            timeLine.play();
            if (currentLevel != null && (currentLevel.getLevelNumber() == 2 || currentLevel.getLevelNumber() == 5)) {
                if (pauseStartTime > 0) {
                    totalPauseDuration += System.currentTimeMillis() - pauseStartTime;
                    pauseStartTime = 0;
                }
                startTimerUpdate();
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
    }

    public boolean isGameOver() {
        return isGameOver.get();
    }

    public void requestFocus() {
        gamePanel.requestFocus();
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
