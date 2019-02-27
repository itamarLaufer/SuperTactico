package com.laufer.itamar.engine.Movings;

import com.laufer.itamar.engine.LocType;
import com.laufer.itamar.engine.Pieces.Piece;
import com.laufer.itamar.engine.Pieces.TourPlane;
import com.laufer.itamar.engine.Square;
import com.laufer.itamar.engine.SuperTacticoGame;
import org.junit.Test;

import static com.laufer.itamar.engine.Location.generateLocation;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertFalse;

class TestMultipleStepMove {
    private SuperTacticoGame game;
    @Test
    public void testCanMoveSingleStep(){
        init();
        Piece multiStepper = new TourPlane(game, game.getCurrentPlayer(), generateLocation(0, 0), 0);
        assertTrue(multiStepper.canMove(generateLocation(1, 0)));
    }
    @Test
    public void testCannotMoveDiagonally(){
        init();
        Piece multiStepper = new TourPlane(game, game.getCurrentPlayer(), generateLocation(0, 0), 0);
        assertFalse(multiStepper.canMove(generateLocation(1, 1)));
    }
    @Test
    public void testCanMoveMoreThanSingleStep(){
        init();
        Piece multiStepper = new TourPlane(game, game.getCurrentPlayer(), generateLocation(0, 0), 0);
        assertTrue(multiStepper.canMove(generateLocation(8, 0)));
    }
    @Test
    public void testCannotMoveAbovePieces(){
        init();
        fail("needs to be implemented"); //todo implement
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