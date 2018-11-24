package com.laufer.itamar.engine.orders;

import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Pieces.Piece;

public class UnloadOrder extends Order {


    public UnloadOrder(Piece actor, Location location, String type) {
        super(actor, location, type);
    }

    @Override
    public void execute() {
        actor.getLoader().unload(actor, location);
    }
}
