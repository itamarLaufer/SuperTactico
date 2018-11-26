package com.laufer.itamar.engine.orders;

import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Pieces.Piece;

public class LoadOrder extends Order {
    private static int ORDER_ID = 2;
    public LoadOrder(Piece actor, Location location) {
        super(actor, location, ORDER_ID);
    }

    @Override
    public void execute() {
        actor.load(game.getPieceFromBoard(location));
    }
}
