package com.laufer.itamar.engine.Loads;


import com.laufer.itamar.engine.Pieces.*;

public class M4Loads extends Loads {
    @Override
    public Boolean canLoad(LifeShip lifeShip) {
        return bombs.isEmpty();
    }

    @Override
    public Boolean canLoad(Bomb bomb) {
        return bombs.isEmpty(); //can only load one bomb
    }

    @Override
    public Boolean canLoad(SpyShip spyShip) {
        return ships.isEmpty(); //can only load one SaveShip or SpyShip
    }

    @Override
    public Boolean canLoad(Soldier soldier) {
        return soldiers.size()<4;
    }

    @Override
    public Boolean canLoad(TourPlaneLoads tourPlaneLoads) {
        return false; //can'nt load planes
    }

    @Override
    public Boolean canLoad(Flag flag) {
        return false;
    }
}
