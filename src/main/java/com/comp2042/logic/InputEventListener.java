package com.comp2042.logic;

import com.comp2042.ui.ViewData;

/**
 * Interface for handling game input events.
 * Defines methods for processing user and automatic game actions.
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 * @see GameController
 */
public interface InputEventListener {

    /**
     * Handles a downward movement event.
     * 
     * @param event the move event containing type and source information
     * @return data about cleared rows and updated view state
     */
    DownData onDownEvent(MoveEvent event);

    /**
     * Handles a leftward movement event.
     * 
     * @param event the move event
     * @return the updated view data
     */
    ViewData onLeftEvent(MoveEvent event);

    /**
     * Handles a rightward movement event.
     * 
     * @param event the move event
     * @return the updated view data
     */
    ViewData onRightEvent(MoveEvent event);

    /**
     * Handles a rotation event.
     * 
     * @param event the move event
     * @return the updated view data
     */
    ViewData onRotateEvent(MoveEvent event);

    /**
     * Creates and initializes a new game.
     * 
     * @return the initial view data for the new game
     */
    ViewData createNewGame();
    
    /**
     * Handles a hard drop event (instant drop to bottom).
     * 
     * @return data about cleared rows and updated view state
     */
    DownData onHardDropEvent();
    
    /**
     * Handles a hold brick event.
     * 
     * @return the updated view data after holding
     */
    ViewData onHoldEvent();
}
