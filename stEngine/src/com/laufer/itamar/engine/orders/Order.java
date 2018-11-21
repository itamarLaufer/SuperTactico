package com.laufer.itamar.engine.orders;

import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Pieces.Piece;
import com.laufer.itamar.engine.SuperTacticoGame;

public abstract class Order {

    protected SuperTacticoGame game;
    protected Piece actor;
    protected Location location;
    protected String type;

    public Order(Piece actor, Location location, String type) {
        this.game = actor.getGame();
        this.actor = actor;
        this.location = location;
        this.type = type;
    }
    public abstract void execute();
}
