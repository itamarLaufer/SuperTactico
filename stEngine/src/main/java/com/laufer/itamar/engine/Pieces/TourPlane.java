package com.laufer.itamar.engine.Pieces;

import com.laufer.itamar.engine.Loads.TourPlaneLoads;
import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Player;
import com.laufer.itamar.engine.SuperTacticoGame;
import com.laufer.itamar.engine.Visitors.AttackVisitor;
import com.laufer.itamar.engine.Visitors.CanLoadVisitor;
import com.laufer.itamar.engine.Visitors.VoidVisitor;

public class TourPlane extends Plane {
    public TourPlane(SuperTacticoGame game, Player owner, Location location, int id) {
        super(game, owner, location, new TourPlaneLoads(), id);
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
        return 17;
    }

    @Override
    public boolean attack(Plane plane) {
        return false; // will always go to the specific
    }

    public boolean attack(TourPlane tourPlane){
        tourPlane.die();
        return true;
    }

    public boolean attack(FighterPlane fighterPlane){
        die();
        return false;
    }
    @Override
    public boolean accept(CanLoadVisitor canLoadVisitor) {
        return canLoadVisitor.visit(this);
    }
}
