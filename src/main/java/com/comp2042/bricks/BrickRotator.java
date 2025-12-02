package com.comp2042.bricks;

import com.comp2042.logic.NextShapeInfo;

public class BrickRotator {

    private static final int INITIAL_ROTATION = 0;

    private Brick brick;
    private int currentRotation = INITIAL_ROTATION;

    public NextShapeInfo getNextShape() {
        int nextRotation = (currentRotation + 1) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextRotation), nextRotation);
    }

    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentRotation);
    }

    public void setCurrentShape(int rotation) {
        this.currentRotation = rotation;
    }

    public void setBrick(Brick brick) {
        this.brick = brick;
        this.currentRotation = INITIAL_ROTATION;
    }
    
    public Brick getBrick() {
        return brick;
    }
}
