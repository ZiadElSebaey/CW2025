package com.comp2042.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GameMode Tests")
class GameModeTest {

    @Test
    @DisplayName("GameMode - getValue returns correct string")
    void testGetValue() {
        assertEquals("normal", GameMode.NORMAL.getValue());
        assertEquals("inverted", GameMode.INVERTED.getValue());
        assertEquals("1984", GameMode.MODE_1984.getValue());
    }

    @Test
    @DisplayName("GameMode - fromString converts valid strings")
    void testFromStringValid() {
        assertEquals(GameMode.NORMAL, GameMode.fromString("normal"));
        assertEquals(GameMode.INVERTED, GameMode.fromString("inverted"));
        assertEquals(GameMode.MODE_1984, GameMode.fromString("1984"));
    }

    @Test
    @DisplayName("GameMode - fromString returns NORMAL for null")
    void testFromStringNull() {
        assertEquals(GameMode.NORMAL, GameMode.fromString(null));
    }

    @Test
    @DisplayName("GameMode - fromString returns NORMAL for invalid string")
    void testFromStringInvalid() {
        assertEquals(GameMode.NORMAL, GameMode.fromString("invalid"));
        assertEquals(GameMode.NORMAL, GameMode.fromString(""));
        assertEquals(GameMode.NORMAL, GameMode.fromString("random"));
    }

    @Test
    @DisplayName("GameMode - matches returns true for correct value")
    void testMatchesTrue() {
        assertTrue(GameMode.NORMAL.matches("normal"));
        assertTrue(GameMode.INVERTED.matches("inverted"));
        assertTrue(GameMode.MODE_1984.matches("1984"));
    }

    @Test
    @DisplayName("GameMode - matches returns false for incorrect value")
    void testMatchesFalse() {
        assertFalse(GameMode.NORMAL.matches("inverted"));
        assertFalse(GameMode.INVERTED.matches("1984"));
        assertFalse(GameMode.MODE_1984.matches("normal"));
    }

    @Test
    @DisplayName("GameMode - matches returns false for null")
    void testMatchesNull() {
        assertFalse(GameMode.NORMAL.matches(null));
        assertFalse(GameMode.INVERTED.matches(null));
        assertFalse(GameMode.MODE_1984.matches(null));
    }

    @Test
    @DisplayName("GameMode - all enum values are present")
    void testAllEnumValues() {
        GameMode[] values = GameMode.values();
        assertEquals(3, values.length);
        assertTrue(java.util.Arrays.asList(values).contains(GameMode.NORMAL));
        assertTrue(java.util.Arrays.asList(values).contains(GameMode.INVERTED));
        assertTrue(java.util.Arrays.asList(values).contains(GameMode.MODE_1984));
    }

    @Test
    @DisplayName("GameMode - valueOf works correctly")
    void testValueOf() {
        assertEquals(GameMode.NORMAL, GameMode.valueOf("NORMAL"));
        assertEquals(GameMode.INVERTED, GameMode.valueOf("INVERTED"));
        assertEquals(GameMode.MODE_1984, GameMode.valueOf("MODE_1984"));
    }
}
