package com.laufer.itamar.engine.Pieces;

import com.laufer.itamar.engine.*;
import com.laufer.itamar.engine.Loads.SoldierLoads;
import com.laufer.itamar.engine.Movings.SingleStepMove;

public abstract class Soldier extends Piece {
    public Soldier(SuperTacticoGame game, Player owner, Location location, int id) {
        super(game, location, LocType.LAND, new SingleStepMove(), new SoldierLoads(), owner, id);
    }


    @Override
    public BattleResult attack(FighterPlane fighterPlane) {
        fighterPlane.die();
        return BattleResult.VICTORY;
    }

    @Override
    public BattleResult attack(TourPlane tourPlane) {
        tourPlane.die();
        return BattleResult.VICTORY;
    }

    public BattleResult attack(LandSapper landSapper) {
        landSapper.die();
        return BattleResult.VICTORY;
    }

    @Override
    public BattleResult attack(Ship ship) {
        return null; // impossible
    }

    @Override
    public BattleResult attack(Plane plane) {
        plane.die();
        return BattleResult.VICTORY;
    }

    @Override
    public BattleResult attack(Flag flag) {
        load(flag);
        return BattleResult.VICTORY;
    }

    public boolean hasFlag(){
        return getAllLoads().size() > 0;
    } //Todo maybe redundant
}
