package com.laufer.itamar.communication.gui;

import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Pieces.Piece;
import com.laufer.itamar.engine.SuperTacticoGame;

public class GuiCommunicationManager {
    public static String gameToGui(SuperTacticoGame game){
        GuiSquare[][]guiBoard = new GuiSquare[game.getBoardSize()][game.getBoardSize()];
        Location tmp;
        Piece curPiece;
        for(int i=0; i< game.getBoardSize(); i++){
            for(int j = 0; j< game.getBoardSize(); j++){
                tmp = new Location(i ,j);
                curPiece = game.getPieceFromBoard(tmp);
                guiBoard[i][j] = new GuiSquare(game.getLocTypeInLocation(tmp), new GuiPiece(curPiece, curPiece.getOwner() == game.getCurrentPlayer()));
            }
        }
        return guiBoard.toString(); //Todo change to json using Gson
    }
}
