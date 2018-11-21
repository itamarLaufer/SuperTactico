package com.laufer.itamar.engine.Movings;

import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Pieces.Piece;

import java.util.List;
import java.util.stream.Collectors;

public class SingleStepMove implements MoveType {


    @Override
    public List<Location> getPossibleMoveLocations(Piece piece) {
        return piece.getLocation().touchingLocations().stream().filter(piece::basicCanMove).collect(Collectors.toList());
    }
}