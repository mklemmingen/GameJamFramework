package com.gamejam.game.backend;

import com.gamejam.game.link.*;

public class BackMan implements FrontendZugriff {
    /*
        * This class implements the interface linking front to backend.
     */

    // ---------------- ATTRIBUTES

    private Board gameBoard;
    private GameState gameState;


    // ---------------- METHODS

    public BackMan() {
        // Constructor! ---------------------------

    }


    /*
        * This method is used to start the game: Clear the existing Board and set up a new one.
     */
    @Override
    public void startGame() {

        gameState = GameState.WHITE_TURN; // setting starting GameState

        // create new board and set it up - EMPTY BOARD
        Tile[][] tiles = new Tile[8][8];

        // in the first row, add kings, queens, bishops, knights, rooks
        tiles[0][0] = new Tile(Piece.ROOK, TeamColor.WHITE);
        tiles[0][1] = new Tile(Piece.KNIGHT, TeamColor.WHITE);
        tiles[0][2] = new Tile(Piece.BISHOP, TeamColor.WHITE);
        tiles[0][3] = new Tile(Piece.QUEEN, TeamColor.WHITE);
        tiles[0][4] = new Tile(Piece.KING, TeamColor.WHITE);
        tiles[0][5] = new Tile(Piece.BISHOP, TeamColor.WHITE);
        tiles[0][6] = new Tile(Piece.KNIGHT, TeamColor.WHITE);
        tiles[0][7] = new Tile(Piece.ROOK, TeamColor.WHITE);

        // in the second row, add all pawns white
        for (int i = 1; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                tiles[i][j] = new Tile(Piece.PAWN, TeamColor.WHITE);
            }
        }
        // at pos 5, add the programmer, at pos 6, add the chancellor
        tiles[1][3] = new Tile(Piece.PROGRAMMER, TeamColor.WHITE);
        tiles[1][4] = new Tile(Piece.CHANCELLOR, TeamColor.WHITE);

        // everything inbetween, make it a empty tile
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                tiles[i][j] = new Tile();
            }
        }

        // in the second to last row, add all pawns black
        for (int i = 6; i < 7; i++) {
            for (int j = 0; j < 8; j++) {
                tiles[i][j] = new Tile(Piece.PAWN, TeamColor.BLACK);
            }
        }
        tiles[6][3] = new Tile(Piece.PROGRAMMER, TeamColor.BLACK);
        tiles[6][4] = new Tile(Piece.CHANCELLOR, TeamColor.BLACK);

        // in the last row, add kings, queens, bishops, knights, rooks
        tiles[7][0] = new Tile(Piece.ROOK, TeamColor.BLACK);
        tiles[7][1] = new Tile(Piece.KNIGHT, TeamColor.BLACK);
        tiles[7][2] = new Tile(Piece.BISHOP, TeamColor.BLACK);
        tiles[7][3] = new Tile(Piece.QUEEN, TeamColor.BLACK);
        tiles[7][4] = new Tile(Piece.KING, TeamColor.BLACK);
        tiles[7][5] = new Tile(Piece.BISHOP, TeamColor.BLACK);
        tiles[7][6] = new Tile(Piece.KNIGHT, TeamColor.BLACK);
        tiles[7][7] = new Tile(Piece.ROOK, TeamColor.BLACK);

        // Transpose the matrix
        Tile[][] transposedTiles = new Tile[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                transposedTiles[i][j] = tiles[j][i];
            }
        }

        // Reverse the order of columns
        Tile[][] rotatedTiles = new Tile[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                rotatedTiles[i][j] = transposedTiles[i][7 - j];
            }
        }

        gameBoard = new Board(rotatedTiles);
    }

    /*
     * This method is used to get the board from the backend, it's used in the frontend to display the current board
     */
    @Override
    public Board getBoard() {
        return gameBoard;
    }

    /*
     * This method is used to get the current game state from the backend, it's used in the frontend to make sure
     * it's in the same state as the backend
     */
    @Override
    public GameState getGameState() {
        return gameState;
    }

    /*
     * This method is used to see if a move a Player has made is allowed. It's used in the frontend to make sure
     * that the backend controls whether a move would be allowed or not
     * returns: false if the move is not allowed, true if it is
     */
    @Override
    public boolean isValidMove(Coordinate start, Coordinate end) {
        try{
            // check if outside bounds of the board
            if (start.getX() < 0 || start.getX() > 7 || start.getY() < 0 || start.getY() > 7){
                return false;
            }
            // check if the colour of the piece is the same as the current turn
            if (gameState == GameState.WHITE_TURN){
                if (gameBoard.getGameBoard()[start.getX()][start.getY()].getTeamColor() != TeamColor.WHITE){
                    return false;
                }
            } else if (gameBoard.getGameBoard()[start.getX()][start.getY()].getTeamColor() != TeamColor.BLACK){
                    return false;
            }
            // if coordinate is the same, return false
            if (start.getX() == end.getX() && start.getY() == end.getY()){
                return false;
            }
            // if the target is the same colour, return false
            if (gameBoard.getGameBoard()[end.getX()][end.getY()].getTeamColor()
                    == gameBoard.getGameBoard()[start.getX()][start.getY()].getTeamColor()){
                return false;
            }
            return true;
        }
        catch (ArrayIndexOutOfBoundsException e){
            return false;
        }
    }

    /*
    * This method is used to move a piece on the board. Used by the frontend to move a piece of a coordinate
    * to another coordinate.
    * returns: false if no hit has taken place, true if a hit has taken place
     */
    @Override
    public boolean movePiece(Coordinate start, Coordinate end) {
        boolean returnBool = gameBoard.updateWithEmptyBehind(start, end);
        switchGameState();
        // returnBool true if a piece was hit
        if (returnBool){
            // check if the game is over
            if (isGameOver()){
                gameState = GameState.NOT_IN_GAME;
            }
        }
        return returnBool;
    }

    // ---------- DUMMIES for US TO USE IN TESTING FOR VALIDMOVE AND MOVEPIECE
    private void switchGameState(){
        // switch between white turn and black turn
        if (gameState == GameState.WHITE_TURN){
            gameState = GameState.BLACK_TURN;
        } else {
            gameState = GameState.WHITE_TURN;
        }
    }
    private boolean isGameOver(){
        // check if the game is over
        // iterate over the board and check if there are any pieces left
        Tile[][] tiles = gameBoard.getGameBoard();

        boolean whiteExists = false;
        boolean blackExists = false;
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (tiles[i][j].getPieceType() != null){
                    if (tiles[i][j].getPieceType()==Piece.KING) {
                        if (tiles[i][j].getTeamColor() == TeamColor.WHITE) {
                            whiteExists = true;
                        } else {
                            blackExists = true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
