package com.laufer.itamar.engine.Loads;

import com.laufer.itamar.engine.Pieces.*;

public class M7Loads extends Loads {
    @Override
    public Boolean canLoad(LifeShip lifeShip) {
        return false;
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
    public Boolean canLoad(TourPlane tourPlane) {
        return planes.isEmpty(); //can load only one tour plane
    }

    @Override
    public Boolean canLoad(Flag flag) {
        return false;
    }
}
