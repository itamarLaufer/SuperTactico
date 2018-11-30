package com.laufer.itamar.engine.Pieces;


import com.laufer.itamar.engine.AttackResult;
import com.laufer.itamar.engine.Loads.FigtherPlaneLoads;
import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Player;
import com.laufer.itamar.engine.SuperTacticoGame;
import com.laufer.itamar.engine.Visitors.AttackVisitor;
import com.laufer.itamar.engine.Visitors.CanLoadVisitor;
import com.laufer.itamar.engine.Visitors.ReturningVisitor;
import com.laufer.itamar.engine.Visitors.VoidVisitor;

public class FighterPlane extends Plane {

    public FighterPlane(SuperTacticoGame game, Player owner, Location location, int id) {
        super(game, owner, location, new FigtherPlaneLoads(), id);
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
        return 18;
    }

    public boolean attack(TourPlane tourPlane){
        tourPlane.die();
        return true;
    }

    public boolean attack(FighterPlane fighterPlane){
        fighterPlane.die();
        return true;
    }
    @Override
    public boolean accept(CanLoadVisitor canLoadVisitor) {
        return canLoadVisitor.visit(this);
    }
}
