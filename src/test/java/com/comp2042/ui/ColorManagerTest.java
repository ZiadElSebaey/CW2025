package com.comp2042.ui;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ColorManager Tests")
class ColorManagerTest {

    @Test
    @DisplayName("ColorManager - getFillColor maps color indices correctly")
    void testGetFillColorMapping() {
        assertTrue(ColorManager.getFillColor(0) instanceof Color);
        assertTrue(ColorManager.getFillColor(1) instanceof Color);
        assertTrue(ColorManager.getFillColor(2) instanceof Color);
        assertTrue(ColorManager.getFillColor(3) instanceof Color);
        assertTrue(ColorManager.getFillColor(4) instanceof Color);
        assertTrue(ColorManager.getFillColor(5) instanceof Color);
        assertTrue(ColorManager.getFillColor(6) instanceof Color);
        assertTrue(ColorManager.getFillColor(7) instanceof Color);
        
        Color color0 = (Color) ColorManager.getFillColor(0);
        Color color1 = (Color) ColorManager.getFillColor(1);
        Color color2 = (Color) ColorManager.getFillColor(2);
        Color color3 = (Color) ColorManager.getFillColor(3);
        Color color4 = (Color) ColorManager.getFillColor(4);
        Color color5 = (Color) ColorManager.getFillColor(5);
        Color color6 = (Color) ColorManager.getFillColor(6);
        Color color7 = (Color) ColorManager.getFillColor(7);
        
        assertEquals(Color.TRANSPARENT.getRed(), color0.getRed(), 0.001);
        assertEquals(Color.TRANSPARENT.getOpacity(), color0.getOpacity(), 0.001);
        assertEquals(Color.AQUA, color1);
        assertEquals(Color.BLUEVIOLET, color2);
        assertEquals(Color.DARKGREEN, color3);
        assertEquals(Color.YELLOW, color4);
        assertEquals(Color.RED, color5);
        assertEquals(Color.BEIGE, color6);
        assertEquals(Color.BURLYWOOD, color7);
    }

    @Test
    @DisplayName("ColorManager - getFillColor returns WHITE for negative index")
    void testGetFillColorNegativeIndex() {
        Paint result = ColorManager.getFillColor(-1);
        assertEquals(Color.WHITE, result);
    }

    @Test
    @DisplayName("ColorManager - getFillColor returns WHITE for out of range index")
    void testGetFillColorOutOfRange() {
        Paint result = ColorManager.getFillColor(100);
        assertEquals(Color.WHITE, result);
    }

    @Test
    @DisplayName("ColorManager - getFillColor with 1984 mode returns WHITE for non-transparent")
    void testGetFillColor1984Mode() {
        Paint result1 = ColorManager.getFillColor(1, true);
        assertEquals(Color.WHITE, result1);
        
        Paint result2 = ColorManager.getFillColor(5, true);
        assertEquals(Color.WHITE, result2);
        
        Paint result3 = ColorManager.getFillColor(0, true);
        assertEquals(Color.TRANSPARENT, result3);
    }

    @Test
    @DisplayName("ColorManager - getFillColor without 1984 mode returns normal colors")
    void testGetFillColorNormalMode() {
        Paint result1 = ColorManager.getFillColor(1, false);
        assertEquals(Color.AQUA, result1);
        
        Paint result2 = ColorManager.getFillColor(5, false);
        assertEquals(Color.RED, result2);
    }

    @Test
    @DisplayName("ColorManager - createGhostColor with custom opacity")
    void testCreateGhostColorCustomOpacity() {
        Color original = Color.RED;
        Color ghost = ColorManager.createGhostColor(original, 0.5);
        
        assertEquals(original.getRed(), ghost.getRed(), 0.001);
        assertEquals(original.getGreen(), ghost.getGreen(), 0.001);
        assertEquals(original.getBlue(), ghost.getBlue(), 0.001);
        assertEquals(0.5, ghost.getOpacity(), 0.001);
    }

    @Test
    @DisplayName("ColorManager - createGhostColor with default opacity")
    void testCreateGhostColorDefaultOpacity() {
        Color original = Color.BLUE;
        Color ghost = ColorManager.createGhostColor(original);
        
        assertEquals(original.getRed(), ghost.getRed(), 0.001);
        assertEquals(original.getGreen(), ghost.getGreen(), 0.001);
        assertEquals(original.getBlue(), ghost.getBlue(), 0.001);
        assertEquals(AnimationConstants.GHOST_COLOR_OPACITY, ghost.getOpacity(), 0.001);
    }

    @Test
    @DisplayName("ColorManager - createGhostBorderColor uses border opacity")
    void testCreateGhostBorderColor() {
        Color original = Color.GREEN;
        Color border = ColorManager.createGhostBorderColor(original);
        
        assertEquals(original.getRed(), border.getRed(), 0.001);
        assertEquals(original.getGreen(), border.getGreen(), 0.001);
        assertEquals(original.getBlue(), border.getBlue(), 0.001);
        assertEquals(AnimationConstants.GHOST_BORDER_OPACITY, border.getOpacity(), 0.001);
    }

    @Test
    @DisplayName("ColorManager - createGhostColor preserves RGB values")
    void testCreateGhostColorPreservesRGB() {
        Color original = Color.rgb(100, 150, 200);
        Color ghost = ColorManager.createGhostColor(original, 0.3);
        
        assertEquals(100.0 / 255.0, ghost.getRed(), 0.001);
        assertEquals(150.0 / 255.0, ghost.getGreen(), 0.001);
        assertEquals(200.0 / 255.0, ghost.getBlue(), 0.001);
        assertEquals(0.3, ghost.getOpacity(), 0.001);
    }
}

