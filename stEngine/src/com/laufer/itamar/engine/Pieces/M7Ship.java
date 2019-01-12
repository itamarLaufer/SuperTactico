package com.laufer.itamar.engine.Pieces;

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
    public Boolean accept(AttackVisitor attackVisitor) {
        return attackVisitor.visit(this);
    }

    @Override
    public int getType() {
        return 16;
    }
    public boolean attack(SeaSapper seaSapper){
        //first to attack wins
        seaSapper.die();
        return true;
    }
    public boolean attack(M7Ship m7Ship){
        m7Ship.die();
        return true;
    }
    public boolean attack(M4Ship m4Ship){
        m4Ship.die();
        return true;
    }
    public boolean attack(SpyShip spyShip){
        spyShip.die();
        return true;
    }
    public boolean attack(LifeShip lifeShip) {
        lifeShip.die();
        return true;
    }
    @Override
    public boolean accept(CanLoadVisitor canLoadVisitor) {
        return canLoadVisitor.visit(this);
    }
}
