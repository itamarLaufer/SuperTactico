package com.laufer.itamar.engine.orders;

import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Pieces.Piece;
import com.laufer.itamar.engine.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MoveOrder extends Order {
    private static int ORDER_ID = 0;
    public MoveOrder(Piece actor, Location location) {
        super(actor, location, ORDER_ID);
    }

    @Override
    public void execute() {
        actor.move(location);
    }

    @Override
    public JSONObject orderDelta(Player player) {
        JSONObject res = new JSONObject();
        JSONArray pieces = new JSONArray();
        pieces.add(actor.parseJson(new String[]{String.valueOf(player.getId())}));
        res.put("pieces", pieces);
        return res;
    }
}