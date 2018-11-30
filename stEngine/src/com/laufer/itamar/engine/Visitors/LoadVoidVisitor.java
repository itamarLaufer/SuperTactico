package com.laufer.itamar.engine.Visitors;

import com.laufer.itamar.engine.Loads.Loads;
import com.laufer.itamar.engine.Pieces.*;

public class LoadVoidVisitor implements VoidVisitor {
    private Loads loads;

    public LoadVoidVisitor(Loads loads) {
        this.loads = loads;
    }

    @Override
    public void visit(SpyShip spyShip) {
        loads.load(spyShip);
    }

    @Override
    public void visit(TourPlane tourPlane) {
        loads.load(tourPlane);
    }

    @Override
    public void visit(Flag flag) {
        loads.load(flag);
    }

    @Override
    public void visit(LifeShip lifeShip) {
        loads.load(lifeShip);
    }

    @Override
    public void visit(FighterPlane fighterPlane) {
        loads.load(fighterPlane);
    }

    @Override
    public void visit(SeaSapper seaSapper) {
        loads.load(seaSapper);
    }

    @Override
    public void visit(LandSapper landSapper) {
        loads.load(landSapper);
    }

    @Override
    public void visit(SeaBomb seaBomb) {
        loads.load(seaBomb);
    }

    @Override
    public void visit(LeveledSoldier leveledSoldier) {
        loads.load(leveledSoldier);
    }

    @Override
    public void visit(LieutenantGeneral lieutenantGeneral) {
        loads.load(lieutenantGeneral);
    }

    @Override
    public void visit(LandBomb landBomb) {
        loads.load(landBomb);
    }

    @Override
    public void visit(M7Ship m7Ship) {
        loads.load(m7Ship);
    }
}
