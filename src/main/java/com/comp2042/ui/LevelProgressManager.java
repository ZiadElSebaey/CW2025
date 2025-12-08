package com.comp2042.ui;

import com.comp2042.logic.Level;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages level progress persistence for the levels game mode.
 * Handles saving and loading completed level information.
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 * @see com.comp2042.logic.Level
 * @see LevelManager
 */
public final class LevelProgressManager {

    private static final String PROGRESS_FILE = "levelprogress.dat";
    private static boolean initialized = false;

    private LevelProgressManager() {}

    /**
     * Ensures the directory for storing level progress exists.
     */
    public static void ensureDirectoryExists() {
        try {
            Path dir = getProgressDirectory();
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the level progress manager and loads saved progress.
     * Should be called once at application startup.
     */
    public static void initialize() {
        if (initialized) {
            return;
        }
        ensureDirectoryExists();
        loadLevelProgress();
        initialized = true;
    }

    /**
     * Saves the current level completion status to persistent storage.
     */
    public static void saveLevelProgress() {
        ensureDirectoryExists();
        try {
            Path path = getProgressPath();
            List<Integer> completedLevels = new ArrayList<>();
            
            for (Level level : LevelManager.getLevels()) {
                if (level.isCompleted()) {
                    completedLevels.add(level.getLevelNumber());
                }
            }
            
            StringBuilder content = new StringBuilder();
            for (int i = 0; i < completedLevels.size(); i++) {
                if (i > 0) {
                    content.append(",");
                }
                content.append(completedLevels.get(i));
            }
            
            Files.writeString(path, content.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads level progress from persistent storage.
     */
    private static void loadLevelProgress() {
        try {
            Path path = getProgressPath();
            if (Files.exists(path)) {
                String content = Files.readString(path).trim();
                if (!content.isEmpty()) {
                    String[] levelNumbers = content.split(",");
                    for (String levelNumStr : levelNumbers) {
                        try {
                            int levelNumber = Integer.parseInt(levelNumStr.trim());
                            Level level = LevelManager.getLevel(levelNumber);
                            if (level != null) {
                                level.setCompleted(true);
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets all level completion statuses and saves the changes.
     */
    public static void resetLevelProgress() {
        List<Level> levels = LevelManager.getLevels();
        for (Level level : levels) {
            if (level.getLevelNumber() != 1) {
                level.setCompleted(false);
            }
        }
        Level level1 = LevelManager.getLevel(1);
        if (level1 != null) {
            level1.setCompleted(false);
        }
        saveLevelProgress();
    }

    /**
     * Gets the directory path for storing level progress.
     * 
     * @return the progress directory path
     */
    private static Path getProgressDirectory() {
        String userHome = System.getProperty("user.home");
        return Paths.get(userHome, ".tetrisjfx");
    }

    /**
     * Gets the file path for the level progress file.
     * 
     * @return the progress file path
     */
    private static Path getProgressPath() {
        return getProgressDirectory().resolve(PROGRESS_FILE);
    }
}

