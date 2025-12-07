package com.comp2042.logic;

import com.comp2042.ui.HighScoreManager;
import com.comp2042.ui.LevelManager;
import com.comp2042.ui.SettingsManager;
import com.comp2042.ui.ViewData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Game Logic Tests")
class MatrixOperationsTest {

    private int[][] emptyBoard;
    private int[][] filledBoard;
    private SimpleBoard board;
    private Score score;

    @BeforeEach
    void setUp() {
        emptyBoard = new int[10][10];
        filledBoard = new int[10][10];
        filledBoard[5][5] = 1;
        filledBoard[5][6] = 1;
        board = new SimpleBoard();
        score = new Score();
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
    @DisplayName("Level - all three conditions together")
    void testLevelAllConditions() {
        Level level = new Level(1, "Test", "All conditions", 5, 1000, 60, 500);
        assertTrue(level.checkObjective(5, 1000, 60));
        assertTrue(level.checkObjective(6, 1001, 59));
        assertFalse(level.checkObjective(4, 1000, 60));
        assertFalse(level.checkObjective(5, 999, 60));
        assertFalse(level.checkObjective(5, 1000, 61));
    }

    @Test
    @DisplayName("Level - only score target")
    void testLevelOnlyScore() {
        Level level = new Level(1, "Test", "Score only", 0, 5000, 0, 500);
        assertTrue(level.checkObjective(0, 5000, 0));
        assertTrue(level.checkObjective(0, 6000, 0));
        assertFalse(level.checkObjective(0, 4999, 0));
    }

    @Test
    @DisplayName("Level - edge values at target")
    void testLevelEdgeValues() {
        Level level = new Level(1, "Test", "Edge test", 10, 0, 0, 500);
        assertTrue(level.checkObjective(10, 0, 0));
        assertFalse(level.checkObjective(9, 0, 0));
        assertTrue(level.checkObjective(11, 0, 0));
    }

    @Test
    @DisplayName("Level - getters return correct values")
    void testLevelGetters() {
        Level level = new Level(3, "Test Level", "Test objective", 5, 1000, 120, 300);
        assertEquals(3, level.getLevelNumber());
        assertEquals("Test Level", level.getName());
        assertEquals("Test objective", level.getObjective());
        assertEquals(5, level.getTargetLines());
        assertEquals(1000, level.getTargetScore());
        assertEquals(120, level.getTimeLimit());
        assertEquals(300, level.getDropSpeed());
        assertFalse(level.isCompleted());
        level.setCompleted(true);
        assertTrue(level.isCompleted());
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
    @DisplayName("LevelManager - getLevels returns copy")
    void testGetLevelsReturnsCopy() {
        java.util.List<Level> levels1 = LevelManager.getLevels();
        java.util.List<Level> levels2 = LevelManager.getLevels();
        assertNotSame(levels1, levels2);
        assertEquals(levels1.size(), levels2.size());
    }

    @Test
    @DisplayName("LevelManager - multiple level unlock chain")
    void testMultipleLevelUnlockChain() {
        Level level1 = LevelManager.getLevel(1);
        Level level2 = LevelManager.getLevel(2);
        Level level3 = LevelManager.getLevel(3);
        level1.setCompleted(true);
        assertTrue(LevelManager.isLevelUnlocked(2));
        level2.setCompleted(true);
        assertTrue(LevelManager.isLevelUnlocked(3));
        level3.setCompleted(false);
        assertFalse(LevelManager.isLevelUnlocked(3));
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

    @Test
    @DisplayName("MatrixOperations - clear multiple lines")
    void testClearMultipleLines() {
        int[][] board = new int[10][10];
        for (int i = 5; i < 8; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = 1;
            }
        }
        ClearRow result = MatrixOperations.checkRemoving(board);
        assertEquals(3, result.getLinesRemoved());
        assertEquals(450, result.getScoreBonus());
    }

    @Test
    @DisplayName("MatrixOperations - boundary collision detection")
    void testBoundaryCollision() {
        int[][] board = new int[10][10];
        int[][] brick = {{1, 1}};
        assertTrue(MatrixOperations.intersect(board, brick, -1, 5));
        assertTrue(MatrixOperations.intersect(board, brick, 10, 5));
        assertTrue(MatrixOperations.intersect(board, brick, 5, 10));
    }


    @Test
    @DisplayName("SimpleBoard - boundary movement restrictions")
    void testBoundaryMovement() {
        board.spawnNewBrick();
        for (int i = 0; i < 20; i++) {
            board.moveBrickLeft();
        }
        int xPos = board.getViewData().getxPosition();
        assertTrue(xPos >= 0);
        for (int i = 0; i < 20; i++) {
            board.moveBrickRight();
        }
        int maxX = board.getViewData().getxPosition();
        assertTrue(maxX < SimpleBoard.BOARD_COLUMNS);
    }

    @Test
    @DisplayName("Score - multiple line clear scoring")
    void testMultipleLineScore() {
        int[][] board = new int[10][10];
        for (int i = 5; i < 7; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = 1;
            }
        }
        ClearRow result = MatrixOperations.checkRemoving(board);
        assertEquals(2, result.getLinesRemoved());
        assertEquals(200, result.getScoreBonus());
    }

    @Test
    @DisplayName("MatrixOperations - clear 4 lines Tetris")
    void testClearFourLines() {
        int[][] board = new int[10][10];
        for (int i = 5; i < 9; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = 1;
            }
        }
        ClearRow result = MatrixOperations.checkRemoving(board);
        assertEquals(4, result.getLinesRemoved());
        assertEquals(800, result.getScoreBonus());
    }

    @Test
    @DisplayName("MatrixOperations - deep copy list")
    void testDeepCopyList() {
        java.util.List<int[][]> original = new java.util.ArrayList<>();
        original.add(new int[][]{{1, 2}, {3, 4}});
        original.add(new int[][]{{5, 6}, {7, 8}});
        java.util.List<int[][]> copy = MatrixOperations.deepCopyList(original);
        copy.get(0)[0][0] = 99;
        assertEquals(1, original.get(0)[0][0]);
        assertEquals(99, copy.get(0)[0][0]);
    }

    @Test
    @DisplayName("SimpleBoard - ghost Y calculation")
    void testGhostY() {
        board.spawnNewBrick();
        int ghostY = board.getGhostY();
        int currentY = board.getViewData().getyPosition();
        assertTrue(ghostY >= currentY);
    }

    @Test
    @DisplayName("SimpleBoard - rotation collision detection")
    void testRotationCollision() {
        board.spawnNewBrick();
        int[][] matrix = board.getBoardMatrix();
        ViewData viewData = board.getViewData();
        int y = viewData.getyPosition();
        for (int j = 0; j < SimpleBoard.BOARD_COLUMNS; j++) {
            if (y + 1 < matrix.length) {
                matrix[y + 1][j] = 1;
            }
        }
        assertFalse(board.rotateBrick());
    }

    @Test
    @DisplayName("SimpleBoard - newGame resets state")
    void testNewGame() {
        board.spawnNewBrick();
        board.moveBrickDown();
        board.moveBrickRight();
        board.getScore().add(100);
        board.newGame();
        assertEquals(0, board.getScore().getValue());
        ViewData viewData = board.getViewData();
        assertEquals(5, viewData.getxPosition());
        assertEquals(2, viewData.getyPosition());
    }

    @Test
    @DisplayName("SimpleBoard - merge brick to background")
    void testMergeBrickToBackground() {
        board.spawnNewBrick();
        int[][] matrixBefore = MatrixOperations.copy(board.getBoardMatrix());
        board.mergeBrickToBackground();
        int[][] matrixAfter = board.getBoardMatrix();
        boolean changed = false;
        for (int i = 0; i < matrixBefore.length; i++) {
            for (int j = 0; j < matrixBefore[i].length; j++) {
                if (matrixBefore[i][j] != matrixAfter[i][j]) {
                    changed = true;
                    break;
                }
            }
        }
        assertTrue(changed);
    }

    @Test
    @DisplayName("MatrixOperations - merge empty brick")
    void testMergeEmptyBrick() {
        int[][] board = new int[10][10];
        int[][] emptyBrick = {{0, 0}, {0, 0}};
        int[][] result = MatrixOperations.merge(board, emptyBrick, 5, 5);
        for (int i = 0; i < board.length; i++) {
            assertArrayEquals(board[i], result[i]);
        }
    }

    @Test
    @DisplayName("Score - large score values")
    void testLargeScore() {
        score.add(1000000);
        assertEquals(1000000, score.getValue());
        score.add(500000);
        assertEquals(1500000, score.getValue());
    }

    @Test
    @DisplayName("ViewData - immutability")
    void testViewDataImmutability() {
        board.spawnNewBrick();
        ViewData viewData = board.getViewData();
        int[][] brickData1 = viewData.getBrickData();
        int[][] brickData2 = viewData.getBrickData();
        assertNotSame(brickData1, brickData2);
        brickData1[0][0] = 99;
        int[][] brickData3 = viewData.getBrickData();
        assertNotEquals(99, brickData3[0][0]);
    }

    @Test
    @DisplayName("NextShapeInfo - immutability")
    void testNextShapeInfoImmutability() {
        com.comp2042.bricks.BrickRotator rotator = new com.comp2042.bricks.BrickRotator();
        rotator.setBrick(new com.comp2042.bricks.RandomBrickGenerator().getBrick());
        com.comp2042.logic.NextShapeInfo shapeInfo = rotator.getNextShape();
        int[][] shape1 = shapeInfo.getShape();
        int[][] shape2 = shapeInfo.getShape();
        assertNotSame(shape1, shape2);
        if (shape1.length > 0 && shape1[0].length > 0) {
            shape1[0][0] = 99;
            int[][] shape3 = shapeInfo.getShape();
            assertNotEquals(99, shape3[0][0]);
        }
    }

    @Test
    @DisplayName("MatrixOperations - merge at board edges")
    void testMergeAtEdges() {
        int[][] board = new int[10][10];
        int[][] brick = {{1}};
        int[][] result1 = MatrixOperations.merge(board, brick, 0, 0);
        assertEquals(1, result1[0][0]);
        int[][] result2 = MatrixOperations.merge(board, brick, 9, 9);
        assertEquals(1, result2[9][9]);
    }

    @Test
    @DisplayName("SimpleBoard - multiple consecutive merges")
    void testMultipleMerges() {
        board.spawnNewBrick();
        board.mergeBrickToBackground();
        int[][] afterFirst = MatrixOperations.copy(board.getBoardMatrix());
        board.spawnNewBrick();
        board.mergeBrickToBackground();
        int[][] afterSecond = board.getBoardMatrix();
        boolean different = false;
        for (int i = 0; i < afterFirst.length; i++) {
            for (int j = 0; j < afterFirst[i].length; j++) {
                if (afterFirst[i][j] != afterSecond[i][j]) {
                    different = true;
                    break;
                }
            }
        }
        assertTrue(different);
    }

    @Test
    @DisplayName("SimpleBoard - clear multiple rows consecutively")
    void testClearMultipleRowsConsecutively() {
        int[][] matrix = board.getBoardMatrix();
        for (int i = SimpleBoard.BOARD_ROWS - 1; i >= SimpleBoard.BOARD_ROWS - 3; i--) {
            for (int j = 0; j < SimpleBoard.BOARD_COLUMNS; j++) {
                matrix[i][j] = 1;
            }
        }
        ClearRow result = board.clearRows();
        assertEquals(3, result.getLinesRemoved());
        assertEquals(450, result.getScoreBonus());
    }

    @Test
    @DisplayName("Score - reset after adding")
    void testScoreReset() {
        score.add(100);
        score.addLines(5);
        score.reset();
        assertEquals(0, score.getValue());
        assertEquals(0, score.linesProperty().get());
    }

    @Test
    @DisplayName("SimpleBoard - hard drop with obstacles")
    void testHardDropWithObstacles() {
        board.spawnNewBrick();
        int[][] matrix = board.getBoardMatrix();
        for (int j = 0; j < SimpleBoard.BOARD_COLUMNS; j++) {
            matrix[SimpleBoard.BOARD_ROWS - 5][j] = 1;
        }
        int distance = board.hardDrop();
        assertTrue(distance > 0);
        assertTrue(distance < SimpleBoard.BOARD_ROWS);
    }

    @Test
    @DisplayName("MatrixOperations - merge with overlapping blocks")
    void testMergeWithOverlap() {
        int[][] board = new int[10][10];
        board[5][5] = 2;
        int[][] brick = {{1}};
        int[][] result = MatrixOperations.merge(board, brick, 5, 5);
        assertEquals(1, result[5][5]);
    }

    @Test
    @DisplayName("SimpleBoard - getGhostY with obstacles")
    void testGhostYWithObstacles() {
        board.spawnNewBrick();
        int[][] matrix = board.getBoardMatrix();
        for (int j = 0; j < SimpleBoard.BOARD_COLUMNS; j++) {
            matrix[SimpleBoard.BOARD_ROWS - 3][j] = 1;
        }
        int ghostY = board.getGhostY();
        ViewData viewData = board.getViewData();
        assertTrue(ghostY >= viewData.getyPosition());
        assertTrue(ghostY <= SimpleBoard.BOARD_ROWS - 3);
    }

    @Test
    @DisplayName("Integration - full game cycle")
    void testFullGameCycle() {
        board.spawnNewBrick();
        ViewData initial = board.getViewData();
        assertNotNull(initial.getBrickData());
        board.moveBrickDown();
        board.moveBrickRight();
        board.rotateBrick();
        board.holdBrick();
        board.spawnNewBrick();
        while (board.moveBrickDown()) {
        }
        board.mergeBrickToBackground();
        ClearRow result = board.clearRows();
        assertNotNull(result);
        board.spawnNewBrick();
        ViewData afterCycle = board.getViewData();
        assertNotNull(afterCycle.getBrickData());
    }

    @Test
    @DisplayName("Integration - multiple bricks and clears")
    void testMultipleBricksAndClears() {
        int totalCleared = 0;
        for (int i = 0; i < 3; i++) {
            board.spawnNewBrick();
            while (board.moveBrickDown()) {
            }
            board.mergeBrickToBackground();
            int[][] matrix = board.getBoardMatrix();
            for (int j = 0; j < SimpleBoard.BOARD_COLUMNS; j++) {
                matrix[SimpleBoard.BOARD_ROWS - 1][j] = 1;
            }
            ClearRow result = board.clearRows();
            totalCleared += result.getLinesRemoved();
        }
        assertTrue(totalCleared >= 0);
    }

    @Test
    @DisplayName("Integration - score accumulation")
    void testScoreAccumulation() {
        board.spawnNewBrick();
        int initialScore = board.getScore().getValue();
        board.getScore().add(50);
        board.mergeBrickToBackground();
        int[][] matrix = board.getBoardMatrix();
        for (int j = 0; j < SimpleBoard.BOARD_COLUMNS; j++) {
            matrix[SimpleBoard.BOARD_ROWS - 1][j] = 1;
        }
        ClearRow result = board.clearRows();
        board.getScore().add(result.getScoreBonus());
        assertTrue(board.getScore().getValue() > initialScore);
    }

    @Test
    @DisplayName("DownData - getters return correct values")
    void testDownDataGetters() {
        board.spawnNewBrick();
        ViewData viewData = board.getViewData();
        ClearRow clearRow = new ClearRow(2, new int[10][10], 200);
        DownData downData = new DownData(clearRow, viewData);
        assertEquals(clearRow, downData.getClearRow());
        assertEquals(viewData, downData.getViewData());
    }

    @Test
    @DisplayName("MoveEvent - getters return correct values")
    void testMoveEventGetters() {
        MoveEvent event = new MoveEvent(EventType.ROTATE, EventSource.USER);
        assertEquals(EventType.ROTATE, event.getEventType());
        assertEquals(EventSource.USER, event.getEventSource());
    }

    @Test
    @DisplayName("SimpleBoard - custom board size constructor")
    void testCustomBoardSize() {
        SimpleBoard customBoard = new SimpleBoard(20, 10);
        assertEquals(20, customBoard.getBoardMatrix().length);
        assertEquals(10, customBoard.getBoardMatrix()[0].length);
    }

    @Test
    @DisplayName("SimpleBoard - getHeldBrick returns held brick")
    void testGetHeldBrick() {
        board.spawnNewBrick();
        board.holdBrick();
        assertNotNull(board.getHeldBrick());
    }

    @Test
    @DisplayName("MatrixOperations - copy handles irregular matrix sizes")
    void testCopyIrregularMatrix() {
        int[][] irregular = new int[5][];
        irregular[0] = new int[3];
        irregular[1] = new int[5];
        irregular[2] = new int[2];
        irregular[3] = new int[4];
        irregular[4] = new int[1];
        irregular[1][2] = 42;
        int[][] copy = MatrixOperations.copy(irregular);
        assertEquals(42, copy[1][2]);
        assertNotSame(irregular, copy);
    }

    @Test
    @DisplayName("SimpleBoard - newGame resets held brick")
    void testNewGameResetsHeldBrick() {
        board.spawnNewBrick();
        board.holdBrick();
        assertNotNull(board.getHeldBrick());
        board.newGame();
        assertNull(board.getHeldBrick());
    }

    @Test
    @DisplayName("MatrixOperations - intersect with zero-sized brick")
    void testIntersectZeroSizedBrick() {
        int[][] emptyBrick = {{}};
        assertFalse(MatrixOperations.intersect(emptyBoard, emptyBrick, 5, 5));
    }

    @Test
    @DisplayName("MatrixOperations - merge preserves non-zero values")
    void testMergePreservesNonZero() {
        int[][] board = new int[10][10];
        board[5][5] = 3;
        int[][] brick = {{2}};
        int[][] result = MatrixOperations.merge(board, brick, 5, 5);
        assertEquals(2, result[5][5]);
    }

    @Test
    @DisplayName("SimpleBoard - getBoardMatrix returns reference")
    void testGetBoardMatrixReturnsReference() {
        int[][] matrix1 = board.getBoardMatrix();
        int[][] matrix2 = board.getBoardMatrix();
        assertSame(matrix1, matrix2);
    }

    @Test
    @DisplayName("SimpleBoard - move fails at left boundary")
    void testMoveFailsAtLeftBoundary() {
        board.spawnNewBrick();
        for (int i = 0; i < 20; i++) {
            board.moveBrickLeft();
        }
        assertFalse(board.moveBrickLeft());
    }

    @Test
    @DisplayName("SimpleBoard - move fails at right boundary")
    void testMoveFailsAtRightBoundary() {
        board.spawnNewBrick();
        for (int i = 0; i < 20; i++) {
            board.moveBrickRight();
        }
        assertFalse(board.moveBrickRight());
    }

    @Test
    @DisplayName("Score - addLines with zero")
    void testAddLinesZero() {
        score.addLines(0);
        assertEquals(0, score.linesProperty().get());
    }

    @Test
    @DisplayName("Level - checkObjective with all zeros")
    void testLevelObjectiveAllZeros() {
        Level level = new Level(1, "Test", "No requirements", 0, 0, 0, 500);
        assertTrue(level.checkObjective(0, 0, 0));
    }
}
