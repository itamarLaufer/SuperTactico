package com.laufer.itamar.engine.Pieces;

import com.laufer.itamar.engine.*;
import com.laufer.itamar.engine.Loads.Loads;
import com.laufer.itamar.engine.Movings.MultipleStepsMove;
import com.laufer.itamar.engine.orders.MoveOrder;

import java.util.Iterator;
import java.util.List;

public abstract class Plane extends Piece {
    public Plane(SuperTacticoGame game, Player owner, Location location, Loads loads, int id) {
        super(game, location, LocType.ALL, new MultipleStepsMove(), loads, owner, id);
    }
    public boolean attack(Ship ship){
        die();
        return false;
    }
    public boolean attack(Soldier soldier){
        die();
        return false;
    }
    public Boolean attack(Bomb bomb){
        die();
        return false;
    }

    @Override
    public List<MoveOrder> getLocateOrders() {
        List<MoveOrder> res = super.getLocateOrders();
        res.removeIf(moveOrder -> game.getLocTypeInLocation(moveOrder.getLocation()) == LocType.LAND);
        return res;
    }
}
