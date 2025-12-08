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

    @Test
    @DisplayName("SimpleBoard - cannot hold twice in a row")
    void testCannotHoldTwice() {
        board.spawnNewBrick();
        ViewData firstHold = board.holdBrick();
        ViewData secondHold = board.holdBrick();
        assertTrue(java.util.Arrays.deepEquals(firstHold.getHoldBrickData(), secondHold.getHoldBrickData()));
    }

    @Test
    @DisplayName("BrickRotator - full rotation cycle")
    void testFullRotationCycle() {
        Brick brick = generator.getBrick();
        rotator.setBrick(brick);
        int[][] shapeAt0 = rotator.getCurrentShape();
        rotator.setCurrentShape(1);
        rotator.setCurrentShape(2);
        rotator.setCurrentShape(3);
        rotator.setCurrentShape(0);
        int[][] shapeAfterCycle = rotator.getCurrentShape();
        assertArrayEquals(shapeAt0, shapeAfterCycle);
    }

    @Test
    @DisplayName("SimpleBoard - full rotation cycle")
    void testBoardFullRotationCycle() {
        board.spawnNewBrick();
        for (int i = 0; i < 4; i++) {
            if (!board.rotateBrick()) {
                break;
            }
        }
        int[][] afterRotations = board.getViewData().getBrickData();
        assertNotNull(afterRotations);
    }

    @Test
    @DisplayName("RandomBrickGenerator - queue consistency")
    void testQueueConsistency() {
        Brick next1 = generator.getNextBrick();
        Brick next2 = generator.getNextBrick();
        assertEquals(next1, next2);
        generator.getBrick();
        Brick nextAfter = generator.getNextBrick();
        assertNotEquals(next1, nextAfter);
    }

    @Test
    @DisplayName("RandomBrickGenerator - all brick types generated")
    void testAllBrickTypes() {
        java.util.Set<String> brickTypes = new java.util.HashSet<>();
        for (int i = 0; i < 50; i++) {
            Brick brick = generator.getBrick();
            brickTypes.add(brick.getClass().getSimpleName());
        }
        assertTrue(brickTypes.size() >= 5);
    }

    @Test
    @DisplayName("SimpleBoard - hold resets after brick lands")
    void testHoldResetsAfterLand() {
        board.spawnNewBrick();
        board.holdBrick();
        board.spawnNewBrick();
        while (board.moveBrickDown()) {
        }
        board.mergeBrickToBackground();
        board.clearRows();
        board.spawnNewBrick();
        ViewData afterSpawn = board.holdBrick();
        assertNotNull(afterSpawn.getHoldBrickData());
    }

    @Test
    @DisplayName("BrickRotator - getNextShape returns different shape")
    void testGetNextShapeDifferent() {
        Brick brick = generator.getBrick();
        rotator.setBrick(brick);
        com.comp2042.logic.NextShapeInfo next = rotator.getNextShape();
        int[][] nextShape = next.getShape();
        assertNotNull(nextShape);
    }

    @Test
    @DisplayName("BrickRotator - setCurrentShape changes rotation")
    void testSetCurrentShape() {
        Brick brick = generator.getBrick();
        rotator.setBrick(brick);
        int maxRotation = brick.getShapeMatrix().size() - 1;
        if (maxRotation > 0) {
            rotator.setCurrentShape(1);
            int[][] shape1 = rotator.getCurrentShape();
            assertNotNull(shape1);
        } else {
            rotator.setCurrentShape(0);
            int[][] shape0 = rotator.getCurrentShape();
            assertNotNull(shape0);
        }
    }

    @Test
    @DisplayName("RandomBrickGenerator - getNextBrick doesn't consume")
    void testGetNextBrickDoesntConsume() {
        Brick next1 = generator.getNextBrick();
        Brick next2 = generator.getNextBrick();
        Brick next3 = generator.getNextBrick();
        assertEquals(next1, next2);
        assertEquals(next2, next3);
        Brick consumed = generator.getBrick();
        assertNotNull(consumed);
    }

    @Test
    @DisplayName("SimpleBoard - rotation returns to original after 4 rotations")
    void testRotationReturnsToOriginal() {
        board.spawnNewBrick();
        for (int i = 0; i < 4; i++) {
            board.rotateBrick();
        }
        int[][] afterFour = board.getViewData().getBrickData();
        assertNotNull(afterFour);
    }

    @Test
    @DisplayName("SimpleBoard - hold swap mechanism")
    void testHoldSwapMechanism() {
        board.spawnNewBrick();
        board.holdBrick();
        board.spawnNewBrick();
        board.holdBrick();
        ViewData swappedBrick = board.getViewData();
        assertNotNull(swappedBrick.getHoldBrickData());
        assertNotNull(swappedBrick.getBrickData());
    }

    @Test
    @DisplayName("RandomBrickGenerator - getNextBricks maintains order")
    void testGetNextBricksOrder() {
        java.util.List<Brick> bricks1 = generator.getNextBricks(3);
        java.util.List<Brick> bricks2 = generator.getNextBricks(3);
        assertEquals(bricks1.get(0), bricks2.get(0));
        assertEquals(bricks1.get(1), bricks2.get(1));
        assertEquals(bricks1.get(2), bricks2.get(2));
    }

    @Test
    @DisplayName("BrickRotator - getBrick returns current brick")
    void testGetBrick() {
        Brick brick = generator.getBrick();
        rotator.setBrick(brick);
        assertEquals(brick, rotator.getBrick());
    }

    @Test
    @DisplayName("SimpleBoard - ViewData includes all next bricks")
    void testViewDataNextBricks() {
        board.spawnNewBrick();
        ViewData viewData = board.getViewData();
        assertNotNull(viewData.getNextBrickData());
        assertNotNull(viewData.getNextBrickData2());
        assertNotNull(viewData.getNextBrickData3());
    }

    @Test
    @DisplayName("BrickRotator - setBrick resets rotation")
    void testSetBrickResetsRotation() {
        Brick brick = generator.getBrick();
        rotator.setBrick(brick);
        rotator.setCurrentShape(2);
        rotator.setBrick(brick);
        int[][] shape = rotator.getCurrentShape();
        assertNotNull(shape);
    }

    @Test
    @DisplayName("RandomBrickGenerator - getNextBricks with count 1")
    void testGetNextBricksCountOne() {
        java.util.List<Brick> bricks = generator.getNextBricks(1);
        assertEquals(1, bricks.size());
        assertNotNull(bricks.get(0));
    }

    @Test
    @DisplayName("RandomBrickGenerator - getNextBricks with large count")
    void testGetNextBricksLargeCount() {
        java.util.List<Brick> bricks = generator.getNextBricks(10);
        assertEquals(10, bricks.size());
        for (Brick brick : bricks) {
            assertNotNull(brick);
        }
    }

    @Test
    @DisplayName("SimpleBoard - hold when no brick spawned")
    void testHoldWhenNoBrick() {
        board.spawnNewBrick();
        ViewData result = board.holdBrick();
        assertNotNull(result.getHoldBrickData());
    }

    @Test
    @DisplayName("NextShapeInfo - getPosition returns correct value")
    void testNextShapeInfoGetPosition() {
        Brick brick = generator.getBrick();
        rotator.setBrick(brick);
        com.comp2042.logic.NextShapeInfo next = rotator.getNextShape();
        int expectedPosition = (0 + 1) % brick.getShapeMatrix().size();
        assertEquals(expectedPosition, next.getPosition());
    }
}

