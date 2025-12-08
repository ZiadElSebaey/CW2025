package com.comp2042.bricks;

import com.comp2042.logic.NextShapeInfo;

/**
 * Manages brick rotation state and provides rotation functionality.
 * Tracks the current rotation index and provides access to rotated shapes.
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 */
public class BrickRotator {

    private static final int INITIAL_ROTATION = 0;

    private Brick brick;
    private int currentRotation = INITIAL_ROTATION;

    /**
     * Gets information about the next rotation state.
     * 
     * @return a NextShapeInfo object containing the next shape and its rotation index
     */
    public NextShapeInfo getNextShape() {
        int nextRotation = (currentRotation + 1) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextRotation), nextRotation);
    }

    /**
     * Gets the current shape matrix for the brick in its current rotation.
     * 
     * @return the 2D array representing the current brick shape
     */
    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentRotation);
    }

    /**
     * Sets the current rotation index.
     * 
     * @param rotation the rotation index to set
     */
    public void setCurrentShape(int rotation) {
        this.currentRotation = rotation;
    }

    /**
     * Sets a new brick and resets rotation to initial state.
     * 
     * @param brick the brick to set
     */
    public void setBrick(Brick brick) {
        this.brick = brick;
        this.currentRotation = INITIAL_ROTATION;
    }
    
    /**
     * Gets the current brick.
     * 
     * @return the current brick
     */
    public Brick getBrick() {
        return brick;
    }
}
