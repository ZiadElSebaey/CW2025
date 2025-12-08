package com.comp2042.ui;

import com.comp2042.logic.Level;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LevelProgressManager Tests")
class LevelProgressManagerTest {

    private Path testProgressPath;

    @BeforeEach
    void setUp() {
        String userHome = System.getProperty("user.home");
        testProgressPath = Paths.get(userHome, ".tetrisjfx", "levelprogress.dat");
        
        try {
            if (Files.exists(testProgressPath)) {
                Files.delete(testProgressPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        LevelProgressManager.resetLevelProgress();
        try {
            if (Files.exists(testProgressPath)) {
                Files.delete(testProgressPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("LevelProgressManager - ensureDirectoryExists creates directory")
    void testEnsureDirectoryExists() {
        LevelProgressManager.ensureDirectoryExists();
        Path dir = testProgressPath.getParent();
        assertTrue(Files.exists(dir));
        assertTrue(Files.isDirectory(dir));
    }

    @Test
    @DisplayName("LevelProgressManager - saveLevelProgress saves completed levels")
    void testSaveLevelProgress() {
        Level level1 = LevelManager.getLevel(1);
        Level level2 = LevelManager.getLevel(2);
        
        level1.setCompleted(true);
        level2.setCompleted(true);
        
        LevelProgressManager.saveLevelProgress();
        
        assertTrue(Files.exists(testProgressPath));
    }

    @Test
    @DisplayName("LevelProgressManager - resetLevelProgress clears all levels except level 1")
    void testResetLevelProgress() {
        Level level1 = LevelManager.getLevel(1);
        Level level2 = LevelManager.getLevel(2);
        Level level3 = LevelManager.getLevel(3);
        
        level1.setCompleted(true);
        level2.setCompleted(true);
        level3.setCompleted(true);
        
        LevelProgressManager.resetLevelProgress();
        
        assertFalse(level1.isCompleted());
        assertFalse(level2.isCompleted());
        assertFalse(level3.isCompleted());
    }

    @Test
    @DisplayName("LevelProgressManager - initialize loads progress")
    void testInitialize() {
        LevelProgressManager.initialize();
        assertTrue(Files.exists(testProgressPath.getParent()));
    }
}

