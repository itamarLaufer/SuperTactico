package com.laufer.itamar.engine.Movings;

import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Pieces.Piece;

import java.util.LinkedList;
import java.util.List;

public class NotMove implements MoveType {


    @Override
    public List<Location> getPossibleMoveLocations(Piece piece) {
        return new LinkedList<>(); //empty, cannot move at all
    }
}
