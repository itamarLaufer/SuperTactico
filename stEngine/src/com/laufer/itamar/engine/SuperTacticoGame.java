package com.laufer.itamar.engine;

import com.laufer.itamar.engine.Pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class SuperTacticoGame {
    private Player[] players;
    private Square[][] board;
    private int turns;
    private List<Piece> diedWithLifeShip; //to show dialog to select which pieces to save and kill the rest
    private int boardSize;

    /**
     * Initializes a game for testing reasons, *with no pieces*
     *
     * @param board the board of the game
     */
    public SuperTacticoGame(Square[][] board) {
        turns = 0;
        this.boardSize = board.length;
        this.board = board;
        players = new Player[2];
        players[0] = new Player("player1", 0);
        players[1] = new Player("player2", 1);
    }

    /**
     * Creates a game with the default map and the pieces from the fake games
     *
     * @param p1name the name of the 1st player
     * @param p2name the name of the 2nd player
     * @param p1FakeGame the fake game of player1
     * @param p2FakeGame the fake game of player2
     */
    public SuperTacticoGame(String p1name, String p2name, SuperTacticoGame p1FakeGame, SuperTacticoGame p2FakeGame) {
        boardSize = 20; //Todo constant
        turns = 0;
        board = new Square[boardSize][boardSize];
        createBoard();
        players = new Player[2];
        players[0] = new Player(p1name, 0);
        players[1] = new Player(p2name, 1);
        diedWithLifeShip = new ArrayList<>(2); //Todo if I will go in it than it should be initialized every turn or something
        for(Piece piece: p1FakeGame.getPlayers()[0].getLivingPieces()) {
            piece.setOwner(players[0]);
            insertPiece(piece);
        }
        for(Piece piece: p2FakeGame.getPlayers()[0].getLivingPieces()) {
            piece.setLocation(new Location(boardSize - piece.getLocation().getRow() - 1, piece.getLocation().getCol()));
            piece.setOwner(players[1]);
            insertPiece(piece);
        }
    }

    /**
     * Creates a fake game only for pieces organizing
     */
    public SuperTacticoGame() {
        players = new Player[]{new Player("demo", 0)};
        boardSize = 20; //Todo constant
        turns = 0;
        board = new Square[boardSize][boardSize];
        createBoard();
        for(Piece piece : players[0].getLivingPieces())
            insertPiece(piece);
    }

    /**
     * Creates the default map
     */
    private void createBoard() {
        boardSize = 20; //Todo constant
        board = new Square[boardSize][boardSize];
        board[1][4] = new Square(LocType.LAND);
        board[1][7] = new Square(LocType.LAND);
        board[1][10] = new Square(LocType.LAND);
        board[2][2] = new Square(LocType.LAND);
        board[2][3] = new Square(LocType.LAND);
        board[2][4] = new Square(LocType.LAND);
        board[2][5] = new Square(LocType.LAND);
        board[2][6] = new Square(LocType.LAND);
        board[2][7] = new Square(LocType.LAND);
        board[2][10] = new Square(LocType.LAND);
        board[2][14] = new Square(LocType.LAND);
        board[2][14] = new Square(LocType.LAND);
        board[3][2] = new Square(LocType.LAND);
        board[3][3] = new Square(LocType.LAND);
        board[3][4] = new Square(LocType.LAND);
        board[3][5] = new Square(LocType.LAND);
        board[3][6] = new Square(LocType.LAND);
        board[3][7] = new Square(LocType.LAND);
        board[3][8] = new Square(LocType.LAND);
        board[3][10] = new Square(LocType.LAND);
        board[3][11] = new Square(LocType.LAND);
        board[3][12] = new Square(LocType.LAND);
        board[3][13] = new Square(LocType.LAND);
        board[3][14] = new Square(LocType.LAND);
        board[5][3] = new Square(LocType.LAND);
        board[5][4] = new Square(LocType.LAND);
        board[5][5] = new Square(LocType.LAND);
        board[5][6] = new Square(LocType.LAND);
        board[5][7] = new Square(LocType.LAND);
        board[5][8] = new Square(LocType.LAND);
        board[5][9] = new Square(LocType.LAND);
        board[5][10] = new Square(LocType.LAND);
        board[5][10] = new Square(LocType.LAND);
        board[5][11] = new Square(LocType.LAND);
        board[5][12] = new Square(LocType.LAND);
        board[5][13] = new Square(LocType.LAND);
        board[5][3] = new Square(LocType.LAND);
        board[5][5] = new Square(LocType.LAND);
        board[5][5] = new Square(LocType.LAND);
        board[5][6] = new Square(LocType.LAND);
        board[5][7] = new Square(LocType.LAND);
        board[5][8] = new Square(LocType.LAND);
        board[5][9] = new Square(LocType.LAND);
        board[5][10] = new Square(LocType.LAND);
        board[5][10] = new Square(LocType.LAND);
        board[5][11] = new Square(LocType.LAND);
        board[5][12] = new Square(LocType.LAND);
        board[5][13] = new Square(LocType.LAND);
        board[6][5] = new Square(LocType.LAND);
        board[6][6] = new Square(LocType.LAND);
        board[6][7] = new Square(LocType.LAND);
        board[6][8] = new Square(LocType.LAND);
        board[6][9] = new Square(LocType.LAND);
        board[6][10] = new Square(LocType.LAND);
        board[6][11] = new Square(LocType.LAND);
        board[6][12] = new Square(LocType.LAND);
        board[7][3] = new Square(LocType.LAND);
        board[7][4] = new Square(LocType.LAND);
        board[7][5] = new Square(LocType.LAND);
        board[7][6] = new Square(LocType.LAND);
        board[7][7] = new Square(LocType.LAND);
        board[7][8] = new Square(LocType.LAND);
        board[7][9] = new Square(LocType.LAND);
        board[7][10] = new Square(LocType.LAND);
        board[7][11] = new Square(LocType.LAND);
        board[7][12] = new Square(LocType.LAND);
        board[7][15] = new Square(LocType.LAND);
        board[7][16] = new Square(LocType.LAND);
        board[7][17] = new Square(LocType.LAND);
        board[8][4] = new Square(LocType.LAND);
        board[8][5] = new Square(LocType.LAND);
        board[8][6] = new Square(LocType.LAND);
        board[8][7] = new Square(LocType.LAND);
        board[8][8] = new Square(LocType.LAND);
        board[8][8] = new Square(LocType.LAND);
        board[8][10] = new Square(LocType.LAND);
        board[8][11] = new Square(LocType.LAND);
        board[8][12] = new Square(LocType.LAND);
        board[9][4] = new Square(LocType.LAND);
        board[9][5] = new Square(LocType.LAND);
        board[9][6] = new Square(LocType.LAND);


        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == null)
                    board[i][j] = new Square(LocType.SEA);
                if (i < board.length)
                    board[board.length - i - 1][j] = new Square(board[i][j].getLocType());
            }
        }
    }

    public Player[] getPlayers() {
        return players;
    }

    public void insertPiece(Piece piece) {
        board[piece.getLocation().getRow()][piece.getLocation().getCol()].setPiece(piece);
    }

    public Piece getPieceFromBoard(Location location) {
        return board[location.getRow()][location.getCol()].getPiece();
    }

    public LocType getLocTypeInLocation(Location location) {
        return board[location.getRow()][location.getCol()].getLocType();
    }

    public void movePiece(Piece piece, Location location) {
        board[piece.getLocation().getRow()][piece.getLocation().getCol()].setPiece(null);
        piece.setLocation(location);
        insertPiece(piece);
    }

    public Player getCurrentPlayer() {
        return players[turns % players.length];
    }

    public void removePiece(Location location) {
        board[location.getRow()][location.getCol()] = null;
    }

    public List<Piece> getDiedWithLifeShip() {
        return diedWithLifeShip;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public boolean isFake() {
        return players.length == 1;
    }

    /**
     * @return all living pieces on board
     */
    public List<Piece>getAllLivingPieces(){
        List<Piece> res = new ArrayList<>(players[0].getLivingPieces());
        if(!isFake())
            res.addAll(players[1].getLivingPieces());
        return res;
    }
}

