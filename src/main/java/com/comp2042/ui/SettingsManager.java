package com.comp2042.ui;

import java.io.*;
import java.nio.file.*;

public final class SettingsManager {

    private static final String SETTINGS_FILE = "settings.dat";
    private static boolean ghostBlockEnabled = true; // Default to enabled

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

    private static void loadSettings() {
        try {
            Path path = getSettingsPath();
            if (Files.exists(path)) {
                String content = Files.readString(path).trim();
                String[] parts = content.split("\\|");
                if (parts.length >= 1 && !parts[0].isEmpty()) {
                    ghostBlockEnabled = Boolean.parseBoolean(parts[0]);
                }
            }
        } catch (IOException | NumberFormatException ignored) {
        }
    }

    private static void saveSettings() {
        try {
            Path path = getSettingsPath();
            String content = String.valueOf(ghostBlockEnabled);
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



