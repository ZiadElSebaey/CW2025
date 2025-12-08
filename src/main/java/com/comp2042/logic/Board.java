package com.comp2042.logic;

import com.comp2042.ui.ViewData;

/**
 * Represents the game board interface for the Tetris game.
 * Defines the main operations for manipulating the board state,
 * moving bricks, and managing game logic.
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 * @see SimpleBoard
 */
public interface Board {

    /**
     * Attempts to move the current brick down one cell.
     * 
     * @return {@code true} if the move was successful, {@code false} if blocked
     */
    boolean moveBrickDown();

    /**
     * Attempts to move the current brick left one cell.
     * 
     * @return {@code true} if the move was successful, {@code false} if blocked
     */
    boolean moveBrickLeft();

    /**
     * Attempts to move the current brick right one cell.
     * 
     * @return {@code true} if the move was successful, {@code false} if blocked
     */
    boolean moveBrickRight();

    /**
     * Attempts to rotate the current brick 90 degrees clockwise.
     * 
     * @return {@code true} if the rotation was successful, {@code false} if blocked
     */
    boolean rotateBrick();

    /**
     * Spawns a new brick at the top of the board.
     * 
     * @return {@code true} if the game is over (spawn blocked), {@code false} otherwise
     */
    boolean spawnNewBrick();
    
    /**
     * Gets the current board matrix representing all placed blocks.
     * 
     * @return a 2D array representing the board state
     */
    int[][] getBoardMatrix();

    /**
     * Gets the current view data including brick positions and next shapes.
     * 
     * @return the current view data
     */
    ViewData getViewData();

    /**
     * Merges the current brick into the background board matrix.
     */
    void mergeBrickToBackground();

    /**
     * Clears completed rows and returns information about the cleared rows.
     * 
     * @return a ClearRow object containing cleared row information
     */
    ClearRow clearRows();

    /**
     * Gets the score manager for this board.
     * 
     * @return the Score object
     */
    Score getScore();

    /**
     * Resets the board to start a new game.
     */
    void newGame();
    
    /**
     * Performs a hard drop, instantly dropping the brick to the bottom.
     * 
     * @return the number of cells the brick dropped
     */
    int hardDrop();
    
    /**
     * Holds the current brick and swaps it with the previously held brick.
     * 
     * @return the updated view data after holding
     */
    ViewData holdBrick();
}
