package com.comp2042.ui;

/**
 * Enumeration of available game modes.
 */
public enum GameMode {
    NORMAL("normal"),
    INVERTED("inverted"),
    MODE_1984("1984");
    
    private final String value;
    
    GameMode(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    /**
     * Converts a string value to GameMode enum.
     * 
     * @param value The string value (e.g., "1984", "inverted", "normal")
     * @return The corresponding GameMode, or NORMAL if not found
     */
    public static GameMode fromString(String value) {
        if (value == null) {
            return NORMAL;
        }
        for (GameMode mode : GameMode.values()) {
            if (mode.value.equals(value)) {
                return mode;
            }
        }
        return NORMAL;
    }
    
    /**
     * Checks if a string value matches this game mode.
     * 
     * @param value The string value to check
     * @return true if the value matches this mode
     */
    public boolean matches(String value) {
        return this.value.equals(value);
    }
}

