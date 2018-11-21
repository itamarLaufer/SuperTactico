package com.laufer.itamar.communication.gui;

import com.laufer.itamar.engine.LocType;

public class GuiSquare {
    private LocType locType;
    private GuiPiece piece;

    public GuiSquare(LocType locType, GuiPiece piece) {
        this.locType = locType;
        this.piece = piece;
    }
}
