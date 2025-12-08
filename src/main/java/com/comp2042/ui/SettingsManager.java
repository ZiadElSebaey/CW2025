package com.comp2042.ui;

import java.io.*;
import java.nio.file.*;

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

    public static boolean isGhostBlockEnabled() {
        return ghostBlockEnabled;
    }

    public static void setGhostBlockEnabled(boolean enabled) {
        ghostBlockEnabled = enabled;
        saveSettings();
    }

    public static boolean isMusicEnabled() {
        return musicEnabled;
    }

    public static void setMusicEnabled(boolean enabled) {
        musicEnabled = enabled;
        saveSettings();
    }

    public static boolean isSoundEffectsEnabled() {
        return soundEffectsEnabled;
    }

    public static void setSoundEffectsEnabled(boolean enabled) {
        soundEffectsEnabled = enabled;
        saveSettings();
    }

    public static String getSelectedMusicTrack() {
        return selectedMusicTrack;
    }

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



