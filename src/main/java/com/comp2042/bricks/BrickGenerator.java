package com.comp2042.bricks;

import java.util.List;

/**
 * Interface for generating Tetris bricks.
 * Provides methods to get the current brick and preview upcoming bricks.
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 * @see RandomBrickGenerator
 */
public interface BrickGenerator {

    /**
     * Gets the next brick from the generator and removes it from the queue.
     * 
     * @return the next brick to spawn
     */
    Brick getBrick();

    /**
     * Peeks at the next brick without removing it from the queue.
     * 
     * @return the next brick that will be spawned
     */
    Brick getNextBrick();
    
    /**
     * Gets a list of the next N bricks without removing them from the queue.
     * 
     * @param count the number of bricks to preview
     * @return a list of the next bricks
     */
    List<Brick> getNextBricks(int count);
}
