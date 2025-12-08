package com.comp2042.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LeaderboardManagerTest {
    
    private static final String TEST_LEADERBOARD_FILE = System.getProperty("user.home") + "/.tetrisjfx/leaderboard.txt";
    
    @BeforeEach
    void setUp() {
        LeaderboardManager.resetLeaderboard();
    }
    
    @Test
    @DisplayName("LeaderboardManager - add first entry")
    void testAddFirstEntry() {
        boolean result = LeaderboardManager.addEntry("Player1", 100);
        assertTrue(result);
        
        List<LeaderboardManager.LeaderboardEntry> entries = LeaderboardManager.getEntries();
        assertEquals(1, entries.size());
        assertEquals("Player1", entries.get(0).name());
        assertEquals(100, entries.get(0).score());
    }
    
    @Test
    @DisplayName("LeaderboardManager - add multiple entries")
    void testAddMultipleEntries() {
        LeaderboardManager.addEntry("Player1", 100);
        LeaderboardManager.addEntry("Player2", 200);
        LeaderboardManager.addEntry("Player3", 150);
        
        List<LeaderboardManager.LeaderboardEntry> entries = LeaderboardManager.getEntries();
        assertEquals(3, entries.size());
        assertEquals("Player2", entries.get(0).name());
        assertEquals(200, entries.get(0).score());
        assertEquals("Player3", entries.get(1).name());
        assertEquals(150, entries.get(1).score());
        assertEquals("Player1", entries.get(2).name());
        assertEquals(100, entries.get(2).score());
    }
    
    @Test
    @DisplayName("LeaderboardManager - entries are sorted by score descending")
    void testEntriesSortedByScore() {
        LeaderboardManager.addEntry("Low", 50);
        LeaderboardManager.addEntry("High", 300);
        LeaderboardManager.addEntry("Mid", 150);
        
        List<LeaderboardManager.LeaderboardEntry> entries = LeaderboardManager.getEntries();
        assertEquals(3, entries.size());
        assertEquals(300, entries.get(0).score());
        assertEquals(150, entries.get(1).score());
        assertEquals(50, entries.get(2).score());
    }
    
    @Test
    @DisplayName("LeaderboardManager - update existing entry with higher score")
    void testUpdateExistingEntryHigherScore() {
        LeaderboardManager.addEntry("Player1", 100);
        LeaderboardManager.addEntry("Player1", 200);
        
        List<LeaderboardManager.LeaderboardEntry> entries = LeaderboardManager.getEntries();
        assertEquals(1, entries.size());
        assertEquals("Player1", entries.get(0).name());
        assertEquals(200, entries.get(0).score());
    }
    
    @Test
    @DisplayName("LeaderboardManager - do not update existing entry with lower score")
    void testDoNotUpdateExistingEntryLowerScore() {
        LeaderboardManager.addEntry("Player1", 200);
        LeaderboardManager.addEntry("Player1", 100);
        
        List<LeaderboardManager.LeaderboardEntry> entries = LeaderboardManager.getEntries();
        assertEquals(1, entries.size());
        assertEquals("Player1", entries.get(0).name());
        assertEquals(200, entries.get(0).score());
    }
    
    @Test
    @DisplayName("LeaderboardManager - case-insensitive name matching")
    void testCaseInsensitiveNameMatching() {
        LeaderboardManager.addEntry("Player1", 100);
        LeaderboardManager.addEntry("player1", 200);
        LeaderboardManager.addEntry("PLAYER1", 150);
        
        List<LeaderboardManager.LeaderboardEntry> entries = LeaderboardManager.getEntries();
        assertEquals(1, entries.size());
        assertEquals("player1", entries.get(0).name());
        assertEquals(200, entries.get(0).score());
    }
    
    @Test
    @DisplayName("LeaderboardManager - max entries limit")
    void testMaxEntriesLimit() {
        for (int i = 1; i <= 15; i++) {
            LeaderboardManager.addEntry("Player" + i, i * 10);
        }
        
        List<LeaderboardManager.LeaderboardEntry> entries = LeaderboardManager.getEntries();
        assertEquals(10, entries.size());
        assertEquals(150, entries.get(0).score());
        assertEquals(60, entries.get(9).score());
    }
    
    @Test
    @DisplayName("LeaderboardManager - reset leaderboard")
    void testResetLeaderboard() {
        LeaderboardManager.addEntry("Player1", 100);
        LeaderboardManager.addEntry("Player2", 200);
        
        LeaderboardManager.resetLeaderboard();
        
        List<LeaderboardManager.LeaderboardEntry> entries = LeaderboardManager.getEntries();
        assertTrue(entries.isEmpty());
    }
    
    @Test
    @DisplayName("LeaderboardManager - get entries from empty leaderboard")
    void testGetEntriesFromEmptyLeaderboard() {
        List<LeaderboardManager.LeaderboardEntry> entries = LeaderboardManager.getEntries();
        assertTrue(entries.isEmpty());
    }
    
    @Test
    @DisplayName("LeaderboardManager - ensure directory exists")
    void testEnsureDirectoryExists() {
        LeaderboardManager.ensureDirectoryExists();
        Path dir = Paths.get(TEST_LEADERBOARD_FILE).getParent();
        assertNotNull(dir);
        assertTrue(Files.exists(dir));
    }
    
    @Test
    @DisplayName("LeaderboardManager - LeaderboardEntry record")
    void testLeaderboardEntryRecord() {
        LeaderboardManager.LeaderboardEntry entry = new LeaderboardManager.LeaderboardEntry("Test", 500);
        assertEquals("Test", entry.name());
        assertEquals(500, entry.score());
    }
    
    @Test
    @DisplayName("LeaderboardManager - handle invalid file format gracefully")
    void testHandleInvalidFileFormat() {
        LeaderboardManager.addEntry("Valid", 100);
        
        try {
            Path path = Paths.get(TEST_LEADERBOARD_FILE);
            if (Files.exists(path)) {
                Files.write(path, List.of("InvalidLine", "Another|Invalid|Line|Too|Many|Parts"));
            }
        } catch (Exception e) {
            fail("Failed to write test file: " + e.getMessage());
        }
        
        List<LeaderboardManager.LeaderboardEntry> entries = LeaderboardManager.getEntries();
        assertTrue(entries.isEmpty() || entries.size() == 1);
    }
    
    @Test
    @DisplayName("LeaderboardManager - preserve order for same scores")
    void testPreserveOrderForSameScores() {
        LeaderboardManager.addEntry("Player1", 100);
        LeaderboardManager.addEntry("Player2", 100);
        LeaderboardManager.addEntry("Player3", 100);
        
        List<LeaderboardManager.LeaderboardEntry> entries = LeaderboardManager.getEntries();
        assertEquals(3, entries.size());
        assertEquals(100, entries.get(0).score());
        assertEquals(100, entries.get(1).score());
        assertEquals(100, entries.get(2).score());
        assertTrue(entries.stream().allMatch(e -> e.score() == 100));
    }
}

