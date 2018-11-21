package com.laufer.itamar.engine.Movings;

import com.laufer.itamar.engine.LocType;
import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Pieces.GeneralSoldier;
import com.laufer.itamar.engine.Pieces.M4Ship;
import com.laufer.itamar.engine.Pieces.Piece;
import com.laufer.itamar.engine.Square;
import com.laufer.itamar.engine.SuperTacticoGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TestBasicMove {
    private SuperTacticoGame game;

    @Test
    public void testCannotMoveToItsCurPlace(){
        Piece mover = new GeneralSoldier(game, game.getCurrentPlayer(), new Location(0, 0), 0);
        assertFalse(mover.canMove(new Location(0, 0)));
    }
    @Test
    public void testCannotMoveOutOfBoard(){
        Piece mover = new GeneralSoldier(game, game.getCurrentPlayer(), new Location(0, 0), 0);
        assertFalse(mover.canMove(new Location(-1, 0)));
        assertFalse(mover.canMove(new Location(Location.BOARD_SIZE, Location.BOARD_SIZE)));
    }
    @Test
    public void testCannotMoveToTakenSquare(){
        Piece mover = new GeneralSoldier(game, game.getCurrentPlayer(), new Location(0, 0), 0);
        Piece blocking = new GeneralSoldier(game, game.getCurrentPlayer(), new Location(1, 0), 1);
        assertFalse(mover.canMove(new Location(1, 0)));
    }
    @Test
    public void testCannotMoveToUnfitLocTypeSquare(){
        Piece moverOnlyOnSea = new M4Ship(game, game.getCurrentPlayer(), new Location(0, 0), 0);
        assertFalse(moverOnlyOnSea.canMove(new Location(1, 0))); //all board is LAND
    }
    @BeforeEach
    public void init(){
        Square[][]allLandBoard = new Square[20][20];
        for(int i=0; i<allLandBoard.length; i++){
            for(int j=0;j<allLandBoard[i].length;j++){
                allLandBoard[i][j] = new Square(LocType.LAND);
            }
        }
        game = new SuperTacticoGame(allLandBoard);
    }
}