package com.comp2042.bricks;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomBrickGenerator implements BrickGenerator {

    private static final int INITIAL_QUEUE_SIZE = 2;

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

    public RandomBrickGenerator() {
        for (int i = 0; i < INITIAL_QUEUE_SIZE; i++) {
            brickQueue.add(getRandomBrick());
        }
    }

    private Brick getRandomBrick() {
        int randomIndex = ThreadLocalRandom.current().nextInt(brickTypes.size());
        return brickTypes.get(randomIndex);
    }

    @Override
    public Brick getBrick() {
        if (brickQueue.size() <= 1) {
            brickQueue.add(getRandomBrick());
        }
        return brickQueue.poll();
    }

    @Override
    public Brick getNextBrick() {
        return brickQueue.peek();
    }
}
