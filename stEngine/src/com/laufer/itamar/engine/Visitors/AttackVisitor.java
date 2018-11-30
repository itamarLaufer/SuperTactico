package com.laufer.itamar.engine.Visitors;

import com.laufer.itamar.engine.AttackResult;
import com.laufer.itamar.engine.Pieces.*;

public class AttackVisitor implements ReturningVisitor<AttackResult> {
    private Piece attacker;
    public AttackVisitor(Piece attacker){
        this.attacker = attacker;
    }
    @Override
    public AttackResult visit(SpyShip spyShip) {
        return attacker.attack(spyShip);
    }

    @Override
    public AttackResult visit(TourPlane tourPlane) {
        return attacker.attack(tourPlane);
    }

    @Override
    public AttackResult visit(Flag flag) {
        return attacker.attack(flag);
    }

    @Override
    public AttackResult visit(LifeShip lifeShip) {
        return attacker.attack(lifeShip);
    }

    @Override
    public AttackResult visit(FighterPlane fighterPlane) {
        return attacker.attack(fighterPlane);
    }

    @Override
    public AttackResult visit(SeaSapper seaSapper) {
        return attacker.attack(seaSapper);
    }

    @Override
    public AttackResult visit(LandSapper landSapper) {
        return attacker.attack(landSapper);
    }

    @Override
    public AttackResult visit(SeaBomb seaBomb) {
        return attacker.attack(seaBomb);
    }

    @Override
    public AttackResult visit(LeveledSoldier leveledSoldier) {
        return attacker.attack(leveledSoldier);
    }

    @Override
    public AttackResult visit(LieutenantGeneral lieutenantGeneral) {
        return attacker.attack(lieutenantGeneral);
    }

    @Override
    public AttackResult visit(LandBomb landBomb) {
        return attacker.attack(landBomb);
    }

    @Override
    public AttackResult visit(M7Ship m7Ship) {
        return attacker.attack(m7Ship);
    }
    @Override
    public AttackResult visit(M4Ship m4Ship) {
        return attacker.attack(m4Ship);
    }

}
