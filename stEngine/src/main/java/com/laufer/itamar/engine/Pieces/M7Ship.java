package com.laufer.itamar.engine.Pieces;

import com.laufer.itamar.engine.BattleResult;
import com.laufer.itamar.engine.Loads.M7Loads;
import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Player;
import com.laufer.itamar.engine.SuperTacticoGame;
import com.laufer.itamar.engine.Visitors.AttackVisitor;
import com.laufer.itamar.engine.Visitors.CanLoadVisitor;
import com.laufer.itamar.engine.Visitors.VoidVisitor;

public class M7Ship extends Ship {
    public M7Ship(SuperTacticoGame game, Player owner, Location location, int id) {
        super(game, owner, location, new M7Loads(), id);
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
    public int getType() {
        return 16;
    }

    @Override
    public BattleResult attack(Ship ship) {
        return null; // will always go to the specific
    }

    public BattleResult attack(SeaSapper seaSapper){
        //first to attack wins
        seaSapper.die();
        return BattleResult.VICTORY;
    }
    public BattleResult attack(M7Ship m7Ship){
        m7Ship.die();
        die();
        return BattleResult.VICTORY;
    }
    public BattleResult attack(M4Ship m4Ship){
        m4Ship.die();
        return BattleResult.VICTORY;
    }
    public BattleResult attack(SpyShip spyShip){
        spyShip.die();
        return BattleResult.VICTORY;
    }
    public BattleResult attack(LifeShip lifeShip) {
        lifeShip.die();
        return BattleResult.VICTORY;
    }
    @Override
    public boolean accept(CanLoadVisitor canLoadVisitor) {
        return canLoadVisitor.visit(this);
    }
}
