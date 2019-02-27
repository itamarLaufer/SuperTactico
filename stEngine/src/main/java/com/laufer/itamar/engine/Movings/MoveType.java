package com.laufer.itamar.engine.Movings;

import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Pieces.Piece;

import java.util.List;

public interface MoveType {
    List<Location> getPossibleMoveLocations(Piece piece);

}
