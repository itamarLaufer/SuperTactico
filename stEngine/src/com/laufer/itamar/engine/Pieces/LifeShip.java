package com.laufer.itamar.engine.Pieces;

import com.laufer.itamar.engine.AttackResult;
import com.laufer.itamar.engine.Loads.LifeShipLoads;
import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Player;
import com.laufer.itamar.engine.SuperTacticoGame;
import com.laufer.itamar.engine.Visitors.AttackVisitor;
import com.laufer.itamar.engine.Visitors.CanLoadVisitor;
import com.laufer.itamar.engine.Visitors.ReturningVisitor;
import com.laufer.itamar.engine.Visitors.VoidVisitor;

public class LifeShip extends Ship {
    public LifeShip(SuperTacticoGame game, Player owner, Location location, int id) {
        super(game, owner, location, new LifeShipLoads(), id);
    }

    @Override
    public void accept(VoidVisitor voidVisitor) {
        voidVisitor.visit(this);
    }

    @Override
    public AttackResult accept(AttackVisitor attackVisitor) {
        return attackVisitor.visit(this);
    }

    @Override
    public int getType() {
        return 13;
    }
    public boolean attack(M7Ship m7Ship){
        die();
        return false;
    }
    public boolean attack(M4Ship m4Ship){
        die();
        return false;
    }
    public boolean attack(SpyShip spyShip){
        die();
        return false;
    }
    public boolean attack(LifeShip lifeShip){
        lifeShip.die();
        return true;
    }
    @Override
    public boolean accept(CanLoadVisitor canLoadVisitor) {
        return canLoadVisitor.visit(this);
    }
}
