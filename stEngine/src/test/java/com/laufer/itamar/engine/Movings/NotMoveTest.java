package com.laufer.itamar.engine.Movings;

import com.laufer.itamar.engine.LocType;
import com.laufer.itamar.engine.Pieces.Flag;
import com.laufer.itamar.engine.Pieces.Piece;
import com.laufer.itamar.engine.Square;
import com.laufer.itamar.engine.SuperTacticoGame;
import org.junit.Test;

import static com.laufer.itamar.engine.Location.generateLocation;
import static org.junit.Assert.*;

public class NotMoveTest {

    private SuperTacticoGame game;

    @Test
    public void testCannotMove(){
        init();
        Piece unmovable = new Flag(game, game.getCurrentPlayer(),generateLocation(0, 0), 0);
        assertFalse(unmovable.canMove(generateLocation(0, 1)));
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