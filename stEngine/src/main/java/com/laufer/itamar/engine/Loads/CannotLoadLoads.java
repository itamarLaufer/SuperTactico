package com.laufer.itamar.engine.Loads;


import com.laufer.itamar.engine.Pieces.*;

public class CannotLoadLoads extends Loads {
    @Override
    public Boolean canLoad(LifeShip lifeShip) {
        return false;
    }

    @Override
    public Boolean canLoad(Bomb bomb) {
        return false;
    }

    @Override
    public Boolean canLoad(SpyShip spyShip) {
        return false;
    }

    @Override
    public Boolean canLoad(Soldier soldier) {
        return false;
    }

    @Override
    public Boolean canLoad(TourPlaneLoads tourPlaneLoads) {
        return false;
    }

    @Override
    public Boolean canLoad(Flag flag) {
        return false;
    }
}
