package com.laufer.itamar.engine.orders;

import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Pieces.Piece;
import org.json.simple.JSONObject;

public class AttackOrder extends Order{
    private static int ORDER_ID = 1;
    public AttackOrder(Piece actor, Location location) {
        super(actor, location, ORDER_ID);
    }

    @Override
    public void execute() {
        actor.attack(game.getPieceFromBoard(location));
        //Todo needs to send back to gui to show in nice interface the participants in the attack and the results
    }

    @Override
    public JSONObject parseJson() {
        JSONObject res = super.parseJson();
        res.put("type", "attack");
        return res;
    }
}
