package com.laufer.itamar.engine.orders;

import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Pieces.Piece;

public class AttackOrder extends Order{
    private static int ORDER_ID = 1;
    public AttackOrder(Piece actor, Location location) {
        super(actor, location, ORDER_ID);
    }

    @Override
    public Piece[] execute() {
        Piece target = game.getPieceFromBoard(location);
        actor.attack(target);
        return new Piece[]{actor, target};
        //Todo needs to send back to gui to show in nice interface the participants in the attack and the results
    }
}
