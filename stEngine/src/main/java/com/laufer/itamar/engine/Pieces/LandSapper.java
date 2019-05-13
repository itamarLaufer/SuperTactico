package com.laufer.itamar.engine.Pieces;


import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Player;
import com.laufer.itamar.engine.SuperTacticoGame;
import com.laufer.itamar.engine.Visitors.AttackVisitor;
import com.laufer.itamar.engine.Visitors.CanLoadVisitor;
import com.laufer.itamar.engine.Visitors.VoidVisitor;

public class LandSapper extends Soldier {
    public LandSapper(SuperTacticoGame game, Player owner, Location location, int id) {
        super(game, owner, location, id);
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
        return 9;
    }

    @Override
    public boolean attack(Soldier soldier) {
        return false; // Todo implement all the different type of soldier that it can attack
    }

    public boolean attack(LandBomb landBomb){
        landBomb.die();
        return true;
    }

    @Override
    public boolean accept(CanLoadVisitor canLoadVisitor) {
        return canLoadVisitor.visit(this);
    }
}
