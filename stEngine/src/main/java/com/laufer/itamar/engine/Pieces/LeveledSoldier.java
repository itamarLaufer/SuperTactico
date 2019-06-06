package com.laufer.itamar.engine.Pieces;


import com.laufer.itamar.engine.BattleResult;
import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Player;
import com.laufer.itamar.engine.SuperTacticoGame;
import com.laufer.itamar.engine.Visitors.AttackVisitor;
import com.laufer.itamar.engine.Visitors.CanLoadVisitor;
import com.laufer.itamar.engine.Visitors.VoidVisitor;

public class LeveledSoldier extends Soldier {
    private SoldierLevel soldierLevel;

    public LeveledSoldier(SuperTacticoGame game, Player owner, Location location, SoldierLevel soldierLevel, int id) {
        super(game, owner, location, id);
        this.soldierLevel = soldierLevel;

    }

    @Override
    public void accept(VoidVisitor voidVisitor) {
        voidVisitor.visit(this);
    }

    @Override
    public BattleResult accept(AttackVisitor attackVisitor) {
        return attackVisitor.visit(this);
    }

    public SoldierLevel getSoldierLevel() {
        return soldierLevel;
    }

    @Override
    public int getType() {
        return soldierLevel.getLevel();
    }

    @Override
    public BattleResult attack(Soldier soldier) {
        return null; // will always go the specific
    }

    public BattleResult attack(LeveledSoldier leveledSoldier) {
        if(soldierLevel.getLevel() > leveledSoldier.soldierLevel.getLevel()){
            leveledSoldier.die();
            return BattleResult.VICTORY;
        }
        die();
        if(soldierLevel.getLevel() < leveledSoldier.soldierLevel.getLevel())
            return BattleResult.DEFEAT;
        leveledSoldier.die();
        return BattleResult.TIE;
    }

    public BattleResult attack(LandSapper landSapper) {
        landSapper.die();
        return BattleResult.VICTORY;
    }

    public BattleResult attack(SeaSapper seaSapper) {
        seaSapper.die();
        return BattleResult.VICTORY;
    }
    @Override
    public boolean accept(CanLoadVisitor canLoadVisitor) {
        return canLoadVisitor.visit(this);
    }
}

