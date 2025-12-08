package com.comp2042.ui;

import javafx.scene.text.Font;

import java.io.InputStream;
import java.net.URL;

/**
 * Utility class for loading fonts from resources.
 */
public final class FontLoader {
    
    private FontLoader() {}
    
    /**
     * Loads a font from a resource file.
     * 
     * @param fontPath The path to the font file (e.g., "FROZENLAND.otf")
     * @param size The size of the font
     * @return The loaded Font, or null if loading failed
     */
    public static Font loadFont(String fontPath, double size) {
        if (fontPath == null || fontPath.isEmpty()) {
            return Font.font("System", size);
        }
        try {
            InputStream fontStream = ResourceLoader.getResourceAsStream(fontPath);
            if (fontStream != null) {
                Font loadedFont = Font.loadFont(fontStream, size);
                if (loadedFont != null) {
                    return loadedFont;
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load font '" + fontPath + "': " + e.getMessage());
        }
        return Font.font("System", size);
    }
    
    /**
     * Loads a font from a resource URL.
     * 
     * @param fontPath The path to the font file (e.g., "digital.ttf")
     * @param size The size of the font
     * @return The loaded Font, or null if loading failed
     */
    public static Font loadFontFromUrl(String fontPath, double size) {
        if (fontPath == null || fontPath.isEmpty()) {
            return Font.font("System", size);
        }
        try {
            URL fontUrl = ResourceLoader.getResource(fontPath);
            if (fontUrl != null) {
                Font loadedFont = Font.loadFont(fontUrl.toExternalForm(), size);
                if (loadedFont != null) {
                    return loadedFont;
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load font from URL '" + fontPath + "': " + e.getMessage());
        }
        return Font.font("System", size);
    }
}

