package com.comp2042.logic;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Manages the game score and cleared lines count.
 * Provides JavaFX property bindings for reactive UI updates.
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 */
public final class Score {

    private static final int INITIAL_SCORE = 0;

    private final IntegerProperty score = new SimpleIntegerProperty(INITIAL_SCORE);
    private final IntegerProperty lines = new SimpleIntegerProperty(0);

    /**
     * Returns the score property for JavaFX binding.
     * 
     * @return the score property
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Returns the lines property for JavaFX binding.
     * 
     * @return the lines property
     */
    public IntegerProperty linesProperty() {
        return lines;
    }

    /**
     * Gets the current score value.
     * 
     * @return the current score
     */
    public int getValue() {
        return score.get();
    }

    /**
     * Adds points to the current score.
     * 
     * @param points the points to add
     */
    public void add(int points) {
        score.set(score.get() + points);
    }

    /**
     * Adds cleared lines to the line count.
     * 
     * @param count the number of lines cleared
     */
    public void addLines(int count) {
        lines.set(lines.get() + count);
    }

    /**
     * Resets the score and lines count to initial values.
     */
    public void reset() {
        score.set(INITIAL_SCORE);
        lines.set(0);
    }
}
