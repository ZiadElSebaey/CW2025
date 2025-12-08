package com.comp2042.ui;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Manages color mapping for game bricks and UI elements.
 */
public final class ColorManager {
    
    private ColorManager() {}
    
    /**
     * Maps a color index to a Paint color.
     * 
     * @param colorIndex The color index (0 = transparent, 1-7 = brick colors)
     * @param is1984Mode If true, non-transparent colors return WHITE (1984 mode uses white bricks)
     * @return The corresponding Paint color
     */
    public static Paint getFillColor(int colorIndex, boolean is1984Mode) {
        if (colorIndex < 0) {
            return Color.WHITE;
        }
        
        if (is1984Mode && colorIndex != 0) {
            return Color.WHITE;
        }
        
        return switch (colorIndex) {
            case 0 -> Color.TRANSPARENT;
            case 1 -> Color.AQUA;
            case 2 -> Color.BLUEVIOLET;
            case 3 -> Color.DARKGREEN;
            case 4 -> Color.YELLOW;
            case 5 -> Color.RED;
            case 6 -> Color.BEIGE;
            case 7 -> Color.BURLYWOOD;
            default -> Color.WHITE;
        };
    }
    
    /**
     * Maps a color index to a Paint color (default mode, not 1984).
     * 
     * @param colorIndex The color index (0 = transparent, 1-7 = brick colors)
     * @return The corresponding Paint color
     */
    public static Paint getFillColor(int colorIndex) {
        return getFillColor(colorIndex, false);
    }
    
    /**
     * Creates a ghost color with reduced opacity.
     * 
     * @param originalColor The original color
     * @param opacity The opacity level (0.0 to 1.0)
     * @return A new Color with the same RGB but adjusted opacity
     */
    public static Color createGhostColor(Color originalColor, double opacity) {
        return Color.color(originalColor.getRed(), originalColor.getGreen(), originalColor.getBlue(), opacity);
    }
    
    /**
     * Creates a ghost color with default ghost opacity.
     * 
     * @param originalColor The original color
     * @return A new Color with ghost opacity
     */
    public static Color createGhostColor(Color originalColor) {
        return createGhostColor(originalColor, AnimationConstants.GHOST_COLOR_OPACITY);
    }
    
    /**
     * Creates a ghost border color with default border opacity.
     * 
     * @param originalColor The original color
     * @return A new Color with ghost border opacity
     */
    public static Color createGhostBorderColor(Color originalColor) {
        return createGhostColor(originalColor, AnimationConstants.GHOST_BORDER_OPACITY);
    }
}

