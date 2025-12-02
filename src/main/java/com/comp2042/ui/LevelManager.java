package com.comp2042.ui;

import com.comp2042.logic.Level;
import java.util.ArrayList;
import java.util.List;

public class LevelManager {
    private static final List<Level> levels = new ArrayList<>();
    
    static {
        levels.add(new Level(1, "First Steps", "Clear 5 lines", 5, 0, 0, 500));
        levels.add(new Level(2, "Speed Up", "Clear 10 lines within 2 minutes", 10, 0, 120, 400));
        levels.add(new Level(3, "Line Master", "Clear 3 lines at once 2 times", 0, 0, 0, 350));
    }
    
    public static List<Level> getLevels() {
        return new ArrayList<>(levels);
    }
    
    public static Level getLevel(int levelNumber) {
        if (levelNumber < 1 || levelNumber > levels.size()) {
            return null;
        }
        return levels.get(levelNumber - 1);
    }
    
    public static boolean isLevelUnlocked(int levelNumber) {
        if (levelNumber == 1) {
            return true;
        }
        Level previousLevel = getLevel(levelNumber - 1);
        return previousLevel != null && previousLevel.isCompleted();
    }
}

