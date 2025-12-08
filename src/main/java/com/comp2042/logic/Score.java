package com.comp2042.logic;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Represents the game score and lines cleared.
 * Uses JavaFX properties for reactive UI binding.
 * 
 * @author TetrisJFX Team
 */
public final class Score {

    private static final int INITIAL_SCORE = 0;

    private final IntegerProperty score = new SimpleIntegerProperty(INITIAL_SCORE);
    private final IntegerProperty lines = new SimpleIntegerProperty(0);

    /**
     * Gets the score property for UI binding.
     * 
     * @return The IntegerProperty representing the current score
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Gets the lines property for UI binding.
     * 
     * @return The IntegerProperty representing the number of lines cleared
     */
    public IntegerProperty linesProperty() {
        return lines;
    }

    /**
     * Gets the current score value.
     * 
     * @return The current score
     */
    public int getValue() {
        return score.get();
    }

    /**
     * Adds points to the current score.
     * 
     * @param points The points to add
     */
    public void add(int points) {
        score.set(score.get() + points);
    }

    /**
     * Adds to the count of lines cleared.
     * 
     * @param count The number of lines to add
     */
    public void addLines(int count) {
        lines.set(lines.get() + count);
    }

    /**
     * Resets the score and lines cleared to zero.
     */
    public void reset() {
        score.set(INITIAL_SCORE);
        lines.set(0);
    }
}
