package com.laufer.itamar.engine.Movings;

import com.laufer.itamar.engine.LocType;
import com.laufer.itamar.engine.Pieces.LieutenantGeneral;
import com.laufer.itamar.engine.Pieces.M4Ship;
import com.laufer.itamar.engine.Pieces.Piece;
import com.laufer.itamar.engine.Square;
import com.laufer.itamar.engine.SuperTacticoGame;
import org.junit.Test;


import static com.laufer.itamar.engine.Location.generateLocation;
import static org.junit.Assert.assertFalse;

class TestBasicMove {
    private SuperTacticoGame game;

    @Test
    public void testCannotMoveToItsCurPlace(){
        init();
        Piece mover = new LieutenantGeneral(game, game.getCurrentPlayer(), generateLocation(0, 0), 0);
        assertFalse(mover.canMove(generateLocation(0, 0)));
    }
    @Test
    public void testCannotMoveOutOfBoard(){
        init();
        Piece mover = new LieutenantGeneral(game, game.getCurrentPlayer(), generateLocation(0, 0), 0);
        assertFalse(mover.canMove(generateLocation(-1, 0)));
        assertFalse(mover.canMove(generateLocation(game.getBoardSize(), game.getBoardSize())));
    }
    @Test
    public void testCannotMoveToTakenSquare(){
        init();
        Piece mover = new LieutenantGeneral(game, game.getCurrentPlayer(), generateLocation(0, 0), 0);
        Piece blocking = new LieutenantGeneral(game, game.getCurrentPlayer(), generateLocation(1, 0), 1);
        assertFalse(mover.canMove(generateLocation(1, 0)));
    }
    @Test
    public void testCannotMoveToUnfitLocTypeSquare(){
        init();
        Piece moverOnlyOnSea = new M4Ship(game, game.getCurrentPlayer(), generateLocation(0, 0), 0);
        assertFalse(moverOnlyOnSea.canMove(generateLocation(1, 0))); //all board is LAND
    }
    private void init(){
        Square[][]allLandBoard = new Square[20][20];
        for(int i=0; i<allLandBoard.length; i++){
            for(int j=0;j<allLandBoard[i].length;j++){
                allLandBoard[i][j] = new Square(LocType.LAND);
            }
        }
        game = new SuperTacticoGame(allLandBoard);
    }
}