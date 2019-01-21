package com.laufer.itamar.engine.orders;

import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Pieces.Piece;
import com.laufer.itamar.engine.Player;
import com.laufer.itamar.org.json.simple.JSONArray;
import com.laufer.itamar.org.json.simple.JSONObject;


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
        result = actor.attack(target);
        //Todo needs to send back to gui to show in nice interface the participants in the attack and the results
    }

    @Override
    public JSONObject orderDelta(Player player) {
        JSONObject res = new JSONObject();
        JSONArray pieces = new JSONArray();
        pieces.add(actor.parseJson(new String[]{String.valueOf(actor.getOwner().getId())})); //always visible in a battle
        pieces.add(target.parseJson(new String[]{String.valueOf(target.getOwner().getId())})); //always visible in a battle
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
