package com.comp2042.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SoundManagerTest {
    
    @BeforeEach
    void setUp() {
        SettingsManager.setSoundEffectsEnabled(true);
        SoundManager.dispose();
    }
    
    @AfterEach
    void tearDown() {
        SoundManager.dispose();
    }
    
    @Test
    @DisplayName("SoundManager - set volume clamps to valid range")
    void testSetVolumeClamping() {
        SoundManager.setSoundVolume("test-sound", -0.5);
        SoundManager.setSoundVolume("test-sound", 1.5);
        SoundManager.setSoundVolume("test-sound", 0.5);
        
        assertDoesNotThrow(() -> SoundManager.setSoundVolume("test-sound", 0.0));
        assertDoesNotThrow(() -> SoundManager.setSoundVolume("test-sound", 1.0));
        assertDoesNotThrow(() -> SoundManager.setSoundVolume("test-sound", 0.75));
    }
    
    @Test
    @DisplayName("SoundManager - stop non-existent sound does not throw")
    void testStopNonExistentSound() {
        assertDoesNotThrow(() -> SoundManager.stopSound("non-existent-sound"));
    }
    
    @Test
    @DisplayName("SoundManager - stop all sounds clears map")
    void testStopAllSounds() {
        SoundManager.stopAllSounds();
        assertDoesNotThrow(() -> SoundManager.stopAllSounds());
    }
    
    @Test
    @DisplayName("SoundManager - dispose clears all resources")
    void testDispose() {
        SoundManager.dispose();
        assertDoesNotThrow(() -> SoundManager.dispose());
    }
    
    @Test
    @DisplayName("SoundManager - set volume for multiple sounds")
    void testSetVolumeForMultipleSounds() {
        SoundManager.setSoundVolume("sound1", 0.3);
        SoundManager.setSoundVolume("sound2", 0.7);
        SoundManager.setSoundVolume("sound3", 0.5);
        
        assertDoesNotThrow(() -> {
            SoundManager.setSoundVolume("sound1", 0.4);
            SoundManager.setSoundVolume("sound2", 0.6);
            SoundManager.setSoundVolume("sound3", 0.8);
        });
    }
    
    @Test
    @DisplayName("SoundManager - does not play when sound effects disabled")
    void testDoesNotPlayWhenDisabled() {
        SettingsManager.setSoundEffectsEnabled(false);
        
        assertDoesNotThrow(() -> SoundManager.playSound("fall"));
        assertDoesNotThrow(() -> SoundManager.playSound("glass-break"));
    }
    
    @Test
    @DisplayName("SoundManager - handles null or invalid sound names gracefully")
    void testHandlesInvalidSoundNames() {
        SettingsManager.setSoundEffectsEnabled(true);
        
        assertDoesNotThrow(() -> SoundManager.playSound("non-existent-sound"));
        assertDoesNotThrow(() -> SoundManager.stopSound("non-existent-sound"));
    }
    
    @Test
    @DisplayName("SoundManager - multiple dispose calls are safe")
    void testMultipleDisposeCalls() {
        SoundManager.dispose();
        SoundManager.dispose();
        SoundManager.dispose();
        
        assertDoesNotThrow(() -> SoundManager.dispose());
    }
    
    @Test
    @DisplayName("SoundManager - set volume with edge values")
    void testSetVolumeEdgeValues() {
        assertDoesNotThrow(() -> SoundManager.setSoundVolume("test", 0.0));
        assertDoesNotThrow(() -> SoundManager.setSoundVolume("test", 1.0));
        assertDoesNotThrow(() -> SoundManager.setSoundVolume("test", 0.0001));
        assertDoesNotThrow(() -> SoundManager.setSoundVolume("test", 0.9999));
    }
}
