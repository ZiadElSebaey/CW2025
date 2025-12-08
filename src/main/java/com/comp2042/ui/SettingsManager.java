package com.comp2042.ui;

import java.io.*;
import java.nio.file.*;

/**
 * Manages persistent application settings.
 * Handles loading and saving of user preferences including:
 * - Ghost block visibility
 * - Music enabled/disabled state
 * - Selected music track
 * - Sound effects enabled/disabled state
 * 
 * Settings are persisted to a file in the user's home directory.
 * 
 * @author TetrisJFX Team
 */
public final class SettingsManager {

    private static final String SETTINGS_FILE = "settings.dat";
    private static boolean ghostBlockEnabled = true; // Default to enabled
    private static boolean musicEnabled = true; // Default to enabled
    private static boolean soundEffectsEnabled = true; // Default to enabled
    private static String selectedMusicTrack = "Russia Music"; // Default track

    static {
        loadSettings();
    }

    private SettingsManager() {}

    /**
     * Checks if the ghost block feature is enabled.
     * 
     * @return true if ghost block is enabled, false otherwise
     */
    public static boolean isGhostBlockEnabled() {
        return ghostBlockEnabled;
    }

    /**
     * Sets the ghost block feature enabled/disabled state.
     * 
     * @param enabled true to enable ghost block, false to disable
     */
    public static void setGhostBlockEnabled(boolean enabled) {
        ghostBlockEnabled = enabled;
        saveSettings();
    }

    /**
     * Checks if background music is enabled.
     * 
     * @return true if music is enabled, false otherwise
     */
    public static boolean isMusicEnabled() {
        return musicEnabled;
    }

    /**
     * Sets the background music enabled/disabled state.
     * 
     * @param enabled true to enable music, false to disable
     */
    public static void setMusicEnabled(boolean enabled) {
        musicEnabled = enabled;
        saveSettings();
    }

    /**
     * Checks if sound effects are enabled.
     * 
     * @return true if sound effects are enabled, false otherwise
     */
    public static boolean isSoundEffectsEnabled() {
        return soundEffectsEnabled;
    }

    /**
     * Sets the sound effects enabled/disabled state.
     * 
     * @param enabled true to enable sound effects, false to disable
     */
    public static void setSoundEffectsEnabled(boolean enabled) {
        soundEffectsEnabled = enabled;
        saveSettings();
    }

    /**
     * Gets the currently selected music track name.
     * 
     * @return The music track name (e.g., "Russia Music", "Christmas")
     */
    public static String getSelectedMusicTrack() {
        return selectedMusicTrack;
    }

    /**
     * Sets the selected music track.
     * 
     * @param track The music track name (e.g., "Russia Music", "Christmas")
     */
    public static void setSelectedMusicTrack(String track) {
        selectedMusicTrack = track;
        saveSettings();
    }

    private static void loadSettings() {
        try {
            Path path = getSettingsPath();
            if (Files.exists(path)) {
                String content = Files.readString(path).trim();
                String[] parts = content.split("\\|");
                if (parts.length >= 1 && !parts[0].isEmpty()) {
                    ghostBlockEnabled = Boolean.parseBoolean(parts[0]);
                }
                if (parts.length >= 2 && !parts[1].isEmpty()) {
                    musicEnabled = Boolean.parseBoolean(parts[1]);
                }
                if (parts.length >= 3 && !parts[2].isEmpty()) {
                    selectedMusicTrack = parts[2];
                }
                if (parts.length >= 4 && !parts[3].isEmpty()) {
                    soundEffectsEnabled = Boolean.parseBoolean(parts[3]);
                }
            }
        } catch (IOException | NumberFormatException ignored) {
        }
    }

    private static void saveSettings() {
        try {
            Path path = getSettingsPath();
            String content = ghostBlockEnabled + "|" + musicEnabled + "|" + selectedMusicTrack + "|" + soundEffectsEnabled;
            Files.writeString(path, content);
        } catch (IOException ignored) {
        }
    }

    private static Path getSettingsPath() {
        String userHome = System.getProperty("user.home");
        return Paths.get(userHome, ".tetrisjfx", SETTINGS_FILE);
    }

    /**
     * Ensures the settings directory exists in the user's home directory.
     * Creates the directory if it doesn't exist.
     */
    public static void ensureDirectoryExists() {
        try {
            Path dir = getSettingsPath().getParent();
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
        } catch (IOException ignored) {
        }
    }
}



