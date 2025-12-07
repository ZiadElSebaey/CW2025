package com.comp2042.logic;

import com.comp2042.bricks.Brick;
import com.comp2042.bricks.BrickRotator;
import com.comp2042.bricks.RandomBrickGenerator;
import com.comp2042.ui.HighScoreManager;
import com.comp2042.ui.LevelManager;
import com.comp2042.ui.SettingsManager;
import com.comp2042.ui.ViewData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Game Logic Tests")
class MatrixOperationsTest {

    private int[][] emptyBoard;
    private int[][] filledBoard;
    private SimpleBoard board;
    private Score score;
    private RandomBrickGenerator generator;
    private BrickRotator rotator;

    @BeforeEach
    void setUp() {
        emptyBoard = new int[10][10];
        filledBoard = new int[10][10];
        filledBoard[5][5] = 1;
        filledBoard[5][6] = 1;
        board = new SimpleBoard();
        score = new Score();
        generator = new RandomBrickGenerator();
        rotator = new BrickRotator();
    }

    @Test
    @DisplayName("MatrixOperations - intersect detects collision")
    void testIntersect() {
        int[][] brick = {{1, 1}, {1, 1}};
        assertTrue(MatrixOperations.intersect(filledBoard, brick, 5, 5));
        assertFalse(MatrixOperations.intersect(emptyBoard, brick, 5, 5));
    }

    @Test
    @DisplayName("MatrixOperations - merge brick correctly")
    void testMerge() {
        int[][] brick = {{1, 1}, {1, 1}};
        int[][] result = MatrixOperations.merge(emptyBoard, brick, 5, 5);
        assertEquals(1, result[5][5]);
        assertEquals(1, result[5][6]);
    }

    @Test
    @DisplayName("MatrixOperations - clear lines and calculate score")
    void testCheckRemoving() {
        int[][] board = new int[10][10];
        for (int j = 0; j < 10; j++) {
            board[5][j] = 1;
        }
        ClearRow result = MatrixOperations.checkRemoving(board);
        assertEquals(1, result.getLinesRemoved());
        assertEquals(50, result.getScoreBonus());
    }

    @Test
    @DisplayName("MatrixOperations - deep copy matrix")
    void testCopy() {
        int[][] original = new int[5][5];
        original[2][2] = 42;
        int[][] copy = MatrixOperations.copy(original);
        copy[2][2] = 99;
        assertEquals(42, original[2][2]);
        assertEquals(99, copy[2][2]);
    }

    @Test
    @DisplayName("Score - add points and lines")
    void testScore() {
        score.add(50);
        assertEquals(50, score.getValue());
        score.addLines(2);
        assertEquals(2, score.linesProperty().get());
        score.reset();
        assertEquals(0, score.getValue());
    }

    @Test
    @DisplayName("SimpleBoard - spawn and move brick")
    void testSimpleBoard() {
        assertFalse(board.spawnNewBrick());
        ViewData viewData = board.getViewData();
        assertNotNull(viewData.getBrickData());
        int initialY = viewData.getyPosition();
        assertTrue(board.moveBrickDown());
        assertEquals(initialY + 1, board.getViewData().getyPosition());
    }

    @Test
    @DisplayName("SimpleBoard - clear rows")
    void testClearRows() {
        int[][] matrix = board.getBoardMatrix();
        for (int j = 0; j < SimpleBoard.BOARD_COLUMNS; j++) {
            matrix[SimpleBoard.BOARD_ROWS - 1][j] = 1;
        }
        ClearRow result = board.clearRows();
        assertEquals(1, result.getLinesRemoved());
        assertEquals(50, result.getScoreBonus());
    }

    @Test
    @DisplayName("SimpleBoard - move left and right")
    void testMoveLeftRight() {
        board.spawnNewBrick();
        int initialX = board.getViewData().getxPosition();
        assertTrue(board.moveBrickRight());
        assertEquals(initialX + 1, board.getViewData().getxPosition());
        assertTrue(board.moveBrickLeft());
        assertEquals(initialX, board.getViewData().getxPosition());
    }

    @Test
    @DisplayName("SimpleBoard - hard drop calculates distance")
    void testHardDrop() {
        board.spawnNewBrick();
        int distance = board.hardDrop();
        assertTrue(distance > 0);
    }

    @Test
    @DisplayName("SimpleBoard - game over when spawn fails")
    void testGameOver() {
        int[][] matrix = board.getBoardMatrix();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < SimpleBoard.BOARD_COLUMNS; j++) {
                matrix[i][j] = 1;
            }
        }
        assertTrue(board.spawnNewBrick());
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
    @DisplayName("Level - check objective conditions")
    void testLevelObjective() {
        Level level = new Level(1, "Test", "Clear 5 lines", 5, 0, 0, 500);
        assertTrue(level.checkObjective(5, 0, 0));
        assertFalse(level.checkObjective(4, 0, 0));
        Level timeLevel = new Level(2, "Test", "Time limit", 0, 0, 60, 400);
        assertTrue(timeLevel.checkObjective(0, 0, 50));
        assertFalse(timeLevel.checkObjective(0, 0, 70));
    }

    @Test
    @DisplayName("LevelManager - get level by number")
    void testLevelManager() {
        Level level1 = LevelManager.getLevel(1);
        assertNotNull(level1);
        assertEquals(1, level1.getLevelNumber());
        assertNull(LevelManager.getLevel(0));
        assertNull(LevelManager.getLevel(100));
    }

    @Test
    @DisplayName("LevelManager - level unlock logic")
    void testLevelUnlock() {
        assertTrue(LevelManager.isLevelUnlocked(1));
        Level level1 = LevelManager.getLevel(1);
        level1.setCompleted(true);
        assertTrue(LevelManager.isLevelUnlocked(2));
    }

    @Test
    @DisplayName("HighScoreManager - update high score")
    void testHighScoreManager() {
        HighScoreManager.resetHighScore();
        assertTrue(HighScoreManager.updateHighScore(100, "Player1"));
        assertEquals(100, HighScoreManager.getHighScore());
        assertEquals("Player1", HighScoreManager.getHighScoreHolder());
        assertFalse(HighScoreManager.updateHighScore(50, "Player2"));
        assertTrue(HighScoreManager.updateHighScore(200, "Player2"));
        assertEquals(200, HighScoreManager.getHighScore());
    }

    @Test
    @DisplayName("SettingsManager - ghost block setting")
    void testSettingsManager() {
        boolean original = SettingsManager.isGhostBlockEnabled();
        SettingsManager.setGhostBlockEnabled(!original);
        assertEquals(!original, SettingsManager.isGhostBlockEnabled());
        SettingsManager.setGhostBlockEnabled(original);
    }

    @Test
    @DisplayName("ClearRow - getters return correct values")
    void testClearRow() {
        int[][] matrix = new int[10][10];
        ClearRow clearRow = new ClearRow(2, matrix, 100);
        assertEquals(2, clearRow.getLinesRemoved());
        assertEquals(100, clearRow.getScoreBonus());
        assertNotNull(clearRow.getNewMatrix());
    }
}
