package com.comp2042.ui;

import java.io.*;
import java.nio.file.*;

/**
 * Manages the global high score for the game.
 * Handles loading, saving, and updating the high score with the player's name.
 * High score data is persisted to a file in the user's home directory.
 * 
 * @author TetrisJFX Team
 */
public final class HighScoreManager {

    private static final String HIGH_SCORE_FILE = "highscore.dat";
    private static int highScore = 0;
    private static String highScoreHolder = null;

    static {
        loadHighScore();
    }

    private HighScoreManager() {}

    public static int getHighScore() {
        return highScore;
    }
    
    public static String getHighScoreHolder() {
        return highScoreHolder;
    }

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

    private static void saveHighScore() {
        try {
            Path path = getHighScorePath();
            String content = highScore + "|" + (highScoreHolder != null ? highScoreHolder : "");
            Files.writeString(path, content);
        } catch (IOException ignored) {
        }
    }

    private static Path getHighScorePath() {
        String userHome = System.getProperty("user.home");
        return Paths.get(userHome, ".tetrisjfx", HIGH_SCORE_FILE);
    }

    public static void ensureDirectoryExists() {
        try {
            Path dir = getHighScorePath().getParent();
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
        } catch (IOException ignored) {
        }
    }
    
    public static void resetHighScore() {
        highScore = 0;
        highScoreHolder = null;
        saveHighScore();
    }
}

