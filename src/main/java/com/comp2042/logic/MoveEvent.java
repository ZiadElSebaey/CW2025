package com.comp2042.logic;

/**
 * Immutable record representing a game input event.
 * Contains information about the type of move and its source.
 * 
 * @param eventType the type of movement (DOWN, LEFT, RIGHT, ROTATE)
 * @param eventSource the source of the event (USER or THREAD)
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 * @see EventType
 * @see EventSource
 */
public record MoveEvent(EventType eventType, EventSource eventSource) {

    /**
     * Gets the type of the event.
     * 
     * @return the event type
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Gets the source of the event.
     * 
     * @return the event source
     */
    public EventSource getEventSource() {
        return eventSource;
    }
}
