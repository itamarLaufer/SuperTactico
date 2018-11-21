package com.laufer.itamar.engine;

import com.laufer.itamar.engine.Pieces.Piece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SuperTacticoGame {
    private Player[]players;
    private Square[][]board;
    private int turns;
    private List<Piece>diedWithLifeShip; //to show dialog to select which pieces to save and kill the rest

    public SuperTacticoGame(int boardSize, String p1name, String p2name) {
        turns = 0;
        board = new Square[boardSize][boardSize];
        Location.BOARD_SIZE = boardSize;
        createBoard();
        players = new Player[2];
        players[0] = new Player(p1name, 0);
        players[1] = new Player(p2name, 1);
        diedWithLifeShip = new ArrayList<>(2);
    }
    public SuperTacticoGame(Square[][]board){
        turns = 0;
        Location.BOARD_SIZE = board.length;
        this.board = board;
        players = new Player[2];
        players[0] = new Player("player1", 0);
        players[1] = new Player("player2", 1);
    }

    private void createBoard() {
        //Todo change for real map
        for(int i=0; i<board.length; i++){
            for(int j=0;j<board[i].length;j++){
                board[i][j] = new Square(LocType.LAND);
            }
        }
    }

    /**
     * insert to the game the given players including their pieces with the chosen location and update the board according to that
     * @param players the players that participate in the game (supposes it include exactly 2 players)
     */
    public void insertPlayers(Player[]players){
        this.players = players;
        Arrays.stream(this.players).forEach(player-> Arrays.stream(player.getPieces()).forEach(piece -> insertPiece(piece)));
    }

    public Player[] getPlayers() {
        return players;
    }

    public void insertPiece(Piece piece){
        board[piece.getLocation().getRow()][piece.getLocation().getCol()].setPiece(piece);
    }

    public Piece getPieceFromBoard(Location location){
        return board[location.getRow()][location.getCol()].getPiece();
    }

    public LocType getLocTypeInLocation(Location location){
        return board[location.getRow()][location.getCol()].getLocType();
    }
    public void movePiece(Piece piece, Location location){
        board[piece.getLocation().getRow()][piece.getLocation().getCol()].setPiece(null);
        piece.setLocation(location);
        insertPiece(piece);
    }
    public Player getCurrentPlayer(){
        return players[turns%players.length];
    }

    /**
     * Handles the battle according to the result
     * @param attacker the attacking piece
     * @param target the attacked piece
     * @param result the result of the battle
     */
    public void basicBattleHandle(Piece attacker, Piece target, AttackResult result){
        switch (result){
            case TIE:
                attacker.die();
                target.die();
                break;
            case VICTORY:
                target.die();
                attacker.move(target.getLocation());
            case DEFEAT:
                attacker.die();
        }
    }
    public void removePiece(Location location){
        board[location.getRow()][location.getCol()] = null;
    }

    public List<Piece> getDiedWithLifeShip() {
        return diedWithLifeShip;
    }
}
