package com.comp2042.ui;

import java.io.*;
import java.nio.file.*;

public final class HighScoreManager {

    private static final String HIGH_SCORE_FILE = "highscore.dat";
    private static int highScore = 0;

    static {
        loadHighScore();
    }

    private HighScoreManager() {}

    public static int getHighScore() {
        return highScore;
    }

    public static boolean updateHighScore(int score) {
        if (score > highScore) {
            highScore = score;
            saveHighScore();
            return true;
        }
        return false;
    }

    private static void loadHighScore() {
        try {
            Path path = getHighScorePath();
            if (Files.exists(path)) {
                String content = Files.readString(path).trim();
                highScore = Integer.parseInt(content);
            }
        } catch (IOException | NumberFormatException ignored) {
        }
    }

    private static void saveHighScore() {
        try {
            Path path = getHighScorePath();
            Files.writeString(path, String.valueOf(highScore));
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
}

