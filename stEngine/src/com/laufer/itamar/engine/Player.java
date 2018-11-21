package com.laufer.itamar.engine;


import com.laufer.itamar.engine.Pieces.Piece;

public class Player {
    private int id;
    private String name;
    private Piece[] pieces;

    public Player(String name, int id){
        this.name = name;
        this.id = id;
    }

    public Piece[] getPieces() {
        return pieces;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
