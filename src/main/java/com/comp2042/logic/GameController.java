package com.comp2042.logic;

import com.comp2042.ui.GuiController;
import com.comp2042.ui.ViewData;
import javafx.beans.property.IntegerProperty;

/**
 * Main game controller that coordinates between the game logic (Board)
 * and the user interface (GuiController). Handles user input events
 * and manages game state transitions.
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 * @see Board
 * @see com.comp2042.ui.GuiController
 * @see InputEventListener
 */
public class GameController implements InputEventListener {

    private final Board board = new SimpleBoard();

    private final GuiController viewGuiController;

    /**
     * Creates a new GameController with the specified GUI controller.
     * 
     * @param c the GUI controller to coordinate with
     */
    public GameController(GuiController c) {
        this.viewGuiController = c;
        initializeGame();
    }
    
    /**
     * Initializes the game by spawning the first brick and setting up UI bindings.
     */
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
    
    /**
     * Handles the event when a brick lands and can no longer move down.
     * Merges the brick, clears rows, and spawns a new brick.
     * 
     * @return information about cleared rows, or {@code null} if no rows were cleared
     */
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
    
    /**
     * Applies score bonuses for cleared rows.
     * 
     * @param clearRow the ClearRow object containing cleared row information
     */
    private void applyRowScore(ClearRow clearRow) {
        if (clearRow != null && clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());
            board.getScore().addLines(clearRow.getLinesRemoved());
            viewGuiController.onLinesCleared(clearRow.getLinesRemoved());
        }
    }
    
    /**
     * Performs a move action and returns the updated view data.
     * 
     * @param moveAction the move action to perform
     * @return the updated view data after the move
     */
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
    
    /**
     * Gets the lines property for UI binding.
     * 
     * @return the lines property
     */
    public IntegerProperty getLinesProperty() {
        return board.getScore().linesProperty();
    }
}
