package com.comp2042.ui;

import com.comp2042.logic.*;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
    private GameOverPanel gameOverPanel;

    @FXML
    private BorderPane gameBoard;

    private Rectangle[][] displayMatrix;


    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

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
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(this::handleKeyPressed);
        gameOverPanel.setVisible(false);

        gameOverPanel.getRestartButton().setOnAction(e -> newGame(null));
        gameOverPanel.getMainMenuButton().setOnAction(e -> returnToMainMenu());
    }

    private void returnToMainMenu() {
        if (timeLine != null) {
            timeLine.stop();
        }
        try {
            URL location = getClass().getClassLoader().getResource("mainMenu.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            Parent root = fxmlLoader.load();
            MainMenuController menuController = fxmlLoader.getController();
            menuController.setStage(stage);
            Scene scene = new Scene(root, 720, 680);
            stage.setScene(scene);
        } catch (IOException ex) {
            ex.printStackTrace();
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
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
        updateBrickPanelPosition(brick);



        timeLine = new Timeline(new KeyFrame(
                Duration.millis(DROP_INTERVAL_MS),
                ae -> moveDown(threadMove(EventType.DOWN))
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

    private MoveEvent threadMove(EventType type) {
        return new MoveEvent(type, EventSource.THREAD);
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

    public void bindScore(IntegerProperty integerProperty) {
    }

    public void gameOver() {
        setPaused(true);
        gameOverPanel.setVisible(true);
        isGameOver.set(true);
    }

    public void newGame(ActionEvent actionEvent) {
        if (actionEvent != null) {
            actionEvent.consume();
        }
        startNewGame();
    }
    private void startNewGame() {
        gameOverPanel.setVisible(false);
        isGameOver.set(false);

        ViewData newBrickData = eventListener.createNewGame();
        refreshBrick(newBrickData);
        gamePanel.requestFocus();

        setPaused(false);
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

        setPaused(!isPause.get());
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
