package com.laufer.itamar.engine.Pieces;


import com.laufer.itamar.engine.*;
import com.laufer.itamar.engine.Loads.CannotLoadLoads;
import com.laufer.itamar.engine.Movings.NotMove;
import com.laufer.itamar.engine.Visitors.AttackVisitor;
import com.laufer.itamar.engine.Visitors.CanLoadVisitor;
import com.laufer.itamar.engine.Visitors.VoidVisitor;
import com.laufer.itamar.engine.orders.MoveOrder;

import java.util.List;

public class Flag extends Piece {

    public Flag(SuperTacticoGame game, Player owner, Location location, int id) {
        super(game, location, LocType.LAND, new NotMove(), new CannotLoadLoads(), owner, id);
    }

    @Override
    public BattleResult accept(AttackVisitor attackVisitor) {
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
    public BattleResult attack(Plane plane) {
        return null; //cannot attack
    }

    @Override
    public BattleResult attack(Ship ship) {
        return null; //cannot attack
    }

    @Override
    public BattleResult attack(Soldier soldier) {
        return null; //cannot attack
    }

    @Override
    public boolean accept(CanLoadVisitor canLoadVisitor) {
        return canLoadVisitor.visit(this);
    }

    @Override
    public List<MoveOrder> getLocateOrders() {
        List<MoveOrder> res = super.getLocateOrders();
        res.removeIf(moveOrder -> game.isIsland(moveOrder.getLocation()));
        return res;
    }

    @Override
    public void move(Location dest) {
        super.move(dest);
        if(game.isIsland(location))
            game.endGame();
    }
}
