package com.comp2042.logic;

public record NextShapeInfo(int[][] shape, int position) {

    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    public int getPosition() {
        return position;
    }
}
