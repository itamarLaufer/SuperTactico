package com.laufer.itamar.engine.Pieces;


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
    public Boolean accept(AttackVisitor attackVisitor) {
        return attackVisitor.visit(this);
    }

    public SoldierLevel getSoldierLevel() {
        return soldierLevel;
    }

    @Override
    public int getType() {
        return soldierLevel.getLevel();
    }

    public boolean attack(LeveledSoldier leveledSoldier) {
        Piece loser = this.soldierLevel.getLevel() >= leveledSoldier.soldierLevel.getLevel() ? leveledSoldier : this;
        loser.die();
        return loser != this;
    }

    public boolean attack(LandSapper landSapper) {
        landSapper.die();
        return true;
    }

    public boolean attack(SeaSapper seaSapper) {
        seaSapper.die();
        return true;
    }
    @Override
    public boolean accept(CanLoadVisitor canLoadVisitor) {
        return canLoadVisitor.visit(this);
    }
}

