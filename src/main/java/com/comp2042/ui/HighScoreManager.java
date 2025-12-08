package com.comp2042.ui;

import java.io.*;
import java.nio.file.*;

/**
 * Manages the game's high score persistence.
 * Handles loading, saving, and updating the highest score achieved.
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 */
public final class HighScoreManager {

    private static final String HIGH_SCORE_FILE = "highscore.dat";
    private static int highScore = 0;
    private static String highScoreHolder = null;

    static {
        loadHighScore();
    }

    private HighScoreManager() {}

    /**
     * Gets the current high score.
     * 
     * @return the high score value
     */
    public static int getHighScore() {
        return highScore;
    }
    
    /**
     * Gets the name of the player who achieved the high score.
     * 
     * @return the high score holder's name, or {@code null} if no high score exists
     */
    public static String getHighScoreHolder() {
        return highScoreHolder;
    }

    /**
     * Updates the high score if the new score is higher.
     * 
     * @param score the new score to check
     * @param playerName the name of the player
     * @return {@code true} if the high score was updated, {@code false} otherwise
     */
    public static boolean updateHighScore(int score, String playerName) {
        if (playerName == null || playerName.isEmpty()) {
            return false;
        }
        
        boolean updated = false;
        if (score > highScore) {
            highScore = score;
            highScoreHolder = playerName;
            saveHighScore();
            updated = true;
        } else if (highScoreHolder != null && highScoreHolder.equalsIgnoreCase(playerName) && score == highScore) {
            highScoreHolder = playerName;
            saveHighScore();
        }
        return updated;
    }

    /**
     * Loads the high score from persistent storage.
     */
    private static void loadHighScore() {
        try {
            Path path = getHighScorePath();
            if (Files.exists(path)) {
                String content = Files.readString(path).trim();
                String[] parts = content.split("\\|");
                if (parts.length >= 1) {
                    highScore = Integer.parseInt(parts[0]);
                }
                if (parts.length >= 2 && !parts[1].isEmpty()) {
                    highScoreHolder = parts[1];
                }
            }
        } catch (IOException | NumberFormatException ignored) {
        }
    }

    /**
     * Saves the high score to persistent storage.
     */
    private static void saveHighScore() {
        try {
            Path path = getHighScorePath();
            String content = highScore + "|" + (highScoreHolder != null ? highScoreHolder : "");
            Files.writeString(path, content);
        } catch (IOException ignored) {
        }
    }

    /**
     * Gets the file path for the high score file.
     * 
     * @return the Path to the high score file
     */
    private static Path getHighScorePath() {
        String userHome = System.getProperty("user.home");
        return Paths.get(userHome, ".tetrisjfx", HIGH_SCORE_FILE);
    }

    /**
     * Ensures the directory for storing high scores exists.
     */
    public static void ensureDirectoryExists() {
        try {
            Path dir = getHighScorePath().getParent();
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
        } catch (IOException ignored) {
        }
    }
    
    /**
     * Resets the high score to zero and clears the holder name.
     */
    public static void resetHighScore() {
        highScore = 0;
        highScoreHolder = null;
        saveHighScore();
    }
}

