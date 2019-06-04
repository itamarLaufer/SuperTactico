package com.laufer.itamar.engine.Pieces;


import com.laufer.itamar.engine.BattleResult;
import com.laufer.itamar.engine.Loads.FighterPlaneLoads;
import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Player;
import com.laufer.itamar.engine.SuperTacticoGame;
import com.laufer.itamar.engine.Visitors.AttackVisitor;
import com.laufer.itamar.engine.Visitors.CanLoadVisitor;
import com.laufer.itamar.engine.Visitors.VoidVisitor;

public class FighterPlane extends Plane {

    public FighterPlane(SuperTacticoGame game, Player owner, Location location, int id) {
        super(game, owner, location, new FighterPlaneLoads(), id);
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
        return 18;
    }

    @Override
    public BattleResult attack(Plane plane) {
        return null; // will always go to the specific
    }

    public BattleResult attack(TourPlane tourPlane){
        tourPlane.die();
        return BattleResult.VICTORY;
    }

    public BattleResult attack(FighterPlane fighterPlane){
        fighterPlane.die();
        die();
        return BattleResult.TIE;
    }
    @Override
    public boolean accept(CanLoadVisitor canLoadVisitor) {
        return canLoadVisitor.visit(this);
    }
}
