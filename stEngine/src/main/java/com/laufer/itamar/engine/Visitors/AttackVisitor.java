package com.laufer.itamar.engine.Visitors;

import com.laufer.itamar.engine.BattleResult;
import com.laufer.itamar.engine.Pieces.*;

public class AttackVisitor implements ReturningVisitor<BattleResult> {
    private Piece attacker;
    public AttackVisitor(Piece attacker){
        this.attacker = attacker;
    }
    @Override
    public BattleResult visit(SpyShip spyShip) {
        return attacker.attack(spyShip);
    }

    @Override
    public BattleResult visit(TourPlane tourPlane) {
        return attacker.attack(tourPlane);
    }

    @Override
    public BattleResult visit(Flag flag) {
        return attacker.attack(flag);
    }

    @Override
    public BattleResult visit(LifeShip lifeShip) {
        return attacker.attack(lifeShip);
    }

    @Override
    public BattleResult visit(FighterPlane fighterPlane) {
        return attacker.attack(fighterPlane);
    }

    @Override
    public BattleResult visit(SeaSapper seaSapper) {
        return attacker.attack(seaSapper);
    }

    @Override
    public BattleResult visit(LandSapper landSapper) {
        return attacker.attack(landSapper);
    }

    @Override
    public BattleResult visit(SeaBomb seaBomb) {
        return attacker.attack(seaBomb);
    }

    @Override
    public BattleResult visit(LeveledSoldier leveledSoldier) {
        return attacker.attack(leveledSoldier);
    }

    @Override
    public BattleResult visit(LieutenantGeneral lieutenantGeneral) {
        return attacker.attack(lieutenantGeneral);
    }

    @Override
    public BattleResult visit(LandBomb landBomb) {
        return attacker.attack(landBomb);
    }

    @Override
    public BattleResult visit(M7Ship m7Ship) {
        return attacker.attack(m7Ship);
    }
    @Override
    public BattleResult visit(M4Ship m4Ship) {
        return attacker.attack(m4Ship);
    }
}
