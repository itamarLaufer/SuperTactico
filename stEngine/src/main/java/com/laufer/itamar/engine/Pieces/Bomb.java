package com.laufer.itamar.engine.Pieces;

import com.laufer.itamar.engine.*;
import com.laufer.itamar.engine.Loads.CannotLoadLoads;
import com.laufer.itamar.engine.Movings.NotMove;

public abstract class Bomb extends Piece {
    public Bomb(SuperTacticoGame game, Player owner, Location location, LocType locType, int id) {
        super(game, location, locType, new NotMove(), new CannotLoadLoads(), owner, id);
    }

    @Override
    public BattleResult attack(Piece other) {
        return null;
    } //cannot attack

    @Override
    public boolean canAttack(Piece other) {
        return false;
    }

    @Override
    public BattleResult attack(Plane plane) {
        return null; // cannot attack
    }

    @Override
    public BattleResult attack(Ship ship) {
        return null; // cannot attack
    }

    @Override
    public BattleResult attack(Soldier soldier) {
        return null; // cannot attack
    }

}
