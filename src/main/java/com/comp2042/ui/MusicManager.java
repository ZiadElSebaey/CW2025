package com.comp2042.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;

/**
 * Manages background music playback for the game.
 * Handles loading, playing, pausing, and switching between music tracks.
 * Supports special looping behavior for "Russia Music" track (17.5 second loop).
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 * @see SettingsManager
 */
public final class MusicManager {
    
    private static MediaPlayer mediaPlayer;
    private static boolean isPlaying = false;
    private static String currentTrack = "Russia Music";
    private static Timeline russiaMusicLoopTimeline;
    private static final double RUSSIA_MUSIC_DURATION = 17.5;
    
    private MusicManager() {}
    
    public static void initialize() {
        String savedTrack = SettingsManager.getSelectedMusicTrack();
        if (savedTrack != null && !savedTrack.isEmpty()) {
            currentTrack = savedTrack;
        }
        loadTrack(currentTrack);
    }
    
    public static void loadTrack(String trackName) {
        String resourcePath;
        boolean isRussiaMusic = false;
        if ("Christmas".equals(trackName)) {
            resourcePath = "sfx/backgroundchristimasmusic.mp3";
        } else {
            resourcePath = "sfx/Russiamusic.mp3";
            trackName = "Russia Music";
            isRussiaMusic = true;
        }
        
        boolean wasPlaying = isPlaying;
        stop();
        stopRussiaMusicLoop();
        
        URL musicUrl = MusicManager.class.getClassLoader().getResource(resourcePath);
        if (musicUrl != null) {
            try {
                if (mediaPlayer != null) {
                    mediaPlayer.dispose();
                }
                Media media = new Media(musicUrl.toExternalForm());
                mediaPlayer = new MediaPlayer(media);
                
                if (isRussiaMusic) {
                    mediaPlayer.setCycleCount(1);
                    setupRussiaMusicLoop();
                } else {
                    mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                }
                
                mediaPlayer.setVolume(0.5);
                currentTrack = trackName;
                
                if (wasPlaying && SettingsManager.isMusicEnabled()) {
                    play();
                }
            } catch (Exception e) {
                System.err.println("Failed to load background music: " + e.getMessage());
            }
        }
    }
    
    private static void setupRussiaMusicLoop() {
        stopRussiaMusicLoop();
        russiaMusicLoopTimeline = new Timeline(
            new KeyFrame(Duration.seconds(RUSSIA_MUSIC_DURATION), e -> {
                if (mediaPlayer != null && isPlaying && "Russia Music".equals(currentTrack)) {
                    mediaPlayer.stop();
                    mediaPlayer.seek(Duration.ZERO);
                    mediaPlayer.play();
                }
            })
        );
        russiaMusicLoopTimeline.setCycleCount(Timeline.INDEFINITE);
    }
    
    private static void stopRussiaMusicLoop() {
        if (russiaMusicLoopTimeline != null) {
            russiaMusicLoopTimeline.stop();
            russiaMusicLoopTimeline = null;
        }
    }
    
    public static String getCurrentTrack() {
        return currentTrack;
    }
    
    public static void play() {
        if (mediaPlayer != null) {
            if (!isPlaying) {
                mediaPlayer.play();
                isPlaying = true;
                if ("Russia Music".equals(currentTrack)) {
                    if (russiaMusicLoopTimeline == null) {
                        setupRussiaMusicLoop();
                    }
                    russiaMusicLoopTimeline.play();
                }
            }
        }
    }
    
    public static void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            isPlaying = false;
        }
        stopRussiaMusicLoop();
    }
    
    public static void pause() {
        if (mediaPlayer != null && isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
        }
    }
    
    public static boolean isPlaying() {
        return isPlaying;
    }
    
    public static void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(Math.max(0.0, Math.min(1.0, volume)));
        }
    }
    
    public static void dispose() {
        stopRussiaMusicLoop();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
            isPlaying = false;
        }
    }
}

