package com.comp2042.logic;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for matrix operations used in the Tetris game.
 * Provides static methods for collision detection, matrix manipulation,
 * and row clearing logic.
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 */
public class MatrixOperations {

    private MatrixOperations() {
    }

    /**
     * Checks if a brick intersects with the board matrix at the given position.
     * 
     * @param matrix the board matrix
     * @param brick the brick shape matrix
     * @param x the x coordinate of the brick's top-left corner
     * @param y the y coordinate of the brick's top-left corner
     * @return {@code true} if there is a collision, {@code false} otherwise
     */
    public static boolean intersect(final int[][] matrix, final int[][] brick, int x, int y) {
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;
                if (brick[j][i] != 0 && (isOutOfBounds(matrix, targetX, targetY) || matrix[targetY][targetX] != 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the given coordinates are out of bounds for the matrix.
     * 
     * @param matrix the board matrix
     * @param targetX the x coordinate to check
     * @param targetY the y coordinate to check
     * @return {@code true} if out of bounds, {@code false} otherwise
     */
    private static boolean isOutOfBounds(int[][] matrix, int targetX, int targetY) {
        return targetX < 0 || targetY >= matrix.length || targetX >= matrix[targetY].length;
    }

    /**
     * Creates a deep copy of a 2D integer array.
     * 
     * @param original the original array to copy
     * @return a new array with copied contents
     */
    public static int[][] copy(int[][] original) {
        int[][] result = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            int[] row = original[i];
            result[i] = new int[row.length];
            System.arraycopy(row, 0, result[i], 0, row.length);
        }
        return result;
    }

    /**
     * Merges a brick into the board matrix at the specified position.
     * 
     * @param filledFields the current board matrix
     * @param brick the brick shape to merge
     * @param x the x coordinate where to place the brick
     * @param y the y coordinate where to place the brick
     * @return a new matrix with the brick merged in
     */
    public static int[][] merge(int[][] filledFields, int[][] brick, int x, int y) {
        int[][] copy = copy(filledFields);
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;
                if (brick[j][i] != 0) {
                    copy[targetY][targetX] = brick[j][i];
                }
            }
        }
        return copy;
    }

    /**
     * Checks for completed rows and creates a new matrix with cleared rows removed.
     * Calculates score bonus based on the number of rows cleared.
     * 
     * @param matrix the board matrix to check
     * @return a ClearRow object containing the number of lines removed,
     *         the new matrix, and the score bonus
     */
    public static ClearRow checkRemoving(final int[][] matrix) {
        int[][] tmp = new int[matrix.length][matrix[0].length];
        Deque<int[]> newRows = new ArrayDeque<>();
        List<Integer> clearedRows = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            int[] tmpRow = new int[matrix[i].length];
            boolean rowToClear = true;
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    rowToClear = false;
                }
                tmpRow[j] = matrix[i][j];
            }
            if (rowToClear) {
                clearedRows.add(i);
            } else {
                newRows.add(tmpRow);
            }
        }
        for (int i = matrix.length - 1; i >= 0; i--) {
            int[] row = newRows.pollLast();
            if (row != null) {
                tmp[i] = row;
            } else {
                break;
            }
        }
        int scoreBonus = 50 * clearedRows.size() * clearedRows.size();
        return new ClearRow(clearedRows.size(), tmp, scoreBonus);
    }

    /**
     * Creates a deep copy of a list of 2D integer arrays.
     * 
     * @param list the list to copy
     * @return a new list containing deep copies of all arrays
     */
    public static List<int[][]> deepCopyList(List<int[][]> list){
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }

}
