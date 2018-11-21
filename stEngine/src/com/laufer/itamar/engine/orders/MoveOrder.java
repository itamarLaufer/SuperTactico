package com.laufer.itamar.engine.orders;

import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Pieces.Piece;

public class MoveOrder extends Order {
    private static String ORDER_NAME = "move";
    public MoveOrder(Piece actor, Location location) {
        super(actor, location, ORDER_NAME);
    }

    @Override
    public void execute() {
        actor.move(location);
    }
}
