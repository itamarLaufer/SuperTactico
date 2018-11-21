package com.laufer.itamar.engine.Pieces;

import com.laufer.itamar.engine.*;
import com.laufer.itamar.engine.Loads.Loads;
import com.laufer.itamar.engine.Movings.MultipleStepsMove;

public abstract class Plane extends Piece {
    public Plane(SuperTacticoGame game, Player owner, Location location, Loads loads, int id) {
        super(game, location, LocType.ALL, new MultipleStepsMove(), loads, owner, id);
    }
    public boolean attack(Ship ship){
        die();
        return false;
    }
    public boolean attack(Soldier soldier){
        die();
        return false;
    }
    public AttackResult attack(Bomb bomb){
        die();
        return AttackResult.DEFEAT;
    }
}
