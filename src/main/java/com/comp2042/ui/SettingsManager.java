package com.comp2042.ui;

import java.io.*;
import java.nio.file.*;

/**
 * Manages persistent application settings.
 * Handles loading and saving user preferences including ghost block,
 * music, sound effects, and music track selection.
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
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
     * @return {@code true} if ghost block is enabled, {@code false} otherwise
     */
    public static boolean isGhostBlockEnabled() {
        return ghostBlockEnabled;
    }

    /**
     * Sets the ghost block enabled state and saves to persistent storage.
     * 
     * @param enabled the new ghost block state
     */
    public static void setGhostBlockEnabled(boolean enabled) {
        ghostBlockEnabled = enabled;
        saveSettings();
    }

    /**
     * Checks if background music is enabled.
     * 
     * @return {@code true} if music is enabled, {@code false} otherwise
     */
    public static boolean isMusicEnabled() {
        return musicEnabled;
    }

    /**
     * Sets the music enabled state and saves to persistent storage.
     * 
     * @param enabled the new music state
     */
    public static void setMusicEnabled(boolean enabled) {
        musicEnabled = enabled;
        saveSettings();
    }

    /**
     * Checks if sound effects are enabled.
     * 
     * @return {@code true} if sound effects are enabled, {@code false} otherwise
     */
    public static boolean isSoundEffectsEnabled() {
        return soundEffectsEnabled;
    }

    /**
     * Sets the sound effects enabled state and saves to persistent storage.
     * 
     * @param enabled the new sound effects state
     */
    public static void setSoundEffectsEnabled(boolean enabled) {
        soundEffectsEnabled = enabled;
        saveSettings();
    }

    /**
     * Gets the currently selected music track.
     * 
     * @return the name of the selected music track
     */
    public static String getSelectedMusicTrack() {
        return selectedMusicTrack;
    }

    /**
     * Sets the selected music track and saves to persistent storage.
     * 
     * @param track the name of the music track to select
     */
    public static void setSelectedMusicTrack(String track) {
        selectedMusicTrack = track;
        saveSettings();
    }

    /**
     * Loads settings from persistent storage.
     */
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

    /**
     * Saves current settings to persistent storage.
     */
    private static void saveSettings() {
        try {
            Path path = getSettingsPath();
            String content = ghostBlockEnabled + "|" + musicEnabled + "|" + selectedMusicTrack + "|" + soundEffectsEnabled;
            Files.writeString(path, content);
        } catch (IOException ignored) {
        }
    }

    /**
     * Gets the file path for the settings file.
     * 
     * @return the settings file path
     */
    private static Path getSettingsPath() {
        String userHome = System.getProperty("user.home");
        return Paths.get(userHome, ".tetrisjfx", SETTINGS_FILE);
    }

    /**
     * Ensures the directory for storing settings exists.
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



