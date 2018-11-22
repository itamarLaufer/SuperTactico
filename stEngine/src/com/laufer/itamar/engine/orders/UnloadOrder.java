package com.laufer.itamar.engine.orders;

import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Pieces.Piece;

public class UnloadOrder extends Order {

    private Piece toUnload;

    public UnloadOrder(Piece actor, Location location, String type, Piece toUnload) {
        super(actor, location, type);
        this.toUnload = toUnload;
    }

    @Override
    public void execute() {
        actor.unload(toUnload, location);
    }
}
