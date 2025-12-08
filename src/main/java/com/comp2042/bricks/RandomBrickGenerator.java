package com.comp2042.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Implementation of BrickGenerator that generates random Tetris bricks.
 * Maintains a queue of bricks to ensure fair distribution and provides
 * preview functionality for upcoming bricks.
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 * @see BrickGenerator
 */
public class RandomBrickGenerator implements BrickGenerator {

    private static final int INITIAL_QUEUE_SIZE = 4;

    private final List<Brick> brickTypes = List.of(
            new IBrick(),
            new JBrick(),
            new LBrick(),
            new OBrick(),
            new SBrick(),
            new TBrick(),
            new ZBrick()
    );

    private final Deque<Brick> brickQueue = new ArrayDeque<>();

    /**
     * Creates a new RandomBrickGenerator and initializes the brick queue.
     */
    public RandomBrickGenerator() {
        for (int i = 0; i < INITIAL_QUEUE_SIZE; i++) {
            brickQueue.add(getRandomBrick());
        }
    }

    /**
     * Generates a random brick from the available brick types.
     * 
     * @return a randomly selected brick
     */
    private Brick getRandomBrick() {
        int randomIndex = ThreadLocalRandom.current().nextInt(brickTypes.size());
        return brickTypes.get(randomIndex);
    }

    /**
     * Gets the next brick from the queue and ensures the queue maintains
     * a minimum size. Removes and returns the first brick in the queue.
     * 
     * @return the next brick to be used
     */
    @Override
    public Brick getBrick() {
        while (brickQueue.size() < 4) {
            brickQueue.add(getRandomBrick());
        }
        return brickQueue.poll();
    }

    /**
     * Peeks at the next brick without removing it from the queue.
     * 
     * @return the next brick that will be returned by getBrick(), or {@code null} if queue is empty
     */
    @Override
    public Brick getNextBrick() {
        return brickQueue.peek();
    }
    
    /**
     * Gets a list of the next N bricks without consuming them from the queue.
     * 
     * @param count the number of bricks to preview
     * @return a list of the next N bricks
     */
    @Override
    public List<Brick> getNextBricks(int count) {
        List<Brick> nextBricks = new ArrayList<>();
        Deque<Brick> tempQueue = new ArrayDeque<>(brickQueue);
        
        while (tempQueue.size() < count) {
            tempQueue.add(getRandomBrick());
        }
        
        for (int i = 0; i < count; i++) {
            Brick brick = tempQueue.poll();
            if (brick != null) {
                nextBricks.add(brick);
            }
        }
        
        return nextBricks;
    }
}
