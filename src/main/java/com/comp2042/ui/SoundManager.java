package com.comp2042.ui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages all sound effects in the game.
 * Provides centralized sound effect playback with automatic resource management.
 */
public final class SoundManager {
    
    private static final Map<String, MediaPlayer> soundPlayers = new HashMap<>();
    private static final Map<String, Double> soundVolumes = new HashMap<>();
    
    static {
        // Initialize default volumes for each sound effect
        soundVolumes.put("fall", 0.6);
        soundVolumes.put("glass-break", 0.6);
        soundVolumes.put("gameover1984", 0.7);
        soundVolumes.put("gameover", 0.7);
        soundVolumes.put("next-level-tetris-sounds", 0.7);
        soundVolumes.put("3-2-1", 0.7);
    }
    
    private SoundManager() {}
    
    /**
     * Plays a sound effect if sound effects are enabled.
     * 
     * @param soundName The name of the sound file (without path and extension)
     *                  e.g., "fall" for "sfx/fall.mp3"
     */
    public static void playSound(String soundName) {
        if (!SettingsManager.isSoundEffectsEnabled()) {
            return;
        }
        
        try {
            URL soundUrl = SoundManager.class.getClassLoader().getResource("sfx/" + soundName + ".mp3");
            if (soundUrl != null) {
                // Stop and dispose previous instance if exists
                stopSound(soundName);
                
                Media media = new Media(soundUrl.toExternalForm());
                MediaPlayer player = new MediaPlayer(media);
                
                // Set volume from defaults or use 0.7 as fallback
                double volume = soundVolumes.getOrDefault(soundName, 0.7);
                player.setVolume(volume);
                
                soundPlayers.put(soundName, player);
                player.play();
            }
        } catch (Exception e) {
            System.err.println("Failed to play sound effect '" + soundName + "': " + e.getMessage());
        }
    }
    
    /**
     * Stops and disposes a specific sound effect.
     * 
     * @param soundName The name of the sound to stop
     */
    public static void stopSound(String soundName) {
        MediaPlayer player = soundPlayers.get(soundName);
        if (player != null) {
            player.stop();
            player.dispose();
            soundPlayers.remove(soundName);
        }
    }
    
    /**
     * Stops and disposes all sound effects.
     */
    public static void stopAllSounds() {
        for (MediaPlayer player : soundPlayers.values()) {
            if (player != null) {
                player.stop();
                player.dispose();
            }
        }
        soundPlayers.clear();
    }
    
    /**
     * Sets the volume for a specific sound effect.
     * 
     * @param soundName The name of the sound
     * @param volume The volume (0.0 to 1.0)
     */
    public static void setSoundVolume(String soundName, double volume) {
        soundVolumes.put(soundName, Math.max(0.0, Math.min(1.0, volume)));
        MediaPlayer player = soundPlayers.get(soundName);
        if (player != null) {
            player.setVolume(soundVolumes.get(soundName));
        }
    }
    
    /**
     * Disposes all sound effects and cleans up resources.
     * Should be called when the application is closing.
     */
    public static void dispose() {
        stopAllSounds();
        soundVolumes.clear();
    }
}

