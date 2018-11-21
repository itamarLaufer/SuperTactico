package com.laufer.itamar.engine.Pieces;


import com.laufer.itamar.engine.*;
import com.laufer.itamar.engine.Visitors.AttackVisitor;
import com.laufer.itamar.engine.Visitors.CanLoadVisitor;
import com.laufer.itamar.engine.Visitors.VoidVisitor;

public class SeaBomb extends Bomb {

    public SeaBomb(SuperTacticoGame game, Player owner, Location location, int id) {
        super(game, owner, location, LocType.SEA, id);
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
    public String getType() {
        return "Sea Bomb";
    }
    @Override
    public boolean accept(CanLoadVisitor canLoadVisitor) {
        return canLoadVisitor.visit(this);
    }
}
