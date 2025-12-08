package com.comp2042.ui;

import com.comp2042.logic.MatrixOperations;

/**
 * Immutable data class containing all information needed to render the game view.
 * Includes current brick position, next bricks, ghost brick position, and held brick.
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 */
public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;
    private final int[][] nextBrickData2;
    private final int[][] nextBrickData3;
    private final int ghostY;
    private final int[][] holdBrickData;

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int ghostY, int[][] holdBrickData) {
        this(brickData, xPosition, yPosition, nextBrickData, null, null, ghostY, holdBrickData);
    }
    
    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int ghostY) {
        this(brickData, xPosition, yPosition, nextBrickData, null, null, ghostY, null);
    }
    
    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int[][] nextBrickData2, int[][] nextBrickData3, int ghostY, int[][] holdBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.nextBrickData2 = nextBrickData2;
        this.nextBrickData3 = nextBrickData3;
        this.ghostY = ghostY;
        this.holdBrickData = holdBrickData;
    }

    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }
    
    public int[][] getNextBrickData2() {
        return nextBrickData2 != null ? MatrixOperations.copy(nextBrickData2) : null;
    }
    
    public int[][] getNextBrickData3() {
        return nextBrickData3 != null ? MatrixOperations.copy(nextBrickData3) : null;
    }

    public int getGhostY() {
        return ghostY;
    }
    
    public int[][] getHoldBrickData() {
        return holdBrickData != null ? MatrixOperations.copy(holdBrickData) : null;
    }
}
