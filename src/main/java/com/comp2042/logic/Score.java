package com.comp2042.logic;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class Score {

    private static final int INITIAL_SCORE = 0;

    private final IntegerProperty score = new SimpleIntegerProperty(INITIAL_SCORE);

    public IntegerProperty scoreProperty() {
        return score;
    }

    public int getValue() {
        return score.get();
    }

    public void add(int points) {
        score.set(score.get() + points);
    }

    public void reset() {
        score.set(INITIAL_SCORE);
    }
}
