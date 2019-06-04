package com.laufer.itamar.engine.orders;

import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Pieces.Piece;
import com.laufer.itamar.engine.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class LoadOrder extends Order {
    private static int ORDER_ID = 2;
    private Piece loader;
    public LoadOrder(Piece actor, Location location) {
        super(actor, location, ORDER_ID);
        this.loader = game.getPieceFromBoard(location);

    }

    @Override
    public void execute() {
        loader.load(actor);
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
