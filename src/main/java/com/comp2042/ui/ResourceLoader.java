package com.comp2042.ui;

import java.io.InputStream;
import java.net.URL;

/**
 * Utility class for loading resources from the classpath.
 */
public final class ResourceLoader {
    
    private ResourceLoader() {}
    
    /**
     * Gets a resource URL from the classpath.
     * 
     * @param resourcePath The path to the resource (e.g., "mainMenu.fxml")
     * @return The URL of the resource, or null if not found
     */
    public static URL getResource(String resourcePath) {
        if (resourcePath == null || resourcePath.isEmpty()) {
            return null;
        }
        return ResourceLoader.class.getClassLoader().getResource(resourcePath);
    }
    
    /**
     * Gets a resource as an InputStream from the classpath.
     * 
     * @param resourcePath The path to the resource (e.g., "FROZENLAND.otf")
     * @return The InputStream of the resource, or null if not found
     */
    public static InputStream getResourceAsStream(String resourcePath) {
        if (resourcePath == null || resourcePath.isEmpty()) {
            return null;
        }
        return ResourceLoader.class.getClassLoader().getResourceAsStream(resourcePath);
    }
}

