package com.comp2042.logic;

import com.comp2042.ui.ViewData;

/**
 * Immutable record containing data returned after a brick moves down.
 * Includes information about cleared rows and the updated view state.
 * 
 * @param clearRow information about cleared rows, or {@code null} if no rows were cleared
 * @param viewData the updated view data after the move
 * 
 * @author CW2025 Team
 * @version 1.0
 * @since 1.0
 * @see ClearRow
 * @see com.comp2042.ui.ViewData
 */
public record DownData(ClearRow clearRow, ViewData viewData) {

    /**
     * Gets the cleared row information.
     * 
     * @return the ClearRow object, or {@code null} if no rows were cleared
     */
    public ClearRow getClearRow() {
        return clearRow;
    }

    /**
     * Gets the updated view data.
     * 
     * @return the view data after the move
     */
    public ViewData getViewData() {
        return viewData;
    }
}
