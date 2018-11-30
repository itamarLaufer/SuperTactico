package com.laufer.itamar.engine.Pieces;

import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Player;
import com.laufer.itamar.engine.SuperTacticoGame;
public class PieceFactory {
    private SuperTacticoGame game;
    private Player player;
    private int counter;

    public PieceFactory(SuperTacticoGame game, Player player) {
        this.game = game;
        this.player = player;
        counter = -1;
    }
    public Piece createPiece(int type, Player player, Location location){
        counter++;
        if(type < 8)
            return new LeveledSoldier(game, player, location, SoldierLevel.getLeveledSoldierFromLevel(type), counter);
        if(type == 8)
            return new LieutenantGeneral(game, player, location, counter);
        if(type == 9)
            return new LandSapper(game, player, location, counter);
        if(type == 10)
            return new SeaSapper(game, player, location, counter);
        if(type == 11)
            return new LandBomb(game, player, location, counter);
        if(type == 12)
            return new SeaBomb(game, player, location, counter);
        if(type == 13)
            return new LifeShip(game, player, location, counter);
        if(type == 14)
            return new SpyShip(game, player, location, counter);
        if(type == 15)
            return new M4Ship(game, player, location, counter);
        if(type == 16)
            return new M7Ship(game, player, location, counter);
        if(type == 17)
            return new TourPlane(game, player, location, counter);
        if(type == 18)
            return new FighterPlane(game, player, location, counter);
        if(type == 19)
            return new Flag(game, player, location, counter);
        counter--;
        return null;
    }
}
