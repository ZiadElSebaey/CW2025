package com.comp2042.ui;

import javafx.scene.text.Font;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FontLoader Tests")
class FontLoaderTest {

    @Test
    @DisplayName("FontLoader - loadFont returns font for existing font file")
    void testLoadFontExisting() {
        Font font = FontLoader.loadFont("digital.ttf", 24);
        assertNotNull(font);
        assertEquals(24, font.getSize(), 0.1);
    }

    @Test
    @DisplayName("FontLoader - loadFont returns default font for non-existent font")
    void testLoadFontNonExistent() {
        Font font = FontLoader.loadFont("nonexistent_font.ttf", 16);
        assertNotNull(font);
        assertEquals(16, font.getSize(), 0.1);
    }

    @Test
    @DisplayName("FontLoader - loadFont handles different sizes")
    void testLoadFontDifferentSizes() {
        Font font12 = FontLoader.loadFont("digital.ttf", 12);
        Font font24 = FontLoader.loadFont("digital.ttf", 24);
        Font font48 = FontLoader.loadFont("digital.ttf", 48);
        
        assertNotNull(font12);
        assertNotNull(font24);
        assertNotNull(font48);
        
        assertEquals(12, font12.getSize(), 0.1);
        assertEquals(24, font24.getSize(), 0.1);
        assertEquals(48, font48.getSize(), 0.1);
    }

    @Test
    @DisplayName("FontLoader - loadFont handles null font file")
    void testLoadFontNullFile() {
        Font font = FontLoader.loadFont(null, 16);
        assertNotNull(font);
        assertEquals(16, font.getSize(), 0.1);
    }

    @Test
    @DisplayName("FontLoader - loadFont handles empty font file")
    void testLoadFontEmptyFile() {
        Font font = FontLoader.loadFont("", 16);
        assertNotNull(font);
        assertEquals(16, font.getSize(), 0.1);
    }
}
