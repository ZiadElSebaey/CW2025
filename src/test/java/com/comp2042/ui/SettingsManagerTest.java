package com.comp2042.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SettingsManager Tests")
class SettingsManagerTest {

    private Path testSettingsPath;
    private boolean originalGhostBlock;
    private boolean originalMusic;
    private boolean originalSoundEffects;
    private String originalTrack;

    @BeforeEach
    void setUp() {
        String userHome = System.getProperty("user.home");
        testSettingsPath = Paths.get(userHome, ".tetrisjfx", "settings.dat");
        
        originalGhostBlock = SettingsManager.isGhostBlockEnabled();
        originalMusic = SettingsManager.isMusicEnabled();
        originalSoundEffects = SettingsManager.isSoundEffectsEnabled();
        originalTrack = SettingsManager.getSelectedMusicTrack();
        
        try {
            if (Files.exists(testSettingsPath)) {
                Files.delete(testSettingsPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        SettingsManager.setGhostBlockEnabled(originalGhostBlock);
        SettingsManager.setMusicEnabled(originalMusic);
        SettingsManager.setSoundEffectsEnabled(originalSoundEffects);
        SettingsManager.setSelectedMusicTrack(originalTrack);
    }

    @Test
    @DisplayName("SettingsManager - ghost block setting persistence")
    void testGhostBlockSetting() {
        boolean original = SettingsManager.isGhostBlockEnabled();
        SettingsManager.setGhostBlockEnabled(!original);
        assertEquals(!original, SettingsManager.isGhostBlockEnabled());
        SettingsManager.setGhostBlockEnabled(original);
    }

    @Test
    @DisplayName("SettingsManager - music setting persistence")
    void testMusicSetting() {
        boolean original = SettingsManager.isMusicEnabled();
        SettingsManager.setMusicEnabled(!original);
        assertEquals(!original, SettingsManager.isMusicEnabled());
        SettingsManager.setMusicEnabled(original);
    }

    @Test
    @DisplayName("SettingsManager - sound effects setting persistence")
    void testSoundEffectsSetting() {
        boolean original = SettingsManager.isSoundEffectsEnabled();
        SettingsManager.setSoundEffectsEnabled(!original);
        assertEquals(!original, SettingsManager.isSoundEffectsEnabled());
        SettingsManager.setSoundEffectsEnabled(original);
    }

    @Test
    @DisplayName("SettingsManager - music track selection")
    void testMusicTrackSelection() {
        String original = SettingsManager.getSelectedMusicTrack();
        SettingsManager.setSelectedMusicTrack("Christmas");
        assertEquals("Christmas", SettingsManager.getSelectedMusicTrack());
        SettingsManager.setSelectedMusicTrack(original);
    }

    @Test
    @DisplayName("SettingsManager - all settings can be toggled")
    void testAllSettingsToggle() {
        boolean ghost = SettingsManager.isGhostBlockEnabled();
        boolean music = SettingsManager.isMusicEnabled();
        boolean sound = SettingsManager.isSoundEffectsEnabled();
        
        SettingsManager.setGhostBlockEnabled(!ghost);
        SettingsManager.setMusicEnabled(!music);
        SettingsManager.setSoundEffectsEnabled(!sound);
        
        assertEquals(!ghost, SettingsManager.isGhostBlockEnabled());
        assertEquals(!music, SettingsManager.isMusicEnabled());
        assertEquals(!sound, SettingsManager.isSoundEffectsEnabled());
        
        SettingsManager.setGhostBlockEnabled(ghost);
        SettingsManager.setMusicEnabled(music);
        SettingsManager.setSoundEffectsEnabled(sound);
    }

    @Test
    @DisplayName("SettingsManager - ensureDirectoryExists creates directory")
    void testEnsureDirectoryExists() {
        SettingsManager.ensureDirectoryExists();
        Path dir = testSettingsPath.getParent();
        assertTrue(Files.exists(dir));
        assertTrue(Files.isDirectory(dir));
    }
}
