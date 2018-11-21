package com.laufer.itamar.engine.Pieces;


import com.laufer.itamar.engine.AttackResult;
import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Player;
import com.laufer.itamar.engine.SuperTacticoGame;
import com.laufer.itamar.engine.Visitors.AttackVisitor;
import com.laufer.itamar.engine.Visitors.ReturningVisitor;
import com.laufer.itamar.engine.Visitors.VoidVisitor;

public class GeneralSoldier extends LeveledSoldier {
    public GeneralSoldier(SuperTacticoGame game, Player owner, Location location, int id){
        super(game, owner, location, SoldierLevel.GENERAL, id);
    }

    @Override
    public void accept(VoidVisitor voidVisitor){
        voidVisitor.visit(this);
    }
    @Override
    public AttackResult accept(AttackVisitor attackVisitor) {
        return attackVisitor.visit(this);
    }

    public boolean attack(LandSapper landSapper) {
        landSapper.die();
        return true;
    }
}
