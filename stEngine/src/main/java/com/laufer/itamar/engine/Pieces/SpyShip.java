package com.laufer.itamar.engine.Pieces;

import com.laufer.itamar.engine.BattleResult;
import com.laufer.itamar.engine.Loads.SpyShipLoads;
import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Player;
import com.laufer.itamar.engine.SuperTacticoGame;
import com.laufer.itamar.engine.Visitors.AttackVisitor;
import com.laufer.itamar.engine.Visitors.CanLoadVisitor;
import com.laufer.itamar.engine.Visitors.VoidVisitor;

public class SpyShip extends Ship {
    public SpyShip(SuperTacticoGame game, Player owner, Location location, int id) {
        super(game, owner, location, new SpyShipLoads(), id);
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
        return 14;
    }

    @Override
    public BattleResult attack(Ship ship) {
        return null; //it will always go to the specific
    }

    public BattleResult attack(M7Ship m7Ship){
        die();
        return BattleResult.DEFEAT;
    }
    public BattleResult attack(M4Ship m4Ship){
        die();
        return BattleResult.DEFEAT;
    }
    public BattleResult attack(SpyShip spyShip){
        die();
        spyShip.die();
        return BattleResult.TIE;
    }
    public BattleResult attack(LifeShip lifeShip){
        lifeShip.die();
        return BattleResult.DEFEAT;
    }
    @Override
    public boolean accept(CanLoadVisitor canLoadVisitor) {
        return canLoadVisitor.visit(this);
    }
}
