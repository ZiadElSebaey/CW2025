package com.comp2042.logic;

import com.comp2042.ui.ViewData;

public record DownData(ClearRow clearRow, ViewData viewData) {

    public ClearRow getClearRow() {
        return clearRow;
    }

    public ViewData getViewData() {
        return viewData;
    }
}
