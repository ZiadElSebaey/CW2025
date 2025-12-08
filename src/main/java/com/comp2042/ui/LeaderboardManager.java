package com.comp2042.ui;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Manages the game leaderboard with top player scores.
 * Handles adding entries, sorting by score, and maintaining a maximum
 * of 10 entries. Supports case-insensitive name matching for updates.
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 */
public class LeaderboardManager {
    
    private static final String LEADERBOARD_FILE = System.getProperty("user.home") + "/.tetrisjfx/leaderboard.txt";
    private static final int MAX_ENTRIES = 10;
    
    /**
     * Ensures the directory for storing the leaderboard file exists.
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
     * Adds or updates an entry in the leaderboard.
     * If a player with the same name (case-insensitive) exists, updates
     * their score only if the new score is higher. Otherwise, adds a new entry.
     * Maintains a maximum of 10 entries, sorted by score descending.
     * 
     * @param name the player's name
     * @param score the player's score
     * @return {@code true} if the entry was added or updated
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
     * Gets all leaderboard entries, sorted by score descending.
     * 
     * @return a list of leaderboard entries
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
    
    /**
     * Saves the leaderboard entries to a file.
     * 
     * @param entries the list of entries to save
     */
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
     * Record representing a single leaderboard entry.
     * 
     * @param name the player's name
     * @param score the player's score
     */
    public record LeaderboardEntry(String name, int score) {}
}

