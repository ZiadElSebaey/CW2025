package com.comp2042.bricks;

import java.util.List;

/**
 * Interface representing a Tetris brick (tetromino).
 * Each brick has multiple rotation states defined as a list of 2D matrices.
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 */
public interface Brick {

    /**
     * Gets all rotation states of this brick as a list of 2D matrices.
     * Each matrix represents the brick in a different rotation.
     * 
     * @return a list of 2D integer arrays representing all rotation states
     */
    List<int[][]> getShapeMatrix();
}
