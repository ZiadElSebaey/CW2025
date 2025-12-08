package com.comp2042.ui;

/**
 * Animation-related constants.
 */
public final class AnimationConstants {
    
    // Rotation
    public static final double INVERTED_ROTATION_ANGLE = 180.0;
    
    // Hold block animation
    public static final double HOLD_SCALE_UP = 1.15;
    public static final int HOLD_SCALE_DURATION_UP_MS = 200;
    public static final int HOLD_SCALE_DURATION_DOWN_MS = 300;
    
    // Ghost block
    public static final double GHOST_COLOR_OPACITY = 0.25;
    public static final double GHOST_BORDER_OPACITY = 0.4;
    public static final int GHOST_FADE_IN_DURATION_MS = 100;
    public static final int GHOST_FADE_OUT_DURATION_MS = 80;
    public static final double GHOST_STROKE_WIDTH = 1.5;
    public static final double GHOST_VISIBILITY_THRESHOLD = 0.01;
    
    // Game timing
    public static final int DEFAULT_DROP_INTERVAL_MS = 400;
    
    private AnimationConstants() {}
}

