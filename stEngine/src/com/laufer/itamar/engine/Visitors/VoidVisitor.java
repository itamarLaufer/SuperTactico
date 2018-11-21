package com.laufer.itamar.engine.Visitors;

import com.laufer.itamar.engine.Pieces.*;

public interface VoidVisitor {
    //for each non abstract piece
    void visit(SpyShip spyShip);
    void visit(TourPlane tourPlane);
    void visit(Flag flag);
    void visit(LifeShip lifeShip);
    void visit(FighterPlane fighterPlane);
    void visit(SeaSapper seaSapper);
    void visit(LandSapper landSapper);
    void visit(SeaBomb seaBomb);
    void visit(LeveledSoldier leveledSoldier);
    void visit(GeneralSoldier generalSoldier);
    void visit(LandBomb landBomb);
    void visit(M7Ship m7Ship);

}
