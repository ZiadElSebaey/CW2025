package com.comp2042.ui;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Manages the game leaderboard.
 * Handles adding, retrieving, and managing leaderboard entries.
 * Entries are sorted by score in descending order and limited to a maximum
 * of 10 entries. Supports case-insensitive name matching for updates.
 * 
 * Leaderboard data is persisted to a file in the user's home directory.
 * 
 * @author TetrisJFX Team
 */
public class LeaderboardManager {
    
    private static final String LEADERBOARD_FILE = System.getProperty("user.home") + "/.tetrisjfx/leaderboard.txt";
    private static final int MAX_ENTRIES = 10;
    
    /**
     * Ensures the leaderboard directory exists.
     * Creates the directory if it doesn't exist.
     */
    public static void ensureDirectoryExists() {
        try {
            Path path = Paths.get(LEADERBOARD_FILE).getParent();
            if (path != null && !Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException ignored) {
        }
    }
    
    /**
     * Adds or updates a leaderboard entry.
     * If an entry with the same name (case-insensitive) exists, it will be
     * updated only if the new score is higher. Otherwise, a new entry is added.
     * Entries are automatically sorted by score and limited to MAX_ENTRIES.
     * 
     * @param name The player's name
     * @param score The player's score
     * @return true if the entry was added or updated
     */
    public static boolean addEntry(String name, int score) {
        ensureDirectoryExists();
        List<LeaderboardEntry> entries = getEntries();
        
        boolean nameExists = false;
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).name().equalsIgnoreCase(name)) {
                if (score > entries.get(i).score()) {
                    entries.set(i, new LeaderboardEntry(name, score));
                }
                nameExists = true;
                break;
            }
        }
        
        if (!nameExists) {
            entries.add(new LeaderboardEntry(name, score));
        }
        
        entries.sort((a, b) -> Integer.compare(b.score(), a.score()));
        
        if (entries.size() > MAX_ENTRIES) {
            entries = new ArrayList<>(entries.subList(0, MAX_ENTRIES));
        }
        
        saveEntries(entries);
        return true;
    }
    
    /**
     * Retrieves all leaderboard entries, sorted by score in descending order.
     * 
     * @return A list of leaderboard entries, sorted by score (highest first)
     */
    public static List<LeaderboardEntry> getEntries() {
        ensureDirectoryExists();
        List<LeaderboardEntry> entries = new ArrayList<>();
        try {
            Path path = Paths.get(LEADERBOARD_FILE);
            if (Files.exists(path)) {
                List<String> lines = Files.readAllLines(path);
                for (String line : lines) {
                    String[] parts = line.split("\\|");
                    if (parts.length == 2) {
                        entries.add(new LeaderboardEntry(parts[0], Integer.parseInt(parts[1])));
                    }
                }
            }
        } catch (IOException | NumberFormatException ignored) {
        }
        return entries;
    }
    
    private static void saveEntries(List<LeaderboardEntry> entries) {
        try {
            List<String> lines = new ArrayList<>();
            for (LeaderboardEntry entry : entries) {
                lines.add(entry.name() + "|" + entry.score());
            }
            Files.write(Paths.get(LEADERBOARD_FILE), lines);
        } catch (IOException ignored) {
        }
    }
    
    /**
     * Resets the leaderboard by clearing all entries.
     */
    public static void resetLeaderboard() {
        ensureDirectoryExists();
        saveEntries(new ArrayList<>());
    }
    
    /**
     * Represents a single leaderboard entry.
     * 
     * @param name The player's name
     * @param score The player's score
     */
    public record LeaderboardEntry(String name, int score) {}
}

