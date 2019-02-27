package com.laufer.itamar.engine.orders;

import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Pieces.Piece;
import com.laufer.itamar.engine.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class UnloadOrder extends Order {
    private static int ORDER_ID = 3;
    private Piece loader;

    public UnloadOrder(Piece actor, Location location) {
        super(actor, location, ORDER_ID);
        this.loader = actor.getLoader();
    }

    @Override
    public void execute() {
        actor.unload(location);
    }
    @Override
    public JSONObject orderDelta(Player player) {
        JSONObject res = new JSONObject();
        JSONArray pieces = new JSONArray();
        pieces.add(actor.parseJson());
        pieces.add(loader.parseJson());
        res.put("pieces", pieces);
        return res;
    }
}
