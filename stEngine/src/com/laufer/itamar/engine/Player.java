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
        createPieces();
    }

    /**
     * Creates the livingPieces list of the player with all the default livingPieces on the default locations
     */
    private void createPieces() {
        livingPieces = new ArrayList<>(60);
        //Todo init livingPieces on default locs(lower side of map), id should be calculated in this formula piece.id = (player.id + 1)*counter
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
}
