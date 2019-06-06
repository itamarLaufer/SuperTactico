package com.laufer.itamar.engine.Pieces;


import com.laufer.itamar.engine.BattleResult;
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
    public BattleResult accept(AttackVisitor attackVisitor) {
        return attackVisitor.visit(this);
    }

    @Override
    public int getType() {
        return 9;
    }

    @Override
    public BattleResult attack(LeveledSoldier soldier) {
        die();
        return BattleResult.DEFEAT;
    }
    @Override
    public BattleResult attack(LandBomb landBomb){
        landBomb.die();
        return BattleResult.VICTORY;
    }

    @Override
    public BattleResult attack(Soldier soldier) {
        return null; //will always go to the specific
    }

    public BattleResult attack(LandSapper landSapper){
        landSapper.die();
        die();
        return BattleResult.TIE;
    }

    @Override
    public BattleResult attack(SeaSapper seaSapper) {
        seaSapper.die();
        return BattleResult.VICTORY;
    }
    @Override
    public BattleResult attack(LieutenantGeneral lieutenantGeneral) {
        lieutenantGeneral.die();
        return BattleResult.VICTORY;
    }



    @Override
    public boolean accept(CanLoadVisitor canLoadVisitor) {
        return canLoadVisitor.visit(this);
    }
}
