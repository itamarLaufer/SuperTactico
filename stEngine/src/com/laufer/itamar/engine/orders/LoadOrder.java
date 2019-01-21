package com.laufer.itamar.engine.orders;

import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Pieces.Piece;
import com.laufer.itamar.engine.Player;
import com.laufer.itamar.org.json.simple.JSONObject;
import com.laufer.itamar.org.json.simple.JSONArray;
import com.laufer.itamar.org.json.simple.JSONObject;

public class LoadOrder extends Order {
    private static int ORDER_ID = 2;
    private Piece loaded;
    public LoadOrder(Piece actor, Location location) {
        super(actor, location, ORDER_ID);
        this.loaded = game.getPieceFromBoard(location);

    }

    @Override
    public void execute() {
        actor.load(loaded);
    }

    @Override
    public JSONObject orderDelta(Player player) {
        JSONObject res = new JSONObject();
        JSONArray pieces = new JSONArray();
        pieces.add(actor.parseJson(new String[]{String.valueOf(player.getId())}));
        pieces.add(loaded.parseJson(new String[]{String.valueOf(player.getId())}));
        res.put("pieces", pieces);
        return res;
    }
}
