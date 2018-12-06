package com.laufer.itamar.engine.orders;

import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Pieces.Piece;

public class MoveOrder extends Order {
    private static int ORDER_ID = 0;
    public MoveOrder(Piece actor, Location location) {
        super(actor, location, ORDER_ID);
    }

    @Override
    public void execute() {
        actor.move(location);
    }
}
