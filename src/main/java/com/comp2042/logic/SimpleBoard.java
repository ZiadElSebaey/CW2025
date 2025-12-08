package com.comp2042.logic;

import com.comp2042.bricks.Brick;
import com.comp2042.bricks.BrickGenerator;
import com.comp2042.bricks.BrickRotator;
import com.comp2042.bricks.RandomBrickGenerator;
import com.comp2042.ui.ViewData;

import java.awt.*;

/**
 * Implementation of the Board interface representing the game board.
 * Manages the game state, brick movement, rotation, and row clearing.
 * Supports standard Tetris gameplay with hold functionality.
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 * @see Board
 */
public class SimpleBoard implements Board {
    public static final int BOARD_ROWS = 27;
    public static final int BOARD_COLUMNS = 13;

    private static final int SPAWN_X = 5;
    private static final int SPAWN_Y = 2;

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private Brick heldBrick;
    private boolean canHold = true;
    
    /**
     * Creates a new SimpleBoard with default dimensions.
     */
    public SimpleBoard() {
        this(BOARD_ROWS, BOARD_COLUMNS);
    }

    /**
     * Creates a new SimpleBoard with custom dimensions.
     * 
     * @param width the number of rows in the board
     * @param height the number of columns in the board
     */
    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    private boolean hasCollision(int[][] shape, Point offset) {
        return MatrixOperations.intersect(
                currentGameMatrix,
                shape,
                (int) offset.getX(),
                (int) offset.getY()
        );
    }
    private boolean tryMove(int deltaX, int deltaY) {
        Point newOffset = new Point(currentOffset);
        newOffset.translate(deltaX, deltaY);

        if (hasCollision(brickRotator.getCurrentShape(), newOffset)) {
            return false;
        }

        currentOffset = newOffset;
        return true;
    }

    @Override
    public boolean moveBrickDown() {
        return tryMove(0, 1);
    }
    @Override
    public boolean moveBrickLeft() {
        return tryMove(-1, 0);
    }

    @Override
    public boolean moveBrickRight() {
        return tryMove(1, 0);
    }

    @Override
    public boolean rotateBrick() {
        NextShapeInfo nextShape = brickRotator.getNextShape();

        if (hasCollision(nextShape.getShape(), currentOffset)) {
            return false;
        }

        brickRotator.setCurrentShape(nextShape.getPosition());
        return true;
    }

    @Override
    public boolean spawnNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(SPAWN_X, SPAWN_Y);
        canHold = true;

        return hasCollision(brickRotator.getCurrentShape(), currentOffset);
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    @Override
    public ViewData getViewData() {
        int[][] holdData = heldBrick != null ? heldBrick.getShapeMatrix().get(0) : null;
        java.util.List<com.comp2042.bricks.Brick> nextBricks = brickGenerator.getNextBricks(3);
        int[][] next1 = nextBricks.get(0).getShapeMatrix().get(0);
        int[][] next2 = nextBricks.size() > 1 ? nextBricks.get(1).getShapeMatrix().get(0) : null;
        int[][] next3 = nextBricks.size() > 2 ? nextBricks.get(2).getShapeMatrix().get(0) : null;
        return new ViewData(brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY(), next1, next2, next3, getGhostY(), holdData);
    }

    /**
     * Gets the Y coordinate where the ghost brick would land.
     * 
     * @return the Y coordinate of the ghost brick position
     */
    public int getGhostY() {
        int ghostY = (int) currentOffset.getY();
        Point testOffset = new Point(currentOffset);
        while (!hasCollision(brickRotator.getCurrentShape(), testOffset)) {
            ghostY = (int) testOffset.getY();
            testOffset.translate(0, 1);
        }
        return ghostY;
    }

    @Override
    public int hardDrop() {
        int dropDistance = 0;
        while (tryMove(0, 1)) {
            dropDistance++;
        }
        return dropDistance;
    }

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;
    }

    @Override
    public Score getScore() {
        return score;
    }

    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        heldBrick = null;
        canHold = true;
        spawnNewBrick();
    }
    
    @Override
    public ViewData holdBrick() {
        if (!canHold) {
            return getViewData();
        }
        
        Brick currentBrick = brickRotator.getBrick();
        
        if (heldBrick == null) {
            heldBrick = currentBrick;
            Brick nextBrick = brickGenerator.getBrick();
            brickRotator.setBrick(nextBrick);
            currentOffset = new Point(SPAWN_X, SPAWN_Y);
        } else {
            Brick temp = heldBrick;
            heldBrick = currentBrick;
            brickRotator.setBrick(temp);
            currentOffset = new Point(SPAWN_X, SPAWN_Y);
        }
        
        canHold = false;
        return getViewData();
    }
    
    /**
     * Gets the currently held brick.
     * 
     * @return the held brick, or {@code null} if no brick is held
     */
    public Brick getHeldBrick() {
        return heldBrick;
    }
}
