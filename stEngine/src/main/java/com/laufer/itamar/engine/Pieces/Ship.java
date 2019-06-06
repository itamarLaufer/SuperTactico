package com.laufer.itamar.engine.Pieces;

import com.laufer.itamar.engine.*;
import com.laufer.itamar.engine.Loads.Loads;
import com.laufer.itamar.engine.Movings.SingleStepMove;

public abstract class Ship extends Piece{
    public Ship(SuperTacticoGame game, Player owner, Location location, Loads loads, int id) {
        super(game, location, LocType.SEA, new SingleStepMove(), loads, owner, id);
    }

    @Override
    public BattleResult attack(Soldier soldier) {
        return null; // impossible
    }
    public BattleResult attack(SeaSapper seaSapper){
        die();
        return BattleResult.DEFEAT;
    }

    @Override
    public BattleResult attack(Plane plane) {
        plane.die();
        return BattleResult.VICTORY;
    }
}
