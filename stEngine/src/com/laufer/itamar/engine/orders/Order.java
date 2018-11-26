package com.laufer.itamar.engine.orders;

import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Pieces.Piece;
import com.laufer.itamar.engine.SuperTacticoGame;

public abstract class Order {

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
    public abstract void execute();
}
