package com.comp2042.bricks;

import com.comp2042.logic.SimpleBoard;
import com.comp2042.ui.ViewData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Brick Tests")
class BrickTest {

    private SimpleBoard board;
    private RandomBrickGenerator generator;
    private BrickRotator rotator;

    @BeforeEach
    void setUp() {
        board = new SimpleBoard();
        generator = new RandomBrickGenerator();
        rotator = new BrickRotator();
    }

    @Test
    @DisplayName("RandomBrickGenerator - generate bricks")
    void testBrickGenerator() {
        Brick brick = generator.getBrick();
        assertNotNull(brick);
        assertNotNull(brick.getShapeMatrix());
        List<Brick> nextBricks = generator.getNextBricks(3);
        assertEquals(3, nextBricks.size());
    }

    @Test
    @DisplayName("BrickRotator - rotate brick")
    void testBrickRotator() {
        Brick brick = generator.getBrick();
        rotator.setBrick(brick);
        assertNotNull(rotator.getCurrentShape());
        assertNotNull(rotator.getNextShape());
    }

    @Test
    @DisplayName("SimpleBoard - rotate brick")
    void testRotateBrick() {
        board.spawnNewBrick();
        assertTrue(board.rotateBrick());
        int[][] rotatedShape = board.getViewData().getBrickData();
        assertNotNull(rotatedShape);
    }

    @Test
    @DisplayName("SimpleBoard - hold brick functionality")
    void testHoldBrick() {
        board.spawnNewBrick();
        ViewData result = board.holdBrick();
        assertNotNull(result.getHoldBrickData());
        board.spawnNewBrick();
        ViewData afterHold = board.holdBrick();
        assertNotNull(afterHold.getHoldBrickData());
    }
}

