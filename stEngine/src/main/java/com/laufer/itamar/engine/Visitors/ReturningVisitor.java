package com.laufer.itamar.engine.Visitors;

import com.laufer.itamar.engine.Pieces.*;

public interface ReturningVisitor<T> {
    //for each non abstract piece
    T visit(SpyShip spyShip);
    T visit(TourPlane tourPlane);
    T visit(Flag flag);
    T visit(LifeShip lifeShip);
    T visit(FighterPlane fighterPlane);
    T visit(SeaSapper seaSapper);
    T visit(LandSapper landSapper);
    T visit(SeaBomb seaBomb);
    T visit(LeveledSoldier leveledSoldier);
    T visit(LieutenantGeneral lieutenantGeneral);
    T visit(LandBomb landBomb);
    T visit(M7Ship m7Ship);
    T visit(M4Ship m4Ship);

}
