package com.comp2042.logic;

import com.comp2042.ui.GuiController;
import com.comp2042.ui.ViewData;
import javafx.beans.property.IntegerProperty;

public class GameController implements InputEventListener {

    private final Board board = new SimpleBoard();

    private final GuiController viewGuiController;

    public GameController(GuiController c) {
        this.viewGuiController = c;
        initializeGame();
    }
    private void initializeGame() {
        board.spawnNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
        viewGuiController.bindLines(board.getScore().linesProperty());
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;

        if (!canMove) {
            clearRow = handleBrickLanded();
        } else if (event.getEventSource() == EventSource.USER) {
            board.getScore().add(1);
        }

        return new DownData(clearRow, board.getViewData());
    }
    private ClearRow handleBrickLanded() {
        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();
        applyRowScore(clearRow);
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        
        if (board.spawnNewBrick()) {
            viewGuiController.gameOver();
        }

        return clearRow;
    }
    private void applyRowScore(ClearRow clearRow) {
        if (clearRow != null && clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());
            board.getScore().addLines(clearRow.getLinesRemoved());
            viewGuiController.onLinesCleared(clearRow.getLinesRemoved());
        }
    }
    private ViewData performMove(Runnable moveAction) {
        moveAction.run();
        return board.getViewData();
    }


    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        return performMove(board::moveBrickLeft);
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        return performMove(board::moveBrickRight);
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        return performMove(board::rotateBrick);
    }


    @Override
    public ViewData createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        return board.getViewData();
    }

    @Override
    public DownData onHardDropEvent() {
        int dropDistance = board.hardDrop();
        board.getScore().add(dropDistance * 2);
        ClearRow clearRow = handleBrickLanded();
        return new DownData(clearRow, board.getViewData());
    }
    
    @Override
    public ViewData onHoldEvent() {
        return board.holdBrick();
    }
    
    public IntegerProperty getLinesProperty() {
        return board.getScore().linesProperty();
    }
}
