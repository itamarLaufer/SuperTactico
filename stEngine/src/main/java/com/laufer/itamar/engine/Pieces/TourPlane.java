package com.laufer.itamar.engine.Pieces;

import com.laufer.itamar.engine.BattleResult;
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
    public BattleResult accept(AttackVisitor attackVisitor) {
        return attackVisitor.visit(this);
    }

    @Override
    public int getType() {
        return 17;
    }

    @Override
    public BattleResult attack(Plane plane) {
        return null; // will always go to the specific
    }

    public BattleResult attack(TourPlane tourPlane){
        tourPlane.die();
        die();
        return BattleResult.TIE;
    }

    public BattleResult attack(FighterPlane fighterPlane){
        die();
        return BattleResult.DEFEAT;
    }
    @Override
    public boolean accept(CanLoadVisitor canLoadVisitor) {
        return canLoadVisitor.visit(this);
    }
}
