package com.laufer.itamar.engine.orders;

import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Pieces.Piece;

public class AttackOrder extends Order{
    private static String ORDER_NAME = "attack";
    public AttackOrder(Piece actor, Location location) {
        super(actor, location, ORDER_NAME);
    }

    @Override
    public void execute() {
        actor.attack(game.getPieceFromBoard(location));
        //Todo needs to send back to gui to show in nice interface the participants in the attack and the results
    }
}
