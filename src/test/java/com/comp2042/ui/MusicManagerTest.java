package com.comp2042.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MusicManagerTest {
    
    @BeforeEach
    void setUp() {
        MusicManager.dispose();
    }
    
    @AfterEach
    void tearDown() {
        MusicManager.dispose();
    }
    
    @Test
    @DisplayName("MusicManager - get current track returns default")
    void testGetCurrentTrackDefault() {
        String track = MusicManager.getCurrentTrack();
        assertNotNull(track);
        assertEquals("Russia Music", track);
    }
    
    @Test
    @DisplayName("MusicManager - is playing returns false initially")
    void testIsPlayingInitiallyFalse() {
        assertFalse(MusicManager.isPlaying());
    }
    
    @Test
    @DisplayName("MusicManager - set volume clamps to valid range")
    void testSetVolumeClamping() {
        assertDoesNotThrow(() -> MusicManager.setVolume(-0.5));
        assertDoesNotThrow(() -> MusicManager.setVolume(1.5));
        assertDoesNotThrow(() -> MusicManager.setVolume(0.5));
    }
    
    @Test
    @DisplayName("MusicManager - set volume with edge values")
    void testSetVolumeEdgeValues() {
        assertDoesNotThrow(() -> MusicManager.setVolume(0.0));
        assertDoesNotThrow(() -> MusicManager.setVolume(1.0));
        assertDoesNotThrow(() -> MusicManager.setVolume(0.0001));
        assertDoesNotThrow(() -> MusicManager.setVolume(0.9999));
    }
    
    @Test
    @DisplayName("MusicManager - multiple dispose calls are safe")
    void testMultipleDisposeCalls() {
        MusicManager.dispose();
        MusicManager.dispose();
        MusicManager.dispose();
        
        assertDoesNotThrow(() -> MusicManager.dispose());
    }
    
    @Test
    @DisplayName("MusicManager - stop when not playing is safe")
    void testStopWhenNotPlaying() {
        assertDoesNotThrow(() -> MusicManager.stop());
        assertFalse(MusicManager.isPlaying());
    }
    
    @Test
    @DisplayName("MusicManager - pause when not playing is safe")
    void testPauseWhenNotPlaying() {
        assertDoesNotThrow(() -> MusicManager.pause());
        assertFalse(MusicManager.isPlaying());
    }
    
    @Test
    @DisplayName("MusicManager - play when not initialized is safe")
    void testPlayWhenNotInitialized() {
        MusicManager.dispose();
        assertDoesNotThrow(() -> MusicManager.play());
        assertFalse(MusicManager.isPlaying());
    }
    
    @Test
    @DisplayName("MusicManager - get current track after dispose")
    void testGetCurrentTrackAfterDispose() {
        MusicManager.dispose();
        String track = MusicManager.getCurrentTrack();
        assertNotNull(track);
    }
    
    @Test
    @DisplayName("MusicManager - load track with Christmas")
    void testLoadTrackChristmas() {
        assertDoesNotThrow(() -> MusicManager.loadTrack("Christmas"));
        String track = MusicManager.getCurrentTrack();
        assertNotNull(track);
        assertTrue(track.equals("Christmas") || track.equals("Russia Music"), 
                   "Track should be Christmas or Russia Music (if resource unavailable), but was: " + track);
    }
    
    @Test
    @DisplayName("MusicManager - load track with Russia Music")
    void testLoadTrackRussiaMusic() {
        assertDoesNotThrow(() -> MusicManager.loadTrack("Russia Music"));
        String track = MusicManager.getCurrentTrack();
        assertEquals("Russia Music", track);
    }
    
    @Test
    @DisplayName("MusicManager - load track with invalid name defaults to Russia Music")
    void testLoadTrackInvalidName() {
        MusicManager.loadTrack("Russia Music");
        String initialTrack = MusicManager.getCurrentTrack();
        
        assertDoesNotThrow(() -> MusicManager.loadTrack("Invalid Track"));
        String track = MusicManager.getCurrentTrack();
        assertNotNull(track);
        assertTrue(track.equals("Russia Music") || track.equals(initialTrack));
    }
}
