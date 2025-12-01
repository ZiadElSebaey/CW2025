package com.comp2042.ui;

import com.comp2042.logic.DownData;
import com.comp2042.logic.EventSource;
import com.comp2042.logic.EventType;
import com.comp2042.logic.InputEventListener;
import com.comp2042.logic.MoveEvent;
import javafx.animation.KeyFrame;
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
    private static final int DROP_INTERVAL_MS = 400;
    private static final int BRICK_PANEL_Y_OFFSET = -20;
    private static final int BORDER_WIDTH = 12;
    private static final int HIDDEN_ROWS = 2;
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

    private Rectangle[][] displayMatrix;
    
    private IntegerProperty currentScoreProperty;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;
    
    private Rectangle[][] ghostRectangles;

    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        URL digitalFontUrl = getClass().getClassLoader().getResource("digital.ttf");
        if (digitalFontUrl != null) {
            Font.loadFont(digitalFontUrl.toExternalForm(), 38);
        }
        HighScoreManager.ensureDirectoryExists();
        highScoreLabel.setText("High Score: " + HighScoreManager.getHighScore());
        
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(this::handleKeyPressed);
        gameOverPanel.setVisible(false);
        gameOverContainer.setVisible(false);
        pausePanel.setVisible(false);
        pauseContainer.setVisible(false);

        gameOverPanel.getRestartButton().setOnAction(_ -> newGame(null));
        gameOverPanel.getMainMenuButton().setOnAction(_ -> returnToMainMenu());

        pausePanel.getResumeButton().setOnAction(_ -> resumeGame());
        pausePanel.getRestartButton().setOnAction(_ -> newGame(null));
        pausePanel.getSettingsButton().setOnAction(_ -> openSettingsFromPause());
        pausePanel.getMainMenuButton().setOnAction(_ -> returnToMainMenu());

        AnimatedBackground animatedBackground = new AnimatedBackground(720, 680);
        rootPane.getChildren().addFirst(animatedBackground);
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
        if (!isPause.get() && !isGameOver.get() && eventListener != null) {
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
            }
        }

        if (keyEvent.getCode() == KeyCode.N) {
            newGame(null);
        } else if (keyEvent.getCode() == KeyCode.P) {
            pauseGame(null);
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



        timeLine = new Timeline(new KeyFrame(
                Duration.millis(DROP_INTERVAL_MS),
                _ -> moveDown(threadMove())
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }
    private void updateBrickPanelPosition(ViewData brick) {
        double cellSize = brickPanel.getVgap() + BRICK_SIZE;
        double x = gameBoard.getLayoutX() + BORDER_WIDTH + brick.getxPosition() * cellSize;
        double y = BRICK_PANEL_Y_OFFSET + gameBoard.getLayoutY() + BORDER_WIDTH
                + (brick.getyPosition() - HIDDEN_ROWS) * cellSize;

        brickPanel.setLayoutX(x);
        brickPanel.setLayoutY(y);
    }

    private void updateGhostPosition(ViewData brick) {
        double cellSize = ghostPanel.getVgap() + BRICK_SIZE;
        double x = gameBoard.getLayoutX() + BORDER_WIDTH + brick.getxPosition() * cellSize;
        double ghostY = gameBoard.getLayoutY() + BORDER_WIDTH
                + (brick.getGhostY() - HIDDEN_ROWS) * cellSize;

        ghostPanel.setLayoutX(x);
        ghostPanel.setLayoutY(ghostY);

        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                if (brick.getBrickData()[i][j] != 0) {
                    ghostRectangles[i][j].setFill(Color.rgb(255, 255, 255, 0.3));
                    ghostRectangles[i][j].setArcHeight(RECTANGLE_ARC_SIZE);
                    ghostRectangles[i][j].setArcWidth(RECTANGLE_ARC_SIZE);
                    ghostRectangles[i][j].setStroke(Color.rgb(255, 255, 255, 0.5));
                    ghostRectangles[i][j].setStrokeWidth(1);
                } else {
                    ghostRectangles[i][j].setFill(Color.TRANSPARENT);
                    ghostRectangles[i][j].setStroke(Color.TRANSPARENT);
                }
            }
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
    }

    public void gameOver() {
        setPaused(true);
        gamePane.setVisible(false);
        
        int finalScore = currentScoreProperty != null ? currentScoreProperty.get() : 0;
        boolean isNewHighScore = HighScoreManager.updateHighScore(finalScore);
        gameOverPanel.showFinalScore(finalScore, HighScoreManager.getHighScore(), isNewHighScore);
        highScoreLabel.setText("High Score: " + HighScoreManager.getHighScore());
        
        gameOverPanel.setVisible(true);
        gameOverContainer.setVisible(true);
        isGameOver.set(true);
    }

    public void newGame(ActionEvent actionEvent) {
        if (actionEvent != null) {
            actionEvent.consume();
        }
        startNewGame();
    }
    private void startNewGame() {
        gamePane.setVisible(true);
        gameOverPanel.setVisible(false);
        gameOverContainer.setVisible(false);
        pausePanel.setVisible(false);
        pauseContainer.setVisible(false);
        isGameOver.set(false);
        setPaused(false);

        ViewData newBrickData = eventListener.createNewGame();
        refreshBrick(newBrickData);
        gamePanel.requestFocus();
    }
    private void setPaused(boolean paused) {
        isPause.set(paused);

        if (timeLine == null) {
            return;
        }

        if (paused) {
            timeLine.stop();
        } else {
            timeLine.play();
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
        gamePanel.requestFocus();
    }

    public boolean isPaused() {
        return isPause.get();
    }

    public boolean isGameOver() {
        return isGameOver.get();
    }

    public void requestFocus() {
        gamePanel.requestFocus();
    }
}
