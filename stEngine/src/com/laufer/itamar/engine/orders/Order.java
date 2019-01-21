package com.laufer.itamar.engine.orders;

import com.laufer.itamar.JsonParsable;
import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Pieces.Piece;
import com.laufer.itamar.engine.Player;
import com.laufer.itamar.engine.SuperTacticoGame;
import org.json.simple.JSONObject;


public abstract class Order implements JsonParsable {

    protected SuperTacticoGame game;
    protected Piece actor;
    protected Location location;
    protected int id;

    public Order(Piece actor, Location location, int id) {
        this.game = actor.getGame();
        this.actor = actor;
        this.location = location;
        this.id = id;
    }

    /**
     * Executes this order
     * @return the pieces that this order changed something regarding to them
     */
    public abstract void execute();
    public abstract JSONObject orderDelta(Player player);

    @Override
    public JSONObject parseJson(String[]args) {
        JSONObject res = new JSONObject();
        res.put("location", location.parseJson());
        res.put("typeId", id);
        return res;
    }

    @Override
    public String toString() {
        return "Order{" +
                "actor=" + actor.getId() +
                ", location=" + location +
                ", id=" + id +
                '}';
    }
}
