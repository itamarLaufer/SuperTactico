package com.laufer.itamar.engine;


import com.laufer.itamar.engine.Pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int id;
    private String name;
    private List<Piece> livingPieces;

    /**
     * Creates a player with the default stack of livingPieces
     * @param name the name of the player to create
     * @param id the id number of the player to create (0 or 1)
     */
    public Player(String name, int id){
        this.name = name;
        this.id = id;
    }

    public List<Piece> getLivingPieces() {
        return livingPieces;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * Removes the given piece from the player livingPieces list
     * @param piece to remove
     */
    public void removePiece(Piece piece) {
        livingPieces.remove(piece);
    }
    /**
     * Adds the given piece to the player livingPieces list
     * @param piece to add
     */
    public void addPiece(Piece piece){livingPieces.add(piece);}
}
