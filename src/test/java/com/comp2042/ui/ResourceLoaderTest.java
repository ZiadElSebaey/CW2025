package com.comp2042.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ResourceLoader Tests")
class ResourceLoaderTest {

    @Test
    @DisplayName("ResourceLoader - getResource returns URL for existing resource")
    void testGetResourceExisting() {
        URL result = ResourceLoader.getResource("window_style.css");
        assertNotNull(result);
    }

    @Test
    @DisplayName("ResourceLoader - getResource returns null for non-existent resource")
    void testGetResourceNonExistent() {
        URL result = ResourceLoader.getResource("nonexistent_file.txt");
        assertNull(result);
    }

    @Test
    @DisplayName("ResourceLoader - getResource handles null path gracefully")
    void testGetResourceNullPath() {
        URL result = ResourceLoader.getResource(null);
        assertNull(result);
    }

    @Test
    @DisplayName("ResourceLoader - getResource handles empty path")
    void testGetResourceEmptyPath() {
        URL result = ResourceLoader.getResource("");
        assertNull(result);
    }

    @Test
    @DisplayName("ResourceLoader - getResource with valid FXML file")
    void testGetResourceFXML() {
        URL result = ResourceLoader.getResource("mainMenu.fxml");
        assertNotNull(result);
    }
}

