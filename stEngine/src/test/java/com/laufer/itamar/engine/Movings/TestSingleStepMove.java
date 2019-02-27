package com.laufer.itamar.engine.Movings;

import com.laufer.itamar.engine.LocType;
import com.laufer.itamar.engine.Pieces.LieutenantGeneral;
import com.laufer.itamar.engine.Pieces.Piece;
import com.laufer.itamar.engine.Square;
import com.laufer.itamar.engine.SuperTacticoGame;
import org.junit.Test;


import static com.laufer.itamar.engine.Location.generateLocation;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

class TestSingleStepMove {
    private SuperTacticoGame game;
    public TestSingleStepMove(){
    }
    @Test
    public void testCanMoveSingleStep(){
        init();
        Piece singleStepper = new LieutenantGeneral(game, game.getCurrentPlayer(), generateLocation(1, 1), 0);
        assertTrue(singleStepper.canMove(generateLocation(1, 0)));
        assertTrue(singleStepper.canMove(generateLocation(1, 2)));
        assertTrue(singleStepper.canMove(generateLocation(0, 1)));
        assertTrue(singleStepper.canMove(generateLocation(2, 1)));
    }
    @Test
    public void testCannotMoveDiagonally(){
        init();
        Piece singleStepper = new LieutenantGeneral(game, game.getCurrentPlayer(), generateLocation(0, 0), 0);
        assertFalse(singleStepper.canMove(generateLocation(1, 1)));
    }
    @Test
    public void testCannotMoveMoreThanSingleStep(){
        init();
        Piece singleStepper = new LieutenantGeneral(game, game.getCurrentPlayer(), generateLocation(0, 0), 0);
        assertFalse(singleStepper.canMove(generateLocation(8, 0)));
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