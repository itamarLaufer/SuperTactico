package com.laufer.itamar.engine.Loads;

import com.laufer.itamar.engine.Pieces.*;

public class TourPlaneLoads extends Loads {
    @Override
    public Boolean canLoad(LifeShip lifeShip) {
        return false;
    }

    @Override
    public Boolean canLoad(Bomb bomb) {
        return bombs.isEmpty();
    }

    @Override
    public Boolean canLoad(SpyShip spyShip) {
        return false;
    }

    @Override
    public Boolean canLoad(Soldier soldier) {
        return soldiers.size()<2;
    }

    @Override
    public Boolean canLoad(TourPlane tourPlane) {
        return false;
    }
}
