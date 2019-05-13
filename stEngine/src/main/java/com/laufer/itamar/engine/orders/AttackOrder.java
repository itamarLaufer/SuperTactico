package com.laufer.itamar.engine.orders;

import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Pieces.Piece;
import com.laufer.itamar.engine.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;



public class AttackOrder extends Order{
    private static int ORDER_ID = 1;
    private Piece target;
    private Boolean result = null;
    public AttackOrder(Piece actor, Location location) {
        super(actor, location, ORDER_ID);
        this.target = game.getPieceFromBoard(location);
    }

    @Override
    public void execute() {
        Location targetLocation = target.getLocation();
        result = actor.attack(target);
        if(result && actor.getLocation() != Location.OUT_LOCATION) // Todo there's gonna be a bug when it's a tie
            actor.move(targetLocation);
    }

    @Override
    public JSONObject orderDelta(Player player) {
        JSONObject res = new JSONObject();
        JSONArray pieces = new JSONArray();
        pieces.add(actor.visibleParseJson()); //always visible in a battle
        pieces.add(target.visibleParseJson()); //always visible in a battle
        res.put("pieces", pieces);
        if(result == null)
            throw new IllegalArgumentException("Must execute the order before retrieving delta!");
        if(actor.getOwner() == player)
            res.put("battleResult", result);
        else
            res.put("battleResult", !result);
        return res;
    }
}
