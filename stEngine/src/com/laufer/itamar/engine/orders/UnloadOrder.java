package com.laufer.itamar.engine.orders;

import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Pieces.Piece;
import org.json.simple.JSONObject;

public class UnloadOrder extends Order {

    private static int ORDER_ID = 3;

    public UnloadOrder(Piece actor, Location location) {
        super(actor, location, ORDER_ID);
    }

    @Override
    public void execute() {
        actor.unload(location);
    }
    @Override
    public JSONObject parseJson() {
        JSONObject res = super.parseJson();
        res.put("type", "unload");
        return res;
    }
}
