package com.laufer.itamar.engine;

import com.laufer.itamar.engine.Pieces.Piece;
import com.laufer.itamar.org.json.simple.JSONObject;

public class Square {
    private LocType locType;
    private Piece piece;

    public Square(LocType locType, Piece piece) {
        this.locType = locType;
        this.piece = piece;
    }

    public Square(LocType locType) {
        this.locType = locType;
        this.piece = null;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public LocType getLocType() {
        return locType;
    }

    @Override
    public String toString() {
        String p = piece == null?"N":"" + piece.getType();
        return "{" +
                "" + locType +
                "," + p +
                '}';
    }
}
