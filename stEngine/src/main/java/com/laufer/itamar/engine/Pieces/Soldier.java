package com.laufer.itamar.engine.Pieces;

import com.laufer.itamar.engine.Loads.SoldierLoads;
import com.laufer.itamar.engine.LocType;
import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Movings.SingleStepMove;
import com.laufer.itamar.engine.Player;
import com.laufer.itamar.engine.SuperTacticoGame;

public abstract class Soldier extends Piece {
    public Soldier(SuperTacticoGame game, Player owner, Location location, int id) {
        super(game, location, LocType.LAND, new SingleStepMove(), new SoldierLoads(), owner, id);
    }

    public boolean attack(Plane plane) {
        plane.die();
        return true;
    }

    public boolean attack(LandSapper landSapper) {
        landSapper.die();
        return true;
    }

    public boolean hasFlag(){
        return getAllLoads().size() > 0;
    }
}
