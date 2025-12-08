package com.comp2042.logic;

/**
 * Represents the result of clearing rows from the game board.
 * Contains information about the number of lines removed, the new board state,
 * and the score bonus awarded.
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 */
public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;

    /**
     * Creates a new ClearRow object.
     * 
     * @param linesRemoved the number of lines that were cleared
     * @param newMatrix the new board matrix after clearing rows
     * @param scoreBonus the score bonus for clearing these lines
     */
    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
    }

    /**
     * Gets the number of lines that were removed.
     * 
     * @return the number of lines removed
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }

    /**
     * Gets a copy of the new board matrix after clearing rows.
     * 
     * @return a copy of the new board matrix
     */
    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    /**
     * Gets the score bonus awarded for clearing these lines.
     * 
     * @return the score bonus
     */
    public int getScoreBonus() {
        return scoreBonus;
    }
}
