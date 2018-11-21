package com.laufer.itamar.engine.Movings;

import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class MultipleStepsMove implements MoveType {


    @Override
    public List<Location> getPossibleMoveLocations(Piece piece) {
        List<Location>res = new ArrayList<>(Location.BOARD_SIZE * 2);
        int[]rowsA = {1, -1, 0, 0};
        int[]colsA = {0, 0, 1, -1};
        for(int i = 0; i < rowsA.length; i++) {
            Location loc = piece.getLocation();
            do {
                if (loc != piece.getLocation() && piece.basicCanMove(loc))
                    res.add(loc);
                loc = loc.add(rowsA[i], colsA[i]);
            }
            while (!loc.notInBoard() && (piece.getGame().getPieceFromBoard(loc) == null));
        }
        return res;
    }
}