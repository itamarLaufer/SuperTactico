package com.laufer.itamar.engine.Visitors;

import com.laufer.itamar.engine.Pieces.*;

public class AttackVisitor implements ReturningVisitor<Boolean> {
    private Piece attacker;
    public AttackVisitor(Piece attacker){
        this.attacker = attacker;
    }
    @Override
    public Boolean visit(SpyShip spyShip) {
        return attacker.attack(spyShip);
    }

    @Override
    public Boolean visit(TourPlane tourPlane) {
        return attacker.attack(tourPlane);
    }

    @Override
    public Boolean visit(Flag flag) {
        return attacker.attack(flag);
    }

    @Override
    public Boolean visit(LifeShip lifeShip) {
        return attacker.attack(lifeShip);
    }

    @Override
    public Boolean visit(FighterPlane fighterPlane) {
        return attacker.attack(fighterPlane);
    }

    @Override
    public Boolean visit(SeaSapper seaSapper) {
        return attacker.attack(seaSapper);
    }

    @Override
    public Boolean visit(LandSapper landSapper) {
        return attacker.attack(landSapper);
    }

    @Override
    public Boolean visit(SeaBomb seaBomb) {
        return attacker.attack(seaBomb);
    }

    @Override
    public Boolean visit(LeveledSoldier leveledSoldier) {
        return attacker.attack(leveledSoldier);
    }

    @Override
    public Boolean visit(LieutenantGeneral lieutenantGeneral) {
        return attacker.attack(lieutenantGeneral);
    }

    @Override
    public Boolean visit(LandBomb landBomb) {
        return attacker.attack(landBomb);
    }

    @Override
    public Boolean visit(M7Ship m7Ship) {
        return attacker.attack(m7Ship);
    }
    @Override
    public Boolean visit(M4Ship m4Ship) {
        return attacker.attack(m4Ship);
    }
}
