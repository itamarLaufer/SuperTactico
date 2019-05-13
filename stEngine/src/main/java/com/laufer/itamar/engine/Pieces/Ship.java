package com.laufer.itamar.engine.Pieces;

import com.laufer.itamar.engine.Loads.Loads;
import com.laufer.itamar.engine.LocType;
import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Movings.SingleStepMove;
import com.laufer.itamar.engine.Player;
import com.laufer.itamar.engine.SuperTacticoGame;

public abstract class Ship extends Piece{
    public Ship(SuperTacticoGame game, Player owner, Location location, Loads loads, int id) {
        super(game, location, LocType.SEA, new SingleStepMove(), loads, owner, id);
    }

    @Override
    public boolean attack(Soldier soldier) {
        return false; // impossible
    }
    public boolean attack(SeaSapper seaSapper){
        die();
        return false;
    }

    @Override
    public boolean attack(Plane plane) {
        plane.die();
        return true;
    }
}
