package com.laufer.itamar.engine.Pieces;


import com.laufer.itamar.engine.Loads.CannotLoadLoads;
import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Movings.NotMove;
import com.laufer.itamar.engine.Player;
import com.laufer.itamar.engine.SuperTacticoGame;
import com.laufer.itamar.engine.Visitors.AttackVisitor;
import com.laufer.itamar.engine.Visitors.CanLoadVisitor;
import com.laufer.itamar.engine.Visitors.VoidVisitor;

public class Flag extends Soldier {

    public Flag(SuperTacticoGame game, Player owner, Location location, int id) {
        super(game, owner, location, id);
        this.loads = new CannotLoadLoads();
        this.moveType = new NotMove();
    }

    @Override
    public Boolean accept(AttackVisitor attackVisitor) {
        return attackVisitor.visit(this);
    }

    @Override
    public void accept(VoidVisitor voidVisitor) {
        voidVisitor.visit(this);

    }
    @Override
    public boolean canAttack(Piece other) {
        return false;
    }

    @Override
    public int getType() {
        return 19;
    }
    @Override
    public boolean accept(CanLoadVisitor canLoadVisitor) {
        return canLoadVisitor.visit(this);
    }
}
