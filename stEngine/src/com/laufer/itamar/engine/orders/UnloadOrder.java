package com.laufer.itamar.engine.orders;

import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Pieces.Piece;

public class UnloadOrder extends Order {

    private static int ORDER_ID = 3;

    public UnloadOrder(Piece actor, Location location) {
        super(actor, location, ORDER_ID);
    }

    @Override
    public Piece[] execute() {
        Piece loader = actor.getLoader();
        actor.unload(location);
        return new Piece[]{actor, loader};
    }
}
