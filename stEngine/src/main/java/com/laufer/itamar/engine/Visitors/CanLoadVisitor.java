package com.laufer.itamar.engine.Visitors;

import com.laufer.itamar.engine.Loads.Loads;
import com.laufer.itamar.engine.Pieces.*;

public class CanLoadVisitor<Boolean> implements ReturningVisitor {
    private Loads loads;

    public CanLoadVisitor(Loads loads) {
        this.loads = loads;
    }

    //suppose to be visit for each piece non abstract class
    public java.lang.Boolean visit(SpyShip spyShip) {
        return loads.canLoad(spyShip);
    }

    @Override
    public java.lang.Boolean visit(TourPlane tourPlane) {
        return loads.canLoad(tourPlane);
    }

    @Override
    public java.lang.Boolean visit(Flag flag) {
        return loads.canLoad(flag);
    }

    @Override
    public java.lang.Boolean visit(LifeShip lifeShip) {
        return loads.canLoad(lifeShip);
    }

    @Override
    public java.lang.Boolean visit(FighterPlane fighterPlane) {
        return loads.canLoad(fighterPlane);
    }

    @Override
    public java.lang.Boolean visit(SeaSapper seaSapper) {
        return loads.canLoad(seaSapper);
    }

    @Override
    public java.lang.Boolean visit(LandSapper landSapper) {
        return loads.canLoad(landSapper);
    }

    @Override
    public java.lang.Boolean visit(SeaBomb seaBomb) {
        return loads.canLoad(seaBomb);
    }

    @Override
    public java.lang.Boolean visit(LeveledSoldier leveledSoldier) {
        return loads.canLoad(leveledSoldier);
    }

    @Override
    public java.lang.Boolean visit(LieutenantGeneral lieutenantGeneral) {
        return loads.canLoad(lieutenantGeneral);
    }

    @Override
    public java.lang.Boolean visit(LandBomb landBomb) {
        return loads.canLoad(landBomb);
    }

    @Override
    public java.lang.Boolean visit(M7Ship m7Ship) {
        return loads.canLoad(m7Ship);
    }

    @Override
    public java.lang.Boolean visit(M4Ship m4Ship) {
        return loads.canLoad(m4Ship);
    }

}
