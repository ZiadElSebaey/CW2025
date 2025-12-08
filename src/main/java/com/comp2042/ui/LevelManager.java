package com.comp2042.ui;

import com.comp2042.logic.Level;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages game levels for the levels game mode.
 * Provides access to level definitions and tracks level unlock status.
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 * @see com.comp2042.logic.Level
 */
public class LevelManager {
    private static final List<Level> levels = new ArrayList<>();
    
    /**
     * Initializes the predefined game levels.
     */
    static {
        levels.add(new Level(1, "First Steps", "Clear 5 lines", 5, 0, 0, 500));
        levels.add(new Level(2, "Speed Up", "Clear 8 lines within 2 minutes", 8, 0, 120, 400));
        levels.add(new Level(3, "Line Master", "Clear 3 lines at once 2 times", 0, 0, 0, 350));
        levels.add(new Level(4, "Score Challenge", "Reach 5000 points", 0, 5000, 0, 300));
        levels.add(new Level(5, "Rush Mode", "Clear 15 lines within 90 seconds", 15, 0, 90, 250));
    }
    
    /**
     * Gets a copy of all available levels.
     * 
     * @return a list of all levels
     */
    public static List<Level> getLevels() {
        return new ArrayList<>(levels);
    }
    
    /**
     * Gets a specific level by its number.
     * 
     * @param levelNumber the level number (1-based)
     * @return the Level object, or {@code null} if the level number is invalid
     */
    public static Level getLevel(int levelNumber) {
        if (levelNumber < 1 || levelNumber > levels.size()) {
            return null;
        }
        return levels.get(levelNumber - 1);
    }
    
    /**
     * Checks if a level is unlocked based on previous level completion.
     * Level 1 is always unlocked.
     * 
     * @param levelNumber the level number to check
     * @return {@code true} if the level is unlocked, {@code false} otherwise
     */
    public static boolean isLevelUnlocked(int levelNumber) {
        if (levelNumber == 1) {
            return true;
        }
        Level previousLevel = getLevel(levelNumber - 1);
        return previousLevel != null && previousLevel.isCompleted();
    }
    
    /**
     * Resets all level completion statuses.
     */
    public static void resetLevels() {
        LevelProgressManager.resetLevelProgress();
    }
}

