package com.laufer.itamar.engine.Pieces;

import com.laufer.itamar.engine.*;
import com.laufer.itamar.engine.Visitors.AttackVisitor;
import com.laufer.itamar.engine.Visitors.CanLoadVisitor;
import com.laufer.itamar.engine.Visitors.ReturningVisitor;
import com.laufer.itamar.engine.Visitors.VoidVisitor;

public class SeaSapper extends Soldier {
    public SeaSapper(SuperTacticoGame game, Player owner, Location location, int id) {
        super(game, owner, location, id);
        this.locType = LocType.ALL;
    }

    @Override
    public void accept(VoidVisitor voidVisitor) {
        voidVisitor.visit(this);
    }

    @Override
    public BattleResult accept(AttackVisitor attackVisitor) {
        return attackVisitor.visit(this);
    }

    @Override
    public boolean accept(CanLoadVisitor canLoadVisitor) {
        return canLoadVisitor.visit(this);
    }

    @Override
    public int getType() {
        return 10;
    }

    public BattleResult attack(M7Ship m7Ship) {
        //first to attack wins
        m7Ship.die();
        return BattleResult.VICTORY;
    }

    @Override
    public BattleResult attack(Soldier soldier) {
        die();
        return BattleResult.DEFEAT;
    }
    @Override
    public BattleResult attack(SeaSapper seaSapper){
        seaSapper.die();
        die();
        return BattleResult.TIE;
    }

    @Override
    public BattleResult attack(Flag flag) {
        die();
        return BattleResult.DEFEAT;
    }
    @Override
    public BattleResult attack(SeaBomb seaBomb){
        seaBomb.die();
        return BattleResult.VICTORY;
    }
}
