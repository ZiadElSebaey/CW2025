package com.comp2042.logic;

/**
 * Immutable record containing information about the next brick rotation state.
 * Used to preview and apply brick rotations.
 * 
 * @param shape the shape matrix of the next rotation
 * @param position the rotation index of the next shape
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 */
public record NextShapeInfo(int[][] shape, int position) {

    /**
     * Gets a copy of the shape matrix.
     * 
     * @return a deep copy of the shape matrix
     */
    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    /**
     * Gets the rotation position index.
     * 
     * @return the rotation position
     */
    public int getPosition() {
        return position;
    }
}
