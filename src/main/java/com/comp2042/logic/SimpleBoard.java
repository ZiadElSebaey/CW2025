package com.comp2042.logic;

import com.comp2042.bricks.Brick;
import com.comp2042.bricks.BrickGenerator;
import com.comp2042.bricks.BrickRotator;
import com.comp2042.bricks.RandomBrickGenerator;
import com.comp2042.ui.ViewData;

import java.awt.*;

public class SimpleBoard implements Board {
    public static final int BOARD_ROWS = 25;
    public static final int BOARD_COLUMNS = 10;

    private static final int SPAWN_X = 4;
    private static final int SPAWN_Y = 0;

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    public SimpleBoard() {
        this(BOARD_ROWS, BOARD_COLUMNS);
    }


    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }
    /**
     */
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

        return hasCollision(brickRotator.getCurrentShape(), currentOffset);
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    @Override
    public ViewData getViewData() {
        return new ViewData(brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY(), brickGenerator.getNextBrick().getShapeMatrix().get(0));
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
        spawnNewBrick();
    }
}
